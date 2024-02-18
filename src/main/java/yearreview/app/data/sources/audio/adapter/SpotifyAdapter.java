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

public class SpotifyAdapter extends SongDataAdapter {
    private final String dataPath;

    public SpotifyAdapter(AudioDatabase database, XmlNode config) {
        super(database);
        dataPath = config.getChildContent("Path");
    }

    public void loadData(Instant start, Instant end) throws IOException {
        File folder = GlobalSettings.getRelativePath(dataPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null)
            throw new IOException("Passed directory doesn't exist");
        for (File f : listOfFiles) {
            if (f.isFile()) {
                System.out.println("Started parsing " + f);
                JsonParser parser = Json.createParser(Files.newInputStream(f.toPath()));
                parser.next();
                parser.getArrayStream().filter(e->isBetween(Instant.parse(e.asJsonObject().getString("ts")), start, end)).forEach(value -> parseJsonObject(value.asJsonObject()));
            }
        }
        System.out.println(database.getFilteredData(AudioData.Type.Song).size());
    }

    private void parseJsonObject(JsonObject object) {
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

    private boolean isBetween(Instant val, Instant start, Instant end) {
        return val.isAfter(start) && val.isBefore(end);
    }
}
