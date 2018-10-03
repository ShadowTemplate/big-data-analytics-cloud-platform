package it.uniba.di.bdacp.indexer;

import it.uniba.di.bdacp.tools.Pair;
import it.uniba.di.bdacp.tools.Utility;

import java.io.*;
import java.util.*;

public class IndexUtility {

    public static <T extends Comparable<? super T>> List<Map.Entry<String, T>> sortFolderIndexByValue(String folderPath, String valueSeparator, Class<T> outputClass, final List<String> exceptionsList) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && !exceptionsList.contains(pathname.getName()) && !pathname.isHidden();
            }
        });

        if (files == null) {
            return new ArrayList<>();
        }

        Map<String, T> dic = new HashMap<>();
        Utility<T> utility = new Utility<>(outputClass);
        for (File f : files) {
            dic.putAll(utility.getKeyValueMap(f.getAbsolutePath(), valueSeparator));
        }

        return getSortedMapByValues(dic);
    }

    /*
    public static List<Map.Entry<String, Integer>> sortFolderIndexByValue(String folderPath, String valueSeparator, final List<String> exceptionsList) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && !exceptionsList.contains(pathname.getName()) && !pathname.isHidden();
            }
        });

        if (files == null) {
            return new ArrayList<>();
        }

        Map<String, Integer> dic = new HashMap<>();
        for (File f : files) {
            dic.putAll(Utility.getKeyValueMap(f.getAbsolutePath(), valueSeparator));
        }

        return getSortedMapByValues(dic);
    }*/

    private static <K,V extends Comparable<? super V>> List<Map.Entry<K, V>> getSortedMapByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }

    public static Pair<Pair<Long, Long>, Pair<Long, Long>> getIndexCounts(String folderPath, String firstDistribPrefix, String valueSeparator) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && !pathname.isHidden();
            }
        });

        if (files == null) {
            return null;
        }

        long firstDistribLines = 0;
        long firstDistribSize = 0;
        long secondDistribLines = 0;
        long secondDistribSize = 0;

        for (File f : files) {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String buffer;
            while ((buffer = in.readLine()) != null) {
                boolean firstClass = buffer.startsWith(firstDistribPrefix);
                long termFreq = Long.parseLong(buffer.substring(buffer.lastIndexOf(valueSeparator) + 1, buffer.length()));
                if(firstClass) {
                    firstDistribLines++;
                    firstDistribSize += termFreq;
                } else {
                    secondDistribLines++;
                    secondDistribSize += termFreq;
                }
            }
            in.close();
        }

        return new Pair<>(new Pair<>(firstDistribLines, firstDistribSize), new Pair<>(secondDistribLines, secondDistribSize));
    }
}
