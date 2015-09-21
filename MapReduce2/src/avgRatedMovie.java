

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

@SuppressWarnings("unused")
public class avgRatedMovie {
	
	public static class MapperRate extends
			Mapper<LongWritable, Text, Text, Text> {
	
		private Text rateValue = new Text();
		private Text keyRate = new Text();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] tokens = value.toString().split("::");
			String userId = tokens[0];
			String movieId = tokens[1];
			String rating = tokens[2];
			rateValue = new Text("R" + movieId + "," + rating);
			keyRate.set(userId);
			context.write(keyRate, rateValue);
		}
	}

	public static class MapperUser extends
			Mapper<LongWritable, Text, Text, Text> {
		private Text valueUser = new Text();
		private Text keyUser = new Text();
		

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String[] tokens = value.toString().split("::");
			String userId = tokens[0];
			String gender = tokens[1];
			if ("F".equalsIgnoreCase(gender)) {
				valueUser = new Text("U" + gender);
				keyUser.set(userId);
				context.write(keyUser, valueUser);
			}

		}

	}

	// The reducer class
	public static class ReduceOne extends Reducer<Text, Text, Text, Text> {
		private Text result = new Text();
		private Text myKey = new Text();
		private ArrayList<Text> listRate = new ArrayList<Text>();
		private ArrayList<Text> listUser = new ArrayList<Text>();
		

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			
		}

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {


			// Clear our lists
			listRate.clear();
			listUser.clear();
			String tmp;
			// iterates through all values, binning each record based on what it was tagged with. 
			for (Text val : values) {
				tmp = val.toString();
				if (tmp.charAt(0) == 'R') {
					listRate.add(new Text(tmp.toString().substring(1)));
				} else if (tmp.charAt(0) == 'U') {
					listUser.add(new Text(tmp.toString().substring(1)));
				}
			}
			executeJoinLogic(context);
			
		}
         
		private void executeJoinLogic(Context context) throws IOException,
				InterruptedException {
			if (!listRate.isEmpty() && !listUser.isEmpty()) {
				for (Text U : listUser) {
					for (Text R : listRate) {
						String[] mydata = R.toString().split(",");
						String ratingStr = mydata[1];
						String movieId = mydata[0];
						result.set("::" + ratingStr);
						myKey.set(movieId);
						context.write(myKey, result);

					}
				}
			}
		}
	}

	public static class MovieRatingMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text rating = new Text();
		private Text movieid = new Text(); // type of output key

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] mydata = value.toString().split("::");
			System.out.println(value.toString());
			rating.set(mydata[1].trim());
			movieid.set(mydata[0].trim());
			context.write(movieid, rating);
		}

	}
	public static class MovieNameMapper extends Mapper<LongWritable, Text, Text, Text> {
		private Text title = new Text();
		private Text movieid = new Text(); // type of output key

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] mydata = value.toString().split("::");
			movieid.set(mydata[0].trim());
			title.set("T" + mydata[1].trim());
			context.write(movieid, title);
		}
	}



	// The reducer class
	public static class ReduceTwo extends Reducer<Text, Text, Text, Text> {
		private Text result = new Text();
		private Text myKey = new Text();
		int sum = 0;
		private Map<String,Double> avgMap = new HashMap<>();
		private ArrayList<Text> listTitle = new ArrayList<Text>();
		private ArrayList<Text> listRating = new ArrayList<Text>();
		

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			listTitle.clear();
			listRating.clear();
			for (Text val : values) {
				String intermediate = val.toString();
				if (intermediate.charAt(0) == 'T') {
					String mydata = intermediate.substring(1);
					listTitle.add(new Text(mydata));

				} else {
					listRating.add(new Text(intermediate));
				}
			}
			executeJoinLogic(context);
		}
		private void executeJoinLogic(Context context) throws IOException,
		InterruptedException {
			
			if (!listTitle.isEmpty() && !listRating.isEmpty()) {
				for (Text title : listTitle) {
					sum=0;
					for (Text rating : listRating) {
						sum = sum + Integer.parseInt(rating.toString());
		
					}
					Double avg = (double) (sum/listRating.size());
					avgMap.put(title.toString(), avg);
				}
			}
		}
		@Override
		protected void cleanup(Context context) throws IOException,
		InterruptedException {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Map<String,Double> sortedMap = sortByValues((HashMap) avgMap);
			
			int counter =0;
			for(String key:sortedMap.keySet()){
				if(counter++==5){
					break;
				}
				myKey.set(key);
				result.set(String.valueOf(sortedMap.get(key)));
				context.write(myKey, result);
			}
		}
		 @SuppressWarnings({ "rawtypes", "unchecked" })
		private static HashMap sortByValues(HashMap map) { 
		       List list = new LinkedList(map.entrySet());
		      
		       Collections.sort(list, new Comparator() {
		            public int compare(Object o1, Object o2) {
		               return ((Comparable) ((Map.Entry) (o2)).getValue())
		                  .compareTo(((Map.Entry) (o1)).getValue());
		            }
		       });

		       //  copying the sorted list in toHashMap using LinkedHashMap to preserve the insertion order
		       HashMap sortedHashMap = new LinkedHashMap();
		       for (Iterator it = list.iterator(); it.hasNext();) {
		              Map.Entry entry = (Map.Entry) it.next();
		              sortedHashMap.put(entry.getKey(), entry.getValue());
		       } 
		       return sortedHashMap;
		  }
	}

	// Driver code
	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		final String OUTPUT_PATH = "/usr/lib/hue";
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err.println("Usage: JoinExample <in> <in2> <out>");
			System.exit(2);
		}
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "joinone");
		job.setJarByClass(avgRatedMovie.class);
		job.setReducerClass(ReduceOne.class);
		MultipleInputs.addInputPath(job, new Path(otherArgs[0]),
				TextInputFormat.class, MapperRate.class);
		MultipleInputs.addInputPath(job, new Path(otherArgs[1]),
				TextInputFormat.class, MapperUser.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));
		job.waitForCompletion(true);

		@SuppressWarnings("deprecation")
		Job job2 = new Job(conf, "jointwo");
		job2.setJarByClass(avgRatedMovie.class);
		job2.setReducerClass(ReduceTwo.class);
		MultipleInputs.addInputPath(job2, new Path(OUTPUT_PATH),
				TextInputFormat.class, MovieRatingMapper.class);
		MultipleInputs.addInputPath(job2, new Path(otherArgs[2]),
				TextInputFormat.class, MovieNameMapper.class);

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);

		FileOutputFormat.setOutputPath(job2, new Path(otherArgs[3]));
		job2.waitForCompletion(true);

	}

}
