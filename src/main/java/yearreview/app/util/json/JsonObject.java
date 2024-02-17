package yearreview.app.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    private final Map<String, String> stringValues;
    private final Map<String, JsonObject> objectValues;
    private final Map<String, JsonArray> arrayValues;
    protected JsonObject(BufferedReader inputJson) throws IOException {
        boolean inString = false;
        char c;
        int c_int;
        while((c_int = inputJson.read()) != -1) {
            c = (char) c_int;


        }
    }
}
