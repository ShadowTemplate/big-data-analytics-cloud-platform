package it.uniba.di.bdacp.crawler.twitter.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    public Integer followers_count;
    public Integer friends_count;
    public Boolean geo_enabled;
    public String id_str;
    public Integer listed_count;
    public String location;
    public Integer statuses_count;
}
