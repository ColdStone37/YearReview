package yearreview.app.data.sources.audio.adapter;

import yearreview.app.config.GlobalSettings;
import yearreview.app.data.sources.audio.database.AudioData;
import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.data.sources.audio.database.AudioPiece;
import yearreview.app.data.sources.audio.database.ListeningEvent;
import yearreview.app.util.xml.XmlNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.json.*;
import javax.json.stream.*;

/**
 * An Adapter to input the spotify extended listening into the {@link AudioDatabase}.
 *
 * @author ColdStone37
 */
public class SpotifyAdapter extends AudioDatabaseAdapter {
    /**
     * Path to the files exported from spotify.
     */
    private final String dataPath;

    /**
     * Constructs a new SpotifyAdapter with a database to use and a configuration.
     * @param database database to insert the songs
     * @param config configuration for the adapter
     */
    public SpotifyAdapter(AudioDatabase database, XmlNode config) {
        super(database);
        dataPath = config.getChildContent("Path");
    }

    /**
     * Loads the data from the specified path.
     * @param start start time of data to load
     * @param end end time of data to load
     * @throws IOException if the all or some of the files can't be read
     */
    public void loadData(Instant start, Instant end) throws IOException {
        File folder = GlobalSettings.getRelativePath(dataPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null)
            throw new IOException("Passed directory doesn't exist");

        // Parse all files in the directory
        for (File f : listOfFiles) {
            if (f.isFile()) {
                // Create new JsonParser
                JsonParser parser = Json.createParser(Files.newInputStream(f.toPath()));
                parser.next();

                // Create Stream of JsonObjects from Array, filter them by time and process them in parseJsonObject-function
                parser.getArrayStream()
                        .map(JsonValue::asJsonObject)
                        .filter(e->isBetween(Instant.parse(e.getString("ts")), start, end))
                        .forEach(this::processJsonObject);
            }
        }
        //System.out.println(database.getFilteredData(AudioData.Type.Song).size());
    }

    /**
     * Processes a JsonObject from the Stream and inserts it into the database.
     * @param object object to process
     */
    private void processJsonObject(JsonObject object) {
        Instant time = Instant.parse(object.getString("ts"));
        Duration duration = Duration.ofMillis(object.getInt("ms_played"));
        if(!object.isNull("master_metadata_track_name")) {
            // Object is a song

            // Artist
            AudioData artist = database.getData(object.getString("master_metadata_album_artist_name"), AudioData.Type.Artist);
            // Album
            AudioData album = database.getData(object.getString("master_metadata_album_album_name"), AudioData.Type.Album);

            List<AudioData> songData = new ArrayList<AudioData>(2);
            songData.add(artist);
            songData.add(album);

            // Song
            AudioPiece song = database.getPiece(object.getString("master_metadata_track_name"), songData, AudioData.Type.Song);

            // Event
            ListeningEvent event = new ListeningEvent(song, time, duration);
            database.insertEvent(event);
        } else {
            if(!object.isNull("episode_show_name")) {
                // Object is an episode of a podcast

                // Podcast
                AudioData podcast = database.getData(object.getString("episode_show_name"), AudioData.Type.Podcast);

                List<AudioData> episodeData = new ArrayList<AudioData>(2);
                episodeData.add(podcast);

                // Episode
                AudioPiece episode = database.getPiece(object.getString("episode_name"), episodeData, AudioData.Type.Episode);

                // Event
                ListeningEvent event = new ListeningEvent(episode, time, duration);
                database.insertEvent(event);
            }
        }
    }

    /**
     * Tests if a time is between to {@link Instant}
     * @param val value to test whether it lies in between
     * @param start start of interval to test
     * @param end end of interval to test
     * @return true if val is between start and end, false otherwise
     */
    private boolean isBetween(Instant val, Instant start, Instant end) {
        return val.isAfter(start) && val.isBefore(end);
    }
}
