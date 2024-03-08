package yearreview.app.data.sources.audio;

import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.processor.toplist.TopListCompatible;
import yearreview.app.data.sources.DataSource;
import yearreview.app.data.sources.audio.adapter.AudioDatabaseAdapter;
import yearreview.app.data.sources.audio.database.AudioData;
import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.data.sources.audio.database.AudioTopListAdapter;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DataSource} that uses {@link AudioDatabaseAdapter adapters} to load the listening-history from files exported from services like Spotify.
 *
 * @author ColdStone37
 */
public class AudioDataSource extends DataSource implements TopListCompatible {
    /**
     * Adapters used for loading the listening histories.
     */
    private List<AudioDatabaseAdapter> adapters;
    /**
     * Database to store the data in.
     */
    private AudioDatabase database;

    /**
     * Constructs a new AudioDataSource from a given configuration.
     * @param c configuration to use
     */
    public AudioDataSource(XmlNode c) {
        super(c);
    }

    /**
     * Parses the passed configuration, initializes the database and creates the adapters.
     * @param config configuration to parse
     */
    public void parseConfig(XmlNode config) {
        adapters = new ArrayList<AudioDatabaseAdapter>();
        database = new AudioDatabase();
        for(XmlNode adapter : config)
            adapters.add(AudioDatabaseAdapter.getAdapter(database, adapter));
    }

    /**
     * Loads the data from the {@link AudioDatabaseAdapter adapters}.
     * @param start start of interval to load data from
     * @param end end of interval to load data from
     * @throws IOException if the data cannot be loaded
     */
    public void loadData(Instant start, Instant end) throws IOException {
        for(AudioDatabaseAdapter adapter : adapters)
            adapter.loadData(start, end);
    }

    /**
     * Gets the TopListAdapter for the {@link AudioDatabase} that can be used to create a TopList from this {@link DataSource}.
     * @param config configuration of the TopListAdapter containing which type to {@link AudioData.Type} to filter by
     * @return adapter for the TopList
     */
    @Override
    public TopListAdapter getTopListAdapter(XmlNode config) {
        return new AudioTopListAdapter(database, AudioData.Type.getTypeByName(config.getChildContent("Type")));
    }
}