package yearreview.app.data.sources.audio.adapter;

import yearreview.app.config.GlobalSettings;
import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.util.json.JsonObject;
import yearreview.app.util.json.JsonParser;
import yearreview.app.util.json.JsonStreamIterator;
import yearreview.app.util.xml.XmlNode;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class SpotifyAdapter extends SongDataAdapter {
    private String dataPath;
    public SpotifyAdapter(AudioDatabase database, XmlNode config) {
        super(database);
        dataPath = config.getChildContent("Path");
    }

    public void loadData(Instant start, Instant end) {
        File folder = GlobalSettings.getRelativePath(dataPath);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles) {
            if (f.isFile()) {
                System.out.println("Started parsing " + f);
                try {
                    JsonParser parser = new JsonParser(f);
                    int i = 0;
                    for(JsonObject object : parser){
                        System.out.println(object);
                    }
                } catch (IOException e) {

                }
            }
        }
    }
}
