package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.indexer.LinesIndexerJob;
import it.uniba.di.bdacp.tools.Utility;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;


public class BuildIndexTask implements PlatformTask {

    private String[] args;

    /**
     * @param args source chunks directory,
     *             output index (non-existing) directory,
     *             lines tokenizer identifier
     *             per-line prefix output index
     */
    BuildIndexTask(String[] args) throws InvalidArgumentsException {
        if(args.length < 3) {
            throw new InvalidArgumentsException("Arguments number insufficient to execute build_index task."
                    + Utility.LINE_SEP + "usage: java -jar BDACP.jar build_index source_chunks_directory output_index_directory tokenizer_id [lines_prefix]");
        }

        this.args = args;
    }

    @Override
    public void executeTask() throws Exception {
        ToolRunner.run(new Configuration(), new LinesIndexerJob(), args);
    }
}
