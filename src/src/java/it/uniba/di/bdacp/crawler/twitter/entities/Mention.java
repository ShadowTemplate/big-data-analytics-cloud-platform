package it.uniba.di.bdacp.crawler.twitter.entities;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mention {

    public String id_str;
    public List<Integer> indices;
}
