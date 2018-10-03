package it.uniba.di.bdacp.crawler.twitter;

import it.uniba.di.bdacp.crawler.twitter.entities.Coordinates;
import it.uniba.di.bdacp.crawler.twitter.entities.Entities;
import it.uniba.di.bdacp.crawler.twitter.entities.Users;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CrawledTweet {
    public Coordinates coordinates;
    public String created_at;
    public Entities entities;
    //public Integer favorite_count;
    public String filter_level; //OKKIO
    public String id_str;
    public String in_reply_to_status_id_str;
    public String in_reply_to_user_id_str;
    //public String lang;
    //public Places place;
    public Integer retweet_count;
    public String text;
    public Users user;
    
    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrawledTweet that = (CrawledTweet) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
