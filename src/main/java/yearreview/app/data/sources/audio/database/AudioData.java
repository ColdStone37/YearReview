package yearreview.app.data.sources.audio.database;

import java.net.URL;

public class AudioData implements Comparable<AudioData> {
	public final String id;
	public final String name;
	public final Type type;
	private URL coverUrl;

	public AudioData(String id, String name, Type type) {
		this.id = id;
		this.name = name;
		this.type = type;
		coverUrl = null;
	}

	public AudioData(String id, String name, Type type, URL coverUrl) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.coverUrl = coverUrl;
	}

	@Override
	public int compareTo(AudioData other) {
		return id.compareTo(other.id);
	}

	public void addCover(URL coverUrl) {
		this.coverUrl = coverUrl;
	}

	public URL getCover() {
		return coverUrl;
	}

	public enum Type {
		Song,
		Audiobook,
		Episode,
		Podcast,
		Album,
		Artist,
		Author;

		public boolean isPiece() {
			switch (this) {
				case Song:
				case Audiobook:
				case Episode:
					return true;
				default:
					return false;
			}
		}
	}
}