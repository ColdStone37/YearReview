package yearreview.app.util.json;

import java.util.Iterator;
import java.util.function.Consumer;

public class JsonIterator implements Iterator<JsonObject> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public JsonObject next() {
        return null;
    }
}
