package yearreview.app.data.sources.audio.adapter;

import yearreview.app.config.GlobalSettings;
import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.util.xml.XmlNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.json.*;
import javax.json.stream.*;
import javax.json.stream.JsonParser.Event;

public class SpotifyAdapter extends SongDataAdapter {
    private final String dataPath;

    private final Set<String> importantKeys = new HashSet<String>(){{
        add("ts");
        add("ms_played");
        add("master_metadata_track_name");
        add("master_metadata_album_artist_name");
        add("master_metadata_album_album_name");
        add("episode_name");
        add("episode_show_name");
    }};

    public SpotifyAdapter(AudioDatabase database, XmlNode config) {
        super(database);
        dataPath = config.getChildContent("Path");
    }

    public void loadData(Instant start, Instant end) throws ParseException {
        File folder = GlobalSettings.getRelativePath(dataPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null)
            throw new ParseException("Passed directory doesn't exist", 0);
        for (File f : listOfFiles) {
            if (f.isFile()) {
                System.out.println("Started parsing " + f);
                try {
                    JsonParser parser = Json.createParser(Files.newInputStream(f.toPath()));
                    Event event = parser.next();
                    if(event != Event.START_ARRAY)
                        throw new ParseException("Input file isn't a JSON Array", (int) parser.getLocation().getStreamOffset());
                    while(parser.hasNext()) {
                        event = parser.next();
                        switch(event) {
                            case START_OBJECT:
                                while(parser.hasNext()){
                                    event = parser.next();
                                    if(event == Event.END_OBJECT)
                                        break;
                                    String key;
                                    if(event == Event.KEY_NAME){
                                        key = parser.getString();
                                    } else {
                                        throw new ParseException("Invalid Json", (int) parser.getLocation().getStreamOffset());
                                    }
                                    event = parser.next();
                                    if(!importantKeys.contains(key))
                                        continue;
                                    String value;
                                    switch(event){
                                        case VALUE_STRING:
                                            value = parser.getString();
                                            break;
                                        case VALUE_NULL:
                                            value = null;
                                            break;
                                        case VALUE_NUMBER:
                                            value = ""+parser.getInt();
                                            break;
                                        default:
                                            throw new ParseException("Invalid Json", (int) parser.getLocation().getStreamOffset());
                                    }
                                    System.out.println(key + ":" + value);
                                }
                            case END_ARRAY:
                                break;
                            default:
                                throw new ParseException("Invalid Json", (int) parser.getLocation().getStreamOffset());
                        }
                    }
                } catch (IOException e) {

                }
            }
        }
    }
}
