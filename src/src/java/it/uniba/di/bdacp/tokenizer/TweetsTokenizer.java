package it.uniba.di.bdacp.tokenizer;

import cmu.arktweetnlp.Twokenize;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

public class TweetsTokenizer implements Tokenizer {

    private Analyzer analyzer;

    TweetsTokenizer() {
        analyzer = new TweetAnalyzer();
    }

    @Override
    public List<String> tokenize(String text) {
        List<String> tokensList = new ArrayList<>();
        TokenStream stream = null;
        try {
             stream = analyzer.tokenStream("tweet", text);

            // get the CharTermAttribute from the TokenStream
            CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

            stream.reset();

            // print all tokens until stream is exhausted
            while (stream.incrementToken()) {
                tokensList.add(termAtt.toString());
            }

            stream.end();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return tokensList;
    }

    public static void main(String[] args) throws IOException {
        // text to tokenize
        final String text = "http://to.url/ciao/lol Provo a condividere un tweet con il mio amico gianvito @djanvito #tweet4j #bdap";

        TweetsTokenizer tokenizer = new TweetsTokenizer();

        System.out.println(tokenizer.tokenize(text));
    }

}


class TweetAnalyzer extends Analyzer {
    public TweetAnalyzer() {}

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

        StringBuilder tokens = new StringBuilder();
        for(String tok : Twokenize.tokenizeRawTweetText(getStringFromReader(reader))) {
            if(tok.startsWith("#")) {
                tokens.append(tok.substring(1, tok.length()));
            } else {
                tokens.append(tok);
            }
            tokens.append(" ");
        }
        StringReader tokReader = new StringReader(tokens.toString());

        return new TokenStreamComponents(new WhitespaceTokenizer(Version.LUCENE_4_9, tokReader));

    }

    private String getStringFromReader(Reader reader) {

        try {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[128];
            int charRead;

            do {
                charRead = reader.read(buffer, 0, buffer.length);
                if(charRead > 0)
                    builder.append(buffer, 0, buffer.length);
            } while(charRead > 0);

            return builder.toString();

        } catch (IOException e) {
            return "";
        }
    }


}

