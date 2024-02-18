package yearreview.app.data.sources.audio.database;

import java.util.ArrayList;
import java.util.List;

public class AudioPiece extends AudioData {
	private final List<AudioData> data;

	public AudioPiece(String name, Type type, List<AudioData> data) {
		super(name, type);
		this.data = data;
	}

	public final List<AudioData> filterData(Type type) {
		List<AudioData> filtered = new ArrayList<AudioData>();
		for (AudioData a : data)
			if (a.type == type)
				filtered.add(a);
		return filtered;
	}
}