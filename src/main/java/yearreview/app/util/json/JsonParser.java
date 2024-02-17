package yearreview.app.util.json;

import java.io.File;

public class JsonParser {
    public JsonParser(File f){

    }

    public JsonParser(String filename) {
        this(new File(filename));
    }

}
