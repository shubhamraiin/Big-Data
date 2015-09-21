import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class listMovie {

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		private Text title = new Text();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] read = value.toString().split("::");
			String genre = read[2];
			Configuration conf = context.getConfiguration();
			String genreInput = conf.get("genrecmd");
			if (genre.contains(genreInput)) {
				title.set(read[1]);
				context.write(title, new Text(""));
			}

		}

	}

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] cmdArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		if (cmdArgs.length != 3) {
			System.err.println("Usage: listMovie <input> <output> <Fantasy>");
			System.exit(2);
		}
		conf.set("genrecmd", cmdArgs[2]);

		Job job = new Job(conf, "listMovie");
		job.setJarByClass(listMovie.class);
		job.setMapperClass(Map.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(0);

		FileInputFormat.addInputPath(job, new Path(cmdArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(cmdArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
