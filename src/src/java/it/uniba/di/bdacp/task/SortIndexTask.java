package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.indexer.IndexUtility;
import it.uniba.di.bdacp.tools.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortIndexTask implements PlatformTask {

    private String[] args;

    /**
     * @param args index chunks directory,
     *             sorted index path,
     *             key-value separator,
     *             value type,
     *             index entries number,
     *             file names list exceptions
     */
    SortIndexTask(String[] args) throws InvalidArgumentsException {
        if(args.length < 5) {
            throw new InvalidArgumentsException("Arguments number insufficient to execute sort_index task."
                    + Utility.LINE_SEP + "usage: java -jar BDACP.jar sort_index index_chunks_path output_file_path key_value_separator value_type index_entries_number [filename_exception1 filename_exception2 ...]");
        }

        this.args = args;
    }

    @Override
    public void executeTask() throws Exception {
        String keyValueSeparator;
        switch (args[2]) {
            case "tab":
                keyValueSeparator = "\t";
                break;
            case "newline":
                keyValueSeparator = "\n";
                break;
            default:
                keyValueSeparator = args[2];
        }

        Class type;
        switch (args[3]) {
            case "Long":
                type = Long.class;
                break;
            case "Double":
                type = Double.class;
                break;
            case "Integer":
            default:
                type = Integer.class;
        }

        List<String> exceptions = new ArrayList<>();
        for (int i = 5; i < args.length; i++) {
            exceptions.add(args[i]);
        }

        System.out.println("Starting index sorting.");
        List<Map.Entry<String, Object>> sortedIndex = IndexUtility.sortFolderIndexByValue(args[0], keyValueSeparator, type, exceptions);

        int entriesNumber = args[4].equals("all") ? sortedIndex.size() : Integer.parseInt(args[4]);
        PrintWriter out = new PrintWriter(new FileOutputStream(new File(args[1]), true));

        for(int i = 0; i < entriesNumber; i++) {
            out.println(sortedIndex.get(i).getKey() + keyValueSeparator + sortedIndex.get(i).getValue());
        }
        System.out.println("Task completed.");
    }
}
