package it.uniba.di.bdacp.crawler;

import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.tools.Utility;

import java.util.Properties;

public class CrawlerFactory {

    public static Crawler build(Properties conf) throws FactoryException {

        switch (conf.getProperty("crawler_id")) {
            case "twitter_streaming_crawler":
                return new TwitterStreamingCrawler(conf);
            default:
                throw new FactoryException("Invalid crawler specified." + Utility.LINE_SEP + Utility.getCrawlerSuggestion());
        }
    }
}
