package it.uniba.di.bdacp.comparator;

public class IntegerValuesComparator extends EntryValuesComparator {
    public int compareEntries(String line1, String line2, String separator) {
        return new Integer(line1.substring(line1.lastIndexOf(separator) + 1, line1.length())).compareTo(new Integer(line2.substring(line2.lastIndexOf(separator) + 1, line2.length())));
    }
}
