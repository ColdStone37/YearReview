package yearreview.app.data.sources.audio.database;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AudioPiece extends AudioData {
	private final static String NO_AUTHOR_STRING = "No Authors";

	private final List<AudioData> data;
	private final String authors;

	public AudioPiece(String id, String name, Type type, URL coverUrl, List<AudioData> data) {
		super(id, name, type, coverUrl);
		this.data = data;
		authors = getAuthorsString();
	}

	public final List<AudioData> filterData(Type type) {
		List<AudioData> filtered = new ArrayList<AudioData>();
		for (AudioData a : data)
			if (a.type == type)
				filtered.add(a);
		return filtered;
	}

	public final String getAuthors() {
		return authors;
	}

	protected String getAuthorsString() {
		List<AudioData> authorsList = filterData(Type.Author);
		if (authorsList.isEmpty())
			return NO_AUTHOR_STRING;
		String authors = "";
		for (AudioData author : authorsList)
			authors += author.name + ", ";
		return authors.substring(0, authors.length() - 2);
	}
}