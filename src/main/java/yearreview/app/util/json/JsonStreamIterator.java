package yearreview.app.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public class JsonStreamIterator implements Iterator<JsonObject> {
    private final BufferedReader inputJson;
    private boolean finished = false;
    protected JsonStreamIterator(BufferedReader inputJson) throws IOException {
        this.inputJson = inputJson;
        inputJson.read();
        char c = (char)inputJson.read();
        if(c == ']')
            finished = true;
    }

    @Override
    public boolean hasNext() {
        return !finished;
    }

    @Override
    public JsonObject next() {
        try{
            JsonObject jsonObject = new JsonObject(inputJson);
            char c = (char)inputJson.read();
            if(c == ']')
                finished = true;
            inputJson.read();
            return jsonObject;
        } catch(IOException e){
            return null;
        }
    }
}
