package it.uniba.di.bdacp.crawler.twitter.handler;

import it.uniba.di.bdacp.crawler.twitter.CrawledTweet;
import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.tools.ChunksBuilder;
import it.uniba.di.bdacp.tools.Utility;

import java.io.IOException;
import java.util.Properties;

public class TweetSerializerHandler implements TweetHandler {

    private final ChunksBuilder out;

    TweetSerializerHandler(Properties config) throws InvalidArgumentsException {
        try {
            out = new ChunksBuilder(config);
        } catch (IOException e) {
            throw new InvalidArgumentsException("Unable to build serializer handler." + Utility.LINE_SEP + e.getMessage());
        }
    }

    @Override
    public void handleTweet(CrawledTweet tweet) {
        try {
            out.addData(tweet.getText());
        } catch (IOException e) {
            System.err.println("Error in serializing the tweet: " + tweet.getText() + Utility.LINE_SEP + e.getMessage());
        }
    }

    @Override
    public void close() {
        out.close();
    }
}
