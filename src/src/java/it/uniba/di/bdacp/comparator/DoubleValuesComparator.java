package it.uniba.di.bdacp.comparator;

public class DoubleValuesComparator extends EntryValuesComparator {
    public int compareEntries(String line1, String line2, String separator) {
        return new Double(line1.substring(line1.lastIndexOf(separator) + 1, line1.length())).compareTo(new Double(line2.substring(line2.lastIndexOf(separator) + 1, line2.length())));
    }
}
