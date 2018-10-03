package it.uniba.di.bdacp.crawler.twitter.handler;

import it.uniba.di.bdacp.crawler.twitter.ClassTask;
import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.tools.Utility;

import java.util.Properties;

public class TweetHandlerFactory {

    public static TweetHandler build(Properties conf, ClassTask task) throws FactoryException {
        switch (conf.getProperty("tweet_handler_type")) {
            case "file_serializer":
                Properties config = new Properties();
                config.put("folder_path", String.format(conf.getProperty("folder_path"), task.getClassID()));
                config.put("file_name", String.format(conf.getProperty("file_name"), task.getClassID()));
                config.put("suffix", conf.getProperty("suffix"));
                config.put("max_file_size", conf.getProperty("max_file_size"));
                try {
                    return new TweetSerializerHandler(config);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            default:
                throw new FactoryException("Invalid tweet handler specified." + Utility.LINE_SEP + Utility.getTweetHandlerSuggestion());
        }
    }
}
