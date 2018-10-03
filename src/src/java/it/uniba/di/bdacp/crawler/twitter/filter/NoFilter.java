package it.uniba.di.bdacp.crawler.twitter.filter;

import it.uniba.di.bdacp.crawler.twitter.CrawledTweet;

public class NoFilter implements TweetFilter {
    @Override
    public boolean accept(CrawledTweet tweet) {
        return true;
    }

    @Override
    public void update(CrawledTweet tweet, long crawledTime) {
    }
}
