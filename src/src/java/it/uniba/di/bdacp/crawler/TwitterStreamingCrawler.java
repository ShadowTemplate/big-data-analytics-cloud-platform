package it.uniba.di.bdacp.crawler;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.uniba.di.bdacp.crawler.twitter.ClassTask;
import it.uniba.di.bdacp.crawler.twitter.CrawledTweet;
import it.uniba.di.bdacp.crawler.twitter.filter.TweetFilter;
import it.uniba.di.bdacp.crawler.twitter.filter.TweetFilterFactory;
import it.uniba.di.bdacp.crawler.twitter.handler.TweetHandler;
import it.uniba.di.bdacp.crawler.twitter.handler.TweetHandlerFactory;
import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.tools.Utility;
import org.codehaus.jackson.map.ObjectMapper;


public class TwitterStreamingCrawler implements Crawler {

    private final BlockingQueue<String> messagesQueue;
    private final Properties conf;
    private final List<ClassTask> tasks;
    private final List<Client> clients;

    TwitterStreamingCrawler(Properties conf) {
        this.messagesQueue = new LinkedBlockingQueue<>(Integer.parseInt(conf.getProperty("messages_queue_size")));
        this.conf = conf;
        this.tasks = getTasksList(conf);
        this.clients = new ArrayList<>();
        for (ClassTask task : this.tasks) {
            clients.add(getTwitterClient(conf, task.getKeywords()));
        }
    }

    @Override
    public void start(int seconds) {
        System.out.println("Task(s) to be executed: " + clients.size());

        ObjectMapper mapper = new ObjectMapper();
        TweetFilter filter;
        try {
            filter = TweetFilterFactory.build(conf);
        } catch (FactoryException e) {
            System.err.println("Unable to build a filter for incoming tweets. Task will be skipped." + Utility.LINE_SEP + e.getMessage());
            return;
        }

        int i = 0;
        for(Client client : clients) {
            System.out.println("Client " + (i+1) + " - " + tasks.get(i));
            TweetHandler handler;
            try {
                handler = TweetHandlerFactory.build(conf, tasks.get(i));
            } catch (FactoryException e) {
                System.err.println("Unable to build an handler for incoming tweets. Task will be skipped." + Utility.LINE_SEP + e.getMessage());
                continue;
            }

            long currTime = System.currentTimeMillis();
            client.connect();
            while ((System.currentTimeMillis() - currTime) < seconds * 1000 || !messagesQueue.isEmpty()) {
                String jsonTweet = "";
                try {
                    jsonTweet = messagesQueue.take();
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }

                try {
                    long crawledTime = System.currentTimeMillis();
                    CrawledTweet newTweet = mapper.readValue(jsonTweet, CrawledTweet.class);

                    System.out.println(newTweet.toString());
                    System.out.println("RT count:" + newTweet.retweet_count);
                    if(filter.accept(newTweet)) {
                        handler.handleTweet(newTweet);
                        filter.update(newTweet, crawledTime);
                    } else {
                        System.out.println("~~~ Tweet skipped. ~~~");
                    }
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }

            client.stop();
            handler.close();
            i++;
        }
    }

    private List<ClassTask> getTasksList(Properties conf) {
        List<ClassTask> tasks = new ArrayList<>();
        List<String> idList = Arrays.asList(conf.getProperty("classes").split(","));

        for (String classID : idList) {
            String formatter = conf.getProperty("seed_file");
            try {
                tasks.add(new ClassTask(classID, parseSeeds(String.format(formatter, classID))));
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return tasks;
    }

    private List<String> parseSeeds(String filename) throws IOException {
        BufferedReader in = null;

        try {
            StringBuilder keywordsList = new StringBuilder();
            in = new BufferedReader(new FileReader(filename));
            String buffer;
            while ((buffer = in.readLine()) != null) {
                keywordsList.append(buffer).append(",");
            }
            return Arrays.asList((keywordsList.substring(0, keywordsList.length() - 1)).split(","));
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private Client getTwitterClient(Properties conf, List<String> keywords) {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        hosebirdEndpoint.trackTerms(keywords);
        hosebirdEndpoint.languages(Lists.newArrayList(conf.getProperty("tweets_language")));
        Authentication hosebirdAuth = new OAuth1(
                conf.getProperty("consumer_key"),
                conf.getProperty("consumer_secret"),
                conf.getProperty("token"),
                conf.getProperty("token_secret"));

        ClientBuilder builder = new ClientBuilder()
                .name(TwitterStreamingCrawler.class.getSimpleName())
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(messagesQueue));

        return builder.build();
    }

}
