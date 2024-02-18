package yearreview.app.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.LinkPermission;
import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    private final Map<String, String> stringValues;
    private final Map<String, JsonObject> objectValues;
    private final Map<String, JsonArray> arrayValues;
	Map<Character, Character> backslashMap  = new HashMap<Character, Character>() {{
		put('\'', '\'');
		put('\\', '\\');
		put('n', '\n');
		put('r', '\r');
		put('t', '\t');
		put('b', '\b');
		put('f', '\f');
	}};

    protected JsonObject(BufferedReader inputJson) throws IOException {
        stringValues = new HashMap<String, String>();
        objectValues = new HashMap<String, JsonObject>();
        arrayValues = new HashMap<String, JsonArray>();

        boolean inString = false;

        int c_int = 0;
        while(c_int != -1 && c_int != (int)'}') {
            inputJson.read();
			String key = readString(inputJson);
			inputJson.read();
			c_int = inputJson.read();
			switch((char) c_int){
				case '{':
					objectValues.put(key, new JsonObject(inputJson));
					c_int = inputJson.read();
					break;
				case '"':
					stringValues.put(key, readString(inputJson));
					c_int = inputJson.read();
					break;
				case '[':
					arrayValues.put(key, new JsonArray(inputJson));
					c_int = inputJson.read();
					break;
				default:
					String read = ((char) c_int) + readUntilBreakCharacter(inputJson);
					c_int = read.charAt(read.length()-1);
					stringValues.put(key, read.substring(0, read.length()-1));
					break;
			}
        }
    }

	private String readString(BufferedReader reader) throws IOException {
		int c_int;
		char c;
		boolean lastBackslash = false;
		StringBuilder stringBuilder = new StringBuilder();
		while((c_int = reader.read()) != -1) {
			c = (char) c_int;

            if(lastBackslash){
				stringBuilder.append(backslashMap.get(c));
				lastBackslash = false;
            } else {
                switch(c) {
                    case '"':
                        return stringBuilder.toString();
                    case '\\':
                        lastBackslash = true;
                        break;
                    default:
						stringBuilder.append(c);
                        break;
                }
            }
		}
		return stringBuilder.toString();
	}

	private String readUntilBreakCharacter(BufferedReader reader) throws IOException {
		int c_int;
		char c;
		boolean lastBackslash = false;
		StringBuilder stringBuilder = new StringBuilder();
		while((c_int = reader.read()) != -1) {
			c = (char) c_int;

			stringBuilder.append(c);
			if(c == ',' || c == '}')
				return stringBuilder.toString();
		}
		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('{');
		for (Map.Entry<String, String> entry : stringValues.entrySet())
			stringBuilder.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",");
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
