import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;

public class FORMAT_GENRE extends EvalFunc <String> {

@Override
    public String exec(Tuple input) {
        try {
            if (input == null || input.size() == 0) {
                return null;
            }
			
			String Genre = "";
            String input_string = (String) input.get(0);
			String[] str = strng.split("\\|");
            for(int i = 0; i < str.length; i++)
			{
				if (i == str.length - 1) {
					Genre += "& ";
					Genre += i + 1 + ") " + str[i] + " ";
				} else if (i == str.length - 2) {
					Genre += i + 1 + ") " + str[i] + " ";
				} else if (i < str.length - 2) {
					Genre += i + 1 + ") " + str[i] + ", ";
				}
			}
			Genre += "scr130130";
			return Genre;
        } catch (ExecException ex) {
            System.out.println("Error: " + ex.toString());
        }

        return null;
    }
}
