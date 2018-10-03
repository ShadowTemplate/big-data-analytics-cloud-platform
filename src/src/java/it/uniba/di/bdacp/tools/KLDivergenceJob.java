package it.uniba.di.bdacp.tools;

import java.io.*;
import java.util.*;

import it.uniba.di.bdacp.task.KLDivergenceTask;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class KLDivergenceJob extends Configured implements Tool {

    private static String firstDistribPrefix;
    private static int firstDistribPrefixLen;
    private static int secondDistribPrefixLen;
    private static String termCountSeparator;
    private static Long firstDistribLines;
    private static Long firstDistribSize;
    private static Long secondDistribLines;
    private static Long secondDistribSize;

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {
        public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
            String line = value.toString();
            boolean firstClass = line.startsWith(firstDistribPrefix);
            int separatorIndex = line.lastIndexOf(termCountSeparator);
            String term = firstClass ? line.substring(firstDistribPrefixLen, separatorIndex) : line.substring(secondDistribPrefixLen, separatorIndex);
            Long termFreq = Long.parseLong(line.substring(separatorIndex+1, line.length()));

            if(firstClass) {
                output.collect(new Text(term), new DoubleWritable((termFreq.doubleValue() + 1) / (firstDistribSize + firstDistribLines)));
            } else {
                output.collect(new Text(term), new DoubleWritable((termFreq.doubleValue() + 1) / (secondDistribSize + secondDistribLines) * -1));
            }
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        public void reduce(Text key, Iterator<DoubleWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
            double firstDistribProbability = 0;
            double secondDistribProbability = 0;

            while (values.hasNext()) {
                double curr = values.next().get();
                if(curr < 0) {
                    secondDistribProbability = curr * -1;
                } else {
                    firstDistribProbability = curr;
                }
            }

            if(firstDistribProbability == 0) {
                firstDistribProbability = ((double) 1) / (firstDistribSize + firstDistribLines);
            }

            if(secondDistribProbability == 0) {
                secondDistribProbability = ((double) 1) / (secondDistribSize + secondDistribLines);
            }

            output.collect(key, new DoubleWritable((Math.log(firstDistribProbability) - Math.log(secondDistribProbability)) * firstDistribProbability));
        }
    }

    public int run(String[] args) throws Exception {
        System.out.println("Configuring Map-Reduce job.");
        JobConf conf = new JobConf(getConf(), KLDivergenceTask.class);
        conf.setJobName(KLDivergenceTask.class.getName());

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(DoubleWritable.class);

        conf.setMapperClass(Map.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        firstDistribPrefix = args[2];
        String secondDistribPrefix = args[3];
        firstDistribPrefixLen = firstDistribPrefix.length();
        secondDistribPrefixLen = secondDistribPrefix.length();
        termCountSeparator = args[4];
        firstDistribLines = Long.parseLong(args[5]);
        firstDistribSize = Long.parseLong(args[6]);
        secondDistribLines = Long.parseLong(args[7]);
        secondDistribSize = Long.parseLong(args[8]);

        JobClient.runJob(conf);
        return 0;
    }
}