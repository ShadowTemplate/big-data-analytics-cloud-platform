package it.uniba.di.bdacp.task;

import com.google.code.externalsorting.ExternalSort;
import it.uniba.di.bdacp.comparator.DoubleValuesComparator;
import it.uniba.di.bdacp.comparator.EntryValuesComparator;
import it.uniba.di.bdacp.comparator.IntegerValuesComparator;
import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.indexer.IndexEntriesComparator;
import it.uniba.di.bdacp.comparator.LongValuesComparator;
import it.uniba.di.bdacp.tools.Utility;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExternalSortIndexTask implements PlatformTask {

    private String[] args;

    /**
     * @param args index chunks directory,
     *             sorted index path,
     *             key-value separator,
     *             value type,
     *             file names list exceptions
     */
    ExternalSortIndexTask(String[] args) throws InvalidArgumentsException {
        if(args.length < 4) {
            throw new InvalidArgumentsException("Arguments number insufficient to execute external_sort_index task."
                    + Utility.LINE_SEP + "usage: java -jar BDACP.jar external_sort_index index_chunks_path output_file_path key_value_separator value_type [filename_exception1 filename_exception2 ...]");
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

        EntryValuesComparator parser;
        switch (args[3]) {
            case "Long":
                parser = new LongValuesComparator();
                break;
            case "Double":
                parser = new DoubleValuesComparator();
                break;
            case "Integer":
            default:
                parser = new IntegerValuesComparator();
        }

        final List<String> exceptions = new ArrayList<>();
        for (int i = 4; i < args.length; i++) {
            exceptions.add(args[i]);
        }

        System.out.println("Starting index sorting.");

        File folder = new File(args[0]);
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && !exceptions.contains(pathname.getName()) && !pathname.isHidden();
            }
        });

        Comparator<String> comparator = new IndexEntriesComparator(parser, keyValueSeparator);

        List<File> chunks = new ArrayList<>();

        for(File f : files) {
            chunks.addAll(ExternalSort.sortInBatch(new File(f.getAbsolutePath()), comparator,
                    ExternalSort.DEFAULTMAXTEMPFILES, Charset.defaultCharset(), null, false, 0, false));
        }

        ExternalSort.mergeSortedFiles(chunks, new File(args[1]), comparator, Charset.defaultCharset(),
                false, false, false);

        System.out.println("Task completed.");

    }
}
