package it.uniba.di.bdacp.crawler.twitter.filter;

import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.tools.Utility;

import java.util.Properties;

public class TweetFilterFactory {

    public static TweetFilter build(Properties conf) throws FactoryException {
        switch(conf.getProperty("tweet_filter_type")) {
            case "spam":
                return new SpamFilter(conf);
            case "none":
                return new NoFilter();
            default:
                throw new FactoryException("Invalid tweet filter specified." + Utility.LINE_SEP + Utility.getTweetFilterSuggestion());
        }
    }
}
