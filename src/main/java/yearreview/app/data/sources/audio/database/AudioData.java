package yearreview.app.data.sources.audio.database;

import yearreview.app.data.processor.toplist.TopListCompatibleItem;

import java.net.URL;

public class AudioData implements Comparable<AudioData>, TopListCompatibleItem {
	public final String name;
	public final Type type;
	private URL coverUrl;

	public AudioData(String name, Type type) {
		this.name = name;
		this.type = type;
		coverUrl = null;
	}

	public AudioData(String name, Type type, URL coverUrl) {
		this.name = name;
		this.type = type;
		this.coverUrl = coverUrl;
	}

	@Override
	public int compareTo(AudioData other) {
		return name.compareTo(other.name);
	}

	public void addCover(URL coverUrl) {
		this.coverUrl = coverUrl;
	}

	@Override
	public String getMainText() {
		return name;
	}

	@Override
	public String getSubText() {
		return null;
	}

	@Override
	public boolean hasSubText() {
		return false;
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
		Artist;

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