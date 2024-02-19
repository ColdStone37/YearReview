package yearreview.app.data.sources.audio;

import yearreview.app.data.sources.DataSource;
import yearreview.app.data.sources.audio.adapter.AudioDatabaseAdapter;
import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AudioDataSource extends DataSource {
    private List<AudioDatabaseAdapter> adapters;
    private AudioDatabase database;
    public AudioDataSource(XmlNode c) {
        super(c);
    }
    public void parseConfig(XmlNode config) {
        adapters = new ArrayList<AudioDatabaseAdapter>();
        database = new AudioDatabase();
        for(XmlNode adapter : config)
            adapters.add(AudioDatabaseAdapter.getAdapter(database, adapter));
    }

    public void loadData(Instant start, Instant end) throws IOException {
        for(AudioDatabaseAdapter adapter : adapters)
            adapter.loadData(start, end);
    }
}