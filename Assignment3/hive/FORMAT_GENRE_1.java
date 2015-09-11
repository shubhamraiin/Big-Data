import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class FORMAT_GENRE extends UDF {
	public Text evaluate(Text s) {
		Text genformat = new Text();
		try {
			if (s == null || s.getLength() == 0) {
				return null;
			}

			String genres = "";
			String strng = s.toString();
			String[] str = strng.split("\\|");
			for (int i = 0; i < str.length; i++) {
				if (i == str.length - 1) {
					genres += "& ";
					genres += i + 1 + ") " + str[i] + " ";
				} else if (i == str.length - 2) {
					genres += i + 1 + ") " + str[i] + " ";
				} else if (i < str.length - 2) {
					genres += i + 1 + ") " + str[i] + ", ";
				}
			}
			genres += "scr130130 : HIVE";
			genformat.set(genres);
		} catch (Exception e) { // Should never happen
			genformat = new Text(s);
		}
		return genformat;
	}
}
