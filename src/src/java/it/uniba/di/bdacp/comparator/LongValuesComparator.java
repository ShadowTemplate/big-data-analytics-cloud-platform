package it.uniba.di.bdacp.comparator;

public class LongValuesComparator extends EntryValuesComparator {
    public int compareEntries(String line1, String line2, String separator) {
        return new Long(line1.substring(line1.lastIndexOf(separator) + 1, line1.length())).compareTo(new Long(line2.substring(line2.lastIndexOf(separator) + 1, line2.length())));
    }
}
