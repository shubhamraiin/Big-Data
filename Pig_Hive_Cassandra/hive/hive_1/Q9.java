import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class HiveFormatGenre extends UDF {
	public Text evaluate(Text input) {
		Text to_value = new Text("");
		if (input != null) {
			try {
				String inputString = input.toString();
				String[] tokens = inputString.split("\\|");
				if (tokens.length == 1) {
					return new Text("1) " + tokens[0] + " <pxp140730> ");
				}
				if (tokens.length == 2) {
					return new Text("1)" + tokens[0] + " & 2)" + tokens[1]
							+ " <pxp140730> ");
				} else if (tokens.length == 3) {
					return new Text("1)" + tokens[0] + " , 2)" + tokens[1]
							+ " & 3)" + tokens[2] + " <pxp140730>  ");
				}
			} catch (Exception e) { // Should never happen
				to_value = new Text(input);
			}
		}
		return to_value;
	}
}