package yearreview.app.data.sources.audio.database;

import java.util.ArrayList;
import java.util.List;

public class AudioPiece extends AudioData {
	private final List<AudioData> data;

	private final String subtext;

	public AudioPiece(String name, Type type, List<AudioData> data) {
		super(name, type);
		this.data = data;
		switch(type) {
			case Song:
			case Audiobook:
				StringBuilder artistString = new StringBuilder();
				for(AudioData d : data)
					if(d.type == Type.Artist)
						artistString.append(d.getMainText()).append(", ");
				subtext = artistString.delete(artistString.length()-2, artistString.length()).toString();
				return;
			case Episode:
				for(AudioData d : data)
					if(d.type == Type.Podcast){
						subtext = d.getMainText();
						return;
					}
			default:
				subtext = null;
		}
	}

	@Override
	public String getSubText() {
		return subtext;
	}

	@Override
	public boolean hasSubText() {
		return true;
	}

	public final List<AudioData> filterData(Type type) {
		List<AudioData> filtered = new ArrayList<AudioData>();
		for (AudioData a : data)
			if (a.type == type)
				filtered.add(a);
		return filtered;
	}
}