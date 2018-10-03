package it.uniba.di.bdacp.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleTextTokenizer implements Tokenizer {

    private Analyzer analyzer;

    SimpleTextTokenizer() {
        analyzer = new UAX29URLEmailAnalyzer(Version.LUCENE_4_9);
    }

    @Override
    public List<String> tokenize(String text) {
        List<String> list = new ArrayList<>();
        try {
            TokenStream tokens = analyzer.tokenStream("word", text);
            tokens.reset();

            while(tokens.incrementToken()) {
                list.add(tokens.getAttribute(CharTermAttribute.class).toString());
            }

            tokens.close();
        } catch (IOException e) { }
        return list;
    }
}
