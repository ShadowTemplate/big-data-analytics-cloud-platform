package it.uniba.di.bdacp.crawler.twitter;

import java.util.List;

public class ClassTask {
    
    private String classID;
    private List<String> keywords;
    
    public ClassTask(String classID, List<String> keywords) {
        this.classID = classID;
        this.keywords = keywords;
    }

    public String getClassID() {
        return classID;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return "Task: {" +
                "classID='" + classID + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}
