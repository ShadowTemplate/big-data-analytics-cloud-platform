package it.uniba.di.bdacp.crawler.twitter.entities;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {

    public String type;
    public List<Float> coordinates;

    @Override
    public String toString() {
        return "Coordinates{" + "type=" + type + ", coordinates=" + coordinates + '}';
    }

}
