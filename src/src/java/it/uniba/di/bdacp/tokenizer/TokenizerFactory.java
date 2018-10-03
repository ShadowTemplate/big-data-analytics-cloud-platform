package it.uniba.di.bdacp.tokenizer;

import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.tools.Utility;

import java.util.ArrayList;
import java.util.List;

public class TokenizerFactory {

    public static Tokenizer build(String type) throws FactoryException {
        switch (type) {
            case "plain_text_tokenizer":
                return new SimpleTextTokenizer();
            case "tweets_tokenizer":
                return new TweetsTokenizer();
            default:
                throw new FactoryException("Invalid tokenizer specified." + Utility.LINE_SEP + Utility.getTokenizerSuggestion());
        }
    }

    public static void main(String[] args) throws FactoryException {
        Tokenizer tok = TokenizerFactory.build("tweets_tokenizer");
        List<String> toks = tok.tokenize("ciao nell\"andare\" l'albero");
        for(String s : toks) {
            System.out.println(s);
        }

        /*
        List<String> temp = new ArrayList<>();
        temp.add("1");
        temp.add("2");
        temp.add("3");
        for(String s : temp) {
            System.out.println(s);
            if(s.equals("2"))
                temp.add("4");
        }
        */


    }

}
