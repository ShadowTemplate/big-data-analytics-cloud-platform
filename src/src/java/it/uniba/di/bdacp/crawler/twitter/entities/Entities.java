package it.uniba.di.bdacp.crawler.twitter.entities;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entities {

    public List<Hashtag> hashtags;
    public List<Mention> user_mentions;
}
