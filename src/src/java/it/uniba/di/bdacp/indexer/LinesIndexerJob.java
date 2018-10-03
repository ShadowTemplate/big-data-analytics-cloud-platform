package it.uniba.di.bdacp.indexer;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import it.uniba.di.bdacp.tokenizer.Tokenizer;
import it.uniba.di.bdacp.tokenizer.TokenizerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class LinesIndexerJob extends Configured implements Tool {

    private static Tokenizer tokenizer;
    private static String prefix;

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, LongWritable> {

        private final static LongWritable one = new LongWritable(1);
        private Text word = new Text();

        public void map(LongWritable key, Text value, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException {
            List<String> tokens = tokenizer.tokenize(value.toString());
            for(String token : tokens) {
                word.set(token);
                output.collect(word, one);
            }
        }
    }

    public static class Combine extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable> {
        public void reduce(Text key, Iterator<LongWritable> values, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException {
            long count = 0;
            while (values.hasNext()) {
                values.next();
                count++;
            }
            output.collect(key, new LongWritable(count));
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable> {
        public void reduce(Text key, Iterator<LongWritable> values, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException {
            long sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }

            if(prefix != null) {
                output.collect(new Text(prefix + key.toString()), new LongWritable(sum));
            } else {
                output.collect(key, new LongWritable(sum));
            }
        }
    }

    public int run(String[] args) throws Exception {
        System.out.println("Configuring Map-Reduce job.");
        JobConf conf = new JobConf(getConf(), LinesIndexerJob.class);
        conf.setJobName(LinesIndexerJob.class.getName());

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(LongWritable.class);

        conf.setMapperClass(Map.class);
        conf.setCombinerClass(Combine.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        tokenizer = TokenizerFactory.build(args[2]);
        prefix = args.length > 3 ? args[3] : null;

        JobClient.runJob(conf);
        return 0;
    }
}

/*

VERSIONE CON INTWRITABLE, senza cominber e senza prefisso

package it.uniba.di.bdacp.indexer;

import java.io.*;
import java.util.*;

import it.uniba.di.bdacp.tokenizer.Tokenizer;
import it.uniba.di.bdacp.tokenizer.TokenizerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

//LONG
//SPECULATIVE

public class LinesIndexerJob extends Configured implements Tool {

    private static Tokenizer tokenizer;

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();


        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            List<String> tokens = tokenizer.tokenize(value.toString());
            for(String token : tokens) {
                word.set(token);
                output.collect(word, one);
            }
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            output.collect(key, new IntWritable(sum));
        }
    }

    public int run(String[] args) throws Exception {
        System.out.println("Configuring Map-Reduce job.");
        JobConf conf = new JobConf(getConf(), LinesIndexerJob.class);
        conf.setJobName(LinesIndexerJob.class.getName());

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(Map.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        tokenizer = TokenizerFactory.build(args[2]);

        JobClient.runJob(conf);
        return 0;
    }
}


*
* */