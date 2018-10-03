package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.crawler.Crawler;
import it.uniba.di.bdacp.crawler.CrawlerFactory;
import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.tools.Utility;

import java.io.FileReader;
import java.util.Properties;

public class CrawlerTask implements PlatformTask {

    private String[] args;

    /**
     * @param args crawler's configuration file path
     */
    CrawlerTask(String[] args) throws InvalidArgumentsException {
        if(args.length < 1) {
            throw new InvalidArgumentsException("Arguments number insufficient to execute crawler task."
                    + Utility.LINE_SEP + "usage: java -jar BDACP.jar crawler crawler_configuration_file_path");
        }

        this.args = args;
    }

    @Override
    public void executeTask() throws Exception {
        Properties conf = new Properties();
        conf.load(new FileReader(args[0]));

        Crawler crawler = CrawlerFactory.build(conf);
        System.out.println("Starting crawler.");
        crawler.start(Integer.parseInt(conf.getProperty("timeout_interval_s")));
        System.out.println("Task completed.");
    }
}
