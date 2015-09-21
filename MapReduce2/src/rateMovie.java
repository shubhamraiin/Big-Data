import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class rateMovie {

		//The Mapper classes and  reducer  code
	public static class Map1 extends Mapper<LongWritable, Text, Text, Text>{
		String mymovieid;
		static Map<String, String> myMap = new HashMap<String, String>();
		@Override
		protected void setup(Context context)
				throws IOException, InterruptedException {
			
			super.setup(context);

			Configuration conf = context.getConfiguration();
			mymovieid = conf.get("movieid"); // to retrieve movieid set in main method
			 myMap = new HashMap<String, String >();

			@SuppressWarnings("deprecation")
			Path[] localPaths = context.getLocalCacheFiles();
			for(Path myfile:localPaths)
			{
				String line=null;
				String nameofFile=myfile.getName();
				File file =new File(nameofFile+"");
				FileReader fr= new FileReader(file);
				BufferedReader br= new BufferedReader(fr);
				line=br.readLine();
			while(line!=null)
			{
					String[] arr=line.split("::");
					String gender=arr[1];
					String userId=arr[0];
					String age=arr[2].trim();
					String genderAge=gender+" "+age;
					myMap.put(userId,genderAge);
					line=br.readLine();
			}
			br.close();
			}
		}

		private Text userid=new Text();
		private Text genderAge=new Text();
	
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] mydata = value.toString().split("::");
			int rating=Integer.parseInt(mydata[2]);
			String uid=mydata[0].trim();
			String movieID=mydata[1].trim();
			if(movieID.equals(mymovieid)&& rating>=4){
				genderAge.set(myMap.get(uid));
				userid.set(uid);
			context.write(userid, genderAge);
			}
		}
	}



		//Driver code
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		// get all args
		if (otherArgs.length != 3) {
			System.err.println("Usage: JoinExample <in> <in2> <out> <anymovieid>");
			System.exit(2);
		}


		conf.set("movieid", otherArgs[2]); //setting global data variable for hadoop

		// create a job with name "joinexc" 
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "joinexc"); 
		job.setJarByClass(rateMovie.class);


		//job.setReducerClass(Reduce.class);
		final String NAME_NODE = "hdfs://sandbox.hortonworks.com:8020";
		job.addCacheFile(new URI(NAME_NODE
		+ "/user/hue/users/users.dat"));

		// OPTIONAL :: uncomment the following line to add the Combiner
		// job.setCombinerClass(Reduce.class);

		MultipleInputs.addInputPath(job, new Path(otherArgs[0]), TextInputFormat.class ,
				Map1.class );

		//MultipleInputs.addInputPath(job, new Path(otherArgs[1]),TextInputFormat.class,Map2.class );

		job.setOutputKeyClass(Text.class);
		// set output value type
		job.setOutputValueClass(Text.class);

		//set the HDFS path of the input data
		// set the HDFS path for the output 
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		job.waitForCompletion(true);
	}
}
