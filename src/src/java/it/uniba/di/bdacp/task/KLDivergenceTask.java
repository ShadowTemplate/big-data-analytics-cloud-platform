package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.indexer.IndexUtility;
import it.uniba.di.bdacp.tools.KLDivergenceJob;
import it.uniba.di.bdacp.tools.Pair;
import it.uniba.di.bdacp.tools.Utility;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

public class KLDivergenceTask implements PlatformTask {

    private String[] args;

    /**
     * @param args indexes chunks path,
     *             output file path,
     *             first class prefix,
     *             second class prefix,
     *             term-count separator
     */
    KLDivergenceTask(String[] args) throws InvalidArgumentsException {
        if(args.length < 5) {
            throw new InvalidArgumentsException("Arguments number insufficient to execute kl_divergence task."
                    + Utility.LINE_SEP + "usage: java -jar BDACP.jar kl_divergence indexes_directory output_directory first_class_prefix second_class_prefix term-count_separator");
        }

        if(args[2].equals(args[3])) {
            throw new InvalidArgumentsException("Classes prefixes must be different.");
        }

        this.args = args;
    }

    @Override
    public void executeTask() throws Exception {
        String keyValueSeparator;
        switch (args[4]) {
            case "tab":
                keyValueSeparator = "\t";
                break;
            case "newline":
                keyValueSeparator = "\n";
                break;
            default:
                keyValueSeparator = args[4];
        }

        Pair<Pair<Long, Long>, Pair<Long, Long>> indexesCounts = IndexUtility.getIndexCounts(args[0], args[2], keyValueSeparator);

        String[] taskArgs = new String[9];
        System.arraycopy(args, 0, taskArgs, 0, 4);
        taskArgs[4] = keyValueSeparator;
        taskArgs[5] = indexesCounts.getFirst().getFirst().toString();
        taskArgs[6] = indexesCounts.getFirst().getSecond().toString();
        taskArgs[7] = indexesCounts.getSecond().getFirst().toString();
        taskArgs[8] = indexesCounts.getSecond().getSecond().toString();

        ToolRunner.run(new Configuration(), new KLDivergenceJob(), taskArgs);
    }

}
