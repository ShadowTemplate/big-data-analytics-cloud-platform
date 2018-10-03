package it.uniba.di.bdacp.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Utility<T> {

    public static final String LINE_SEP = System.getProperty("line.separator");

    private final Class<T> type;

    public Utility(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public static String getTaskSuggestion(){
        return "Please specify one of the following task identifier:"
                + Utility.LINE_SEP + "ITWAC_corpus_converter"
                + Utility.LINE_SEP + "crawler"
                + Utility.LINE_SEP + "build_index"
                + Utility.LINE_SEP + "sort_index"
                + Utility.LINE_SEP + "kl_divergence"
                + Utility.LINE_SEP;
    }

    public static String getTweetHandlerSuggestion() {
        return "Please specify one of the following tweets handler:"
                + Utility.LINE_SEP + "file_serializer"
                + Utility.LINE_SEP;
    }

    public static String getCrawlerSuggestion() {
        return "Please specify one of the following crawler:"
                + Utility.LINE_SEP + "twitter_streaming_crawler"
                + Utility.LINE_SEP;
    }

    public static String getTokenizerSuggestion() {
        return "Please specify one of the following tokenizer:"
                + Utility.LINE_SEP + "plain_text_tokenizer"
                + Utility.LINE_SEP + "tweets_tokenizer"
                + Utility.LINE_SEP;
    }

    public static String getTweetFilterSuggestion() {
        return "Please specify one of the following tweet filter:"
                + Utility.LINE_SEP + "spam"
                + Utility.LINE_SEP + "none"
                + Utility.LINE_SEP;
    }

    public Map<String, T> getKeyValueMap(String fileName, String valueSeparator) throws IOException {
        Constructor constructor = null;
        try {
            constructor = getType().getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            return null;
        }

        BufferedReader in = null;
        Map<String, T> mapFreq = new HashMap<>();

        try {
            in = new BufferedReader(new FileReader(fileName));
            String[] splits;
            String buffer;
            while ((buffer = in.readLine()) != null) {
                splits = buffer.split(valueSeparator);
                try {
                    mapFreq.put(splits[0], (T) constructor.newInstance(splits[1]));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    return null;
                }
            }
        } finally {
            if (in != null)
                in.close();
        }

        return mapFreq;
    }

}
