package yearreview.app.data.sources.audio.database;

import java.net.URL;
import java.util.List;

public class Song extends AudioPiece {
	public Song(String id, String name, URL coverUrl, List<AudioData> audioData) {
		super(id, name, Type.Song, coverUrl, audioData);
	}
}