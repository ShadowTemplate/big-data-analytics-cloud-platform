package it.uniba.di.bdacp.parser;

import it.uniba.di.bdacp.tools.ChunksBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class ITWACParser {

    private ChunksBuilder chunksBuilder;
    private final int wordsPerLine = 30;

    public ITWACParser(ChunksBuilder chunksBuilder) {
        this.chunksBuilder = chunksBuilder;
    }

    public void parseFile(File f) throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(f)), "ISO-8859-1"));
            String buffer;
            StringBuilder text = new StringBuilder();
            int counter = 0;

            while((buffer = in.readLine()) != null) {
                if(buffer.startsWith("</s>")) {
                    chunksBuilder.addData(text.toString());
                    text = new StringBuilder();
                } else if (!buffer.startsWith("<")) {
                    text.append(buffer.substring(0, buffer.indexOf("\t"))).append(" ");
                    counter++;
                    if(counter == wordsPerLine) {
                        counter = 0;
                        text.append("\r\n");
                    }
                }
            }

            if(text.length() != 0) { // Writes the last words of the parsed file (it could not have ended with a </s>)
                chunksBuilder.addData(text.append("\r\n").toString());
            }

        } finally {
            if(in != null)
                in.close();
        }
    }
}
