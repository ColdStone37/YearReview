package yearreview.app.util.json;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;

public class JsonParser implements Iterable<JsonObject> {
    private final File toParse;

    public JsonParser(File f) throws FileNotFoundException {
        toParse = f;

    }

    public JsonParser(String filename) throws FileNotFoundException {
        this(new File(filename));
    }

    public void parse() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(toParse.toPath()), StandardCharsets.UTF_8));
    }

    public JsonStreamIterator getStreamIterator() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(toParse.toPath()), StandardCharsets.UTF_8));
        return new JsonStreamIterator(reader);
    }

    @Override
    public Iterator<JsonObject> iterator() {
        try {
            return getStreamIterator();
        } catch(IOException e){
            System.out.println("Unable to create iterator from stream");
            return null;
        }
    }
}
