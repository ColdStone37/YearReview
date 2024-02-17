package yearreview.app.data.sources.audio.adapter;

import yearreview.app.data.sources.audio.database.AudioDatabase;
import yearreview.app.util.xml.XmlNode;

public class SpotifyAdapter extends SongDataAdapter {
    public SpotifyAdapter(AudioDatabase database, XmlNode config) {
        super(database);
    }


}
