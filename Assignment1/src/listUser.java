        
import java.io.IOException;
import java.util.*;
        
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
        
public class listUser {
        
 public static class Map extends Mapper<LongWritable, Text, Text, NullWritable> {
    //private final static NullWritable one = new NullWritable(1);
    private Text word = new Text();
        
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	String read = value.toString();
		String userId = read.split("::")[0];
		String gender = read.split("::")[1];
		String age = read.split("::")[2];
		int tempAge = Integer.parseInt(age);
		if (tempAge <= 7 && gender.equals("M")) {
			word.set(userId);

			context.write(word, NullWritable.get());
		}

    }
 } 
        
 public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    public void reduce(Text key, Iterator<IntWritable> values, Context context) 
      throws IOException, InterruptedException {
        
        }
    }
 
        
 public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
        
        @SuppressWarnings("deprecation")
		Job job = new Job(conf, "listUser");
    job.setJarByClass(listUser.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(NullWritable.class);
        
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
        
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
        
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
    job.waitForCompletion(true);
 }
        
}