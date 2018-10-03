package it.uniba.di.bdacp.crawler.twitter.handler;

import it.uniba.di.bdacp.crawler.twitter.CrawledTweet;

public interface TweetHandler {
    public void handleTweet(CrawledTweet tweet);

    public void close();
}
