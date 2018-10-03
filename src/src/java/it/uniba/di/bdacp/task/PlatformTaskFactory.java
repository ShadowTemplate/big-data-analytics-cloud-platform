package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.exceptions.FactoryException;
import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.tools.Utility;

import java.util.Arrays;

public class PlatformTaskFactory {

    public static PlatformTask build(String args[]) throws FactoryException {
        String taskID = args[0];
        String[] taskArgs = Arrays.copyOfRange(args, 1, args.length);

        switch(taskID) {
            case "ITWAC_corpus_converter":
                try {
                    return new ITWACCorpusConverterTask(taskArgs);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            case "crawler":
                try {
                    return new CrawlerTask(taskArgs);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            case "build_index":
                try {
                    return new BuildIndexTask(taskArgs);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            case "sort_index":
                try {
                    return new SortIndexTask(taskArgs);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            case "external_sort_index":
                try {
                    return new ExternalSortIndexTask(taskArgs);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            case "kl_divergence":
                try {
                    return new KLDivergenceTask(taskArgs);
                } catch (InvalidArgumentsException e) {
                    throw new FactoryException(e.getMessage());
                }
            default:
                throw new FactoryException("Invalid task specified." + Utility.LINE_SEP + Utility.getTaskSuggestion());
        }
    }
}
