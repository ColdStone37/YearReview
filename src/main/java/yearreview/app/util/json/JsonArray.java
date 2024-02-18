package yearreview.app.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonArray {
	List<JsonObject> array;
	protected JsonArray(BufferedReader inputJson) throws IOException {
		array = new ArrayList<JsonObject>();

		char c = ' ';

		inputJson.read();
		while(c != ']') {
			array.add(new JsonObject(inputJson));

			c = (char) inputJson.read();
		}
	}
}
