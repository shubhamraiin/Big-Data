/*
 * Map Reduce Project
 * Shubham Rai
 * scr130130
 */

/*
 * To compute the total number of crime incidents of each crime type in each region. 
 * I considered following definitions for a region:
 * The crime location is defined by a coordinate system (East, North). East and North are defined by 6 digit numbers.
 * Region definition 1: Using only the first digits of the coordinate system.
 * Region definition 2: Using the first three digits of the coordinate system.
 * 
 * User can enter any region definition from 1 to 6 by passing the number in argument 3   
 */


import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;



@SuppressWarnings("unused")
public class CrimeAnalysis {
	
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, IntWritable> {
		// variable to hold the parsed key data
		private Text totalCrimeRegion = new Text(); 
		private final static IntWritable one = new IntWritable(1); 
		// Variable to hold the command line argument for  North and East mapping
		private static int NEmap;
		
		// function to receive the command line argument passed by user to the mapper class
		public void configure(JobConf job) { 
			// limit holds the args[3]
			NEmap = Integer.parseInt(job.get("limit")); 
		}
		// function to check that parsed data is not a alphabetic token
		public static boolean isNumeric(String str) 
		{
			return str.matches("\\d*(\\.\\d+)?"); // check if the given data matched the regex of the numeric
		}
		// mapper function
		public void map(LongWritable key, Text value,
				OutputCollector<Text, IntWritable> outputCollector,
				Reporter reporter) throws IOException { 
			// variable to hold the parsed lines from the file
			String line = ""; 
			// Variable to hold the north coordinates
			String strEast = "";  
			// variable to hold the east coordinates 
			String strNorth = ""; 
			// variable to hold the crime types
			String crimeCategory = ""; 
			int div = 0;
			switch (NEmap) {
			// if the args[2] is 1 then it maps the North-East on 1st digit only
			case 1:
				div = 100000; 
				break;
			case 2:
				div = 10000;
				break;
				// if the args[2] is 3 then it maps the North-East first 3 digits only
			case 3:
				div = 1000;
				break;
			case 4:
				div = 100;
				break;
			case 5:
				div = 10;
				break;
			case 6:
				div = 1;
				break;
			default:
				System.out.println("Invalid region code");
				break;
			}
			int eastCord = 999999, northCord = 999999, eas = 0, nor = 0;
			try {
				 // to read line by line of the input file
				line = value.toString();
				// 4th field of the CSV contains easting information
				strEast = line.split(",")[4]; 
				// 5th field of the CSV contains nothing information
				strNorth = line.split(",")[5]; 
				// parses the crime type field
				crimeCategory = line.split(",")[7]; 
			} catch (IndexOutOfBoundsException e) { 
				// concatenate the data to the key field
				String locationKey = "(" + "Invalid Records" + ")"; 
				totalCrimeRegion.set(locationKey); // assign the data to the key filed
				outputCollector.collect(totalCrimeRegion, one); 
			}
			if (crimeCategory != null && crimeCategory.length() != 0
					&& strEast != null && strEast.length() != 0
					&& strNorth != null && strNorth.length() != 0) 
			{ 
				if (isNumeric(strEast) && isNumeric(strNorth))
				{ 
					eastCord = Integer.parseInt(strEast);
					northCord = Integer.parseInt(strNorth); 
					eas = eastCord - eastCord % div; // map the digits according to the user input
					nor = northCord - northCord % div;
				}
				// concatenate the data to the key field
				String locationKey = "(" + String.valueOf(eas) + "," + String.valueOf(nor) + "," + crimeCategory + ")"; 
				totalCrimeRegion.set(locationKey); // assign the data to the key filed
				outputCollector.collect(totalCrimeRegion, one); 
			}
		}
	}

	// reducer class
	public static class Reduce extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> 
	{
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			int sum = 0;
			while (values.hasNext()) { 
				sum += values.next().get(); 
			}
			// write the contents to the collector
			output.collect(key, new IntWritable(sum)); 
		}
	}

	// main function
	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(CrimeAnalysis.class); 
		@SuppressWarnings("deprecation")
		Job job = new Job(conf);
		conf.setJobName("CrimeAnalysis"); 
		// error handling if the number of parameters are less then 3
		if (args.length < 3) { 
			System.out.println(" Usage: <input_File> <output_file> <Region Definition:1 or 3 etc > <No_of_Mapper> <No_of_Reducer>");
		}
		// second argument for region definition
		conf.set("limit", args[2]); 
		// third argument for mapper Split size
		if (args.length == 4 && Integer.parseInt(args[3]) != 0) 
		{ 
			TextInputFormat.setMinInputSplitSize(job,
					Integer.parseInt(args[3]) * 1024 * 1024);
		}
		// fourth argument for number of Reducer tasks
		if (args.length == 5 && Integer.parseInt(args[4]) != 0) 
		{
			conf.setNumReduceTasks(Integer.parseInt(args[4]));
		}
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		conf.setInputFormat(org.apache.hadoop.mapred.TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		JobClient.runJob(conf);
	}
}
