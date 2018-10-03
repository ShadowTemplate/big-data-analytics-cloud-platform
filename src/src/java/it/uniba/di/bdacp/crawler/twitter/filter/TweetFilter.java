package it.uniba.di.bdacp.crawler.twitter.filter;

import it.uniba.di.bdacp.crawler.twitter.CrawledTweet;

public interface TweetFilter {
    public boolean accept(CrawledTweet tweet);

    public void update(CrawledTweet tweet, long crawledTime);
}
