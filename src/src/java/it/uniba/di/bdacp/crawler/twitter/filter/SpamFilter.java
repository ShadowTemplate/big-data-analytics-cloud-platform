package it.uniba.di.bdacp.crawler.twitter.filter;

import it.uniba.di.bdacp.crawler.twitter.CrawledTweet;

import java.util.HashMap;
import java.util.Properties;

public class SpamFilter extends Thread implements TweetFilter {
    private final long refreshRate;
    private final HashMap<String, Long> cachedTweets;
    private final long cacheTimeout;
    private final int wildcardLetterNumberForSpam = 3;

    SpamFilter(Properties conf) {
        this.cachedTweets = new HashMap<>();
        this.cacheTimeout = Long.parseLong(conf.getProperty("cache_timeout_s")) * 1000;
        this.refreshRate = Long.parseLong(conf.getProperty("cache_refresh_rate_s")) * 1000;
        start();
    }

    @Override
    public void run() {
        try {
            sleep(refreshRate);
        } catch (InterruptedException e) {
        }

        for(String tweet : cachedTweets.keySet()) {
            if(System.currentTimeMillis() - cachedTweets.get(tweet) > cacheTimeout) {
                cachedTweets.remove(tweet);
            }
        }
    }

    @Override
    public boolean accept(CrawledTweet tweet) {
        /*
        Twitter doesn't allow to post the same tweet many times.
        A spam technique is to post the same tweet after having appended 1 or 2 random letters to it.
        Example:
        "this is a spam tweet: http://example.ex"
        "this is a spam tweet: http://example.ex 1"
        "this is a spam tweet: http://example.ex 2"
        In order to avoid spam, the last 2 characters of the tweet will be ignored while checking for equality.
        */

        String newTweet = tweet.getText();
        int newTweetLen = newTweet.length();

        for(String cachedTweet : cachedTweets.keySet()) {
            int cachedTweetLen = cachedTweet.length();

            if(cachedTweetLen > wildcardLetterNumberForSpam &&
                    newTweetLen > wildcardLetterNumberForSpam &&
                    cachedTweetLen == newTweetLen &&
                    cachedTweet.substring(0, cachedTweetLen - wildcardLetterNumberForSpam).equals(newTweet.substring(0, newTweetLen - wildcardLetterNumberForSpam))) {
                return false;
            }

            if((Math.abs(cachedTweetLen - newTweetLen) <= wildcardLetterNumberForSpam) && (cachedTweet.startsWith(newTweet) || newTweet.startsWith(cachedTweet))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void update(CrawledTweet tweet, long crawledTime) {
        cachedTweets.put(tweet.getText(), crawledTime);
    }
}
