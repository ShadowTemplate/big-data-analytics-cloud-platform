package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.parser.ITWACParser;
import it.uniba.di.bdacp.tools.ChunksBuilder;
import it.uniba.di.bdacp.tools.Utility;

import java.io.*;
import java.util.Properties;

public class ITWACCorpusConverterTask implements PlatformTask {

    private String[] args;

    /**
     * @param args ITWAC archives directory,
     *             chunks builder's config file
     */
    ITWACCorpusConverterTask(String[] args) throws InvalidArgumentsException {
        if(args.length < 2) {
            throw new InvalidArgumentsException("Arguments number insufficient to execute ITWAC_corpus_converter task."
                    + Utility.LINE_SEP + "usage: java -jar BDACP.jar ITWAC_corpus_converter ITWAC_archives_directory chunks_builder_configuration_file");
        }

        this.args = args;
    }

    @Override
    public void executeTask() throws Exception {
        File itwacDirectory = new File(args[0]);
        File[] archivesFiles = itwacDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        } );

        if(archivesFiles != null) {
            Properties conf = new Properties();
            conf.load(new FileReader(args[1]));
            ChunksBuilder chunksBuilder = new ChunksBuilder(conf);
            ITWACParser parser = new ITWACParser(chunksBuilder);

            System.out.println("Starting ITWAC corpus conversion.");
            for(File f : archivesFiles) {
                try {
                    System.out.println("Processing file: " + f.getName());
                    parser.parseFile(f);
                } catch (IOException ex) {
                    System.err.println("Error in processing file: " + f.getName() + ". It will be skipped.");
                    System.err.println(ex.getMessage());
                }
            }
        }

        System.out.println("Task completed.");
    }
}

