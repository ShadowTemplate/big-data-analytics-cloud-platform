package it.uniba.di.bdacp.indexer;

import it.uniba.di.bdacp.comparator.EntryValuesComparator;

import java.util.Comparator;

public class IndexEntriesComparator implements Comparator<String> {

    private final EntryValuesComparator parser;
    private final String separator;

    public IndexEntriesComparator(EntryValuesComparator parser, String separator) {
        this.parser = parser;
        this.separator = separator;
    }

    @Override
    public int compare(String o1, String o2) {
        return parser.compareEntries(o1, o2, separator);
    }
}
