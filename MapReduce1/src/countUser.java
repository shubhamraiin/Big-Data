        
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
        
public class countUser {
        
 public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
   private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
    {
    	
    	String read = value.toString();
		@SuppressWarnings("unused")
		String userId = read.split("::")[0];
		String gender = read.split("::")[1];
		String age = read.split("::")[2];
		int tempAge = Integer.parseInt(age);
		if (tempAge < 18)
			{
			  if(gender.equals("M")) 
			  {  
			      word.set("7"+" "+"M");
			      context.write(word,one);
			  }
			   else
			   {
			     word.set("7"+" "+"F");
			     context.write(word,one);
			   }	  
			}
		else if(tempAge >=18 && tempAge<=24)
		{
			if(gender.equals("M")) 
			  { 
				word.set("24"+" "+"M");
				 context.write(word,one);
			  }
			   else
			   {
				   word.set("24"+" "+"F");
				   context.write(word,one);
			   }
		 }
		else if(tempAge >=25 && tempAge<=34)
		{
			if(gender.equals("M")) 
			  { 
				 word.set("31"+" "+"M");
				 context.write(word,one);
			  }
			   else
			   {
				  word.set("31"+" "+"F");
				  context.write(word,one);
			   }      			  
			}
		else if(tempAge >=35 && tempAge<=44)
		{
			if(gender.equals("M")) 
			  { 
				word.set("41"+" "+"M");
				 context.write(word,one);
			  }
			   else
			   {
				  word.set("41"+" "+"F");
				  context.write(word,one);
			   }	      			  
		}
		else if(tempAge >=45 && tempAge<=55)
		{
			if(gender.equals("M")) 
			  { 
				word.set("51"+" "+"M");
				 context.write(word,one);
			  }
			   else
			   { 
				   word.set("51"+" "+"F");
				   context.write(word,one);
			   }	      			  
		}
		else if(tempAge >55 && tempAge<=61)
		{
			if(gender.equals("M")) 
			  {  
				word.set("56"+" "+"M");
				 context.write(word,one);
			  }
			   else
			   {
				  word.set("56"+" "+"F");
				  context.write(word,one);
			   }
		}
		else 
		{
			if(gender.equals("M")) 
			  { 
				word.set("62"+" "+"M");
				 context.write(word,one);
			  }
			   else
			   {
				 word.set("62"+" "+"F");
				 context.write(word,one);
			   }
			  		      			  
		 }			
	}
    }
 
        
 public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	 private IntWritable result=new IntWritable();
    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
      throws IOException, InterruptedException {
    	
    	int count = 0;
    	for(IntWritable val:values){
    		count+= val.get();
    		
    	}
    	result.set(count);
    	context.write(key,result);
    	
        
        }
    }
 
        
 public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
        
        @SuppressWarnings("deprecation")
		Job job = new Job(conf, "countUser");
    job.setJarByClass(countUser.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
        
    job.setMapperClass(Map.class);
   // job.setCombinerClass(Reduce.class);
    job.setReducerClass(Reduce.class);
        
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
        
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
    job.waitForCompletion(true);
 }
        
}