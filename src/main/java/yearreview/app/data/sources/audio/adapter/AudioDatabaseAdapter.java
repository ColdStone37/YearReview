package yearreview.app.data.sources.audio.adapter;

import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;

/**
 * Abstract class that defines how an adapter to an {@link AudioDatabase} should work.
 *
 * @author ColdStone37
 */
public abstract class AudioDatabaseAdapter {
    /**
     * Stores the database to insert the data into.
     */
    protected final AudioDatabase database;

    /**
     * Constructs an adapter. A database must always be passed since it's needed to insert the data.
     * @param database database to insert the data into
     */
    public AudioDatabaseAdapter(AudioDatabase database){
        this.database = database;
    }

    /**
     * Creates a new Adapter by its name.
     * @param database database to insert the data of the Adapter into
     * @param config configuration for the Adapter
     * @return the constructed Adapter
     */
    public static AudioDatabaseAdapter getAdapter(AudioDatabase database, XmlNode config) {
        switch(config.getName()){
            case "Spotify":
                return new SpotifyAdapter(database, config);
        }
        return null;
    }

    /**
     * Loads the data of the Adapter and inserts it into the database.
     * @param start start time of data to load
     * @param end end time of data to load
     * @throws IOException if the all or some of the files cannot be read
     */
    public abstract void loadData(Instant start, Instant end) throws IOException;
}