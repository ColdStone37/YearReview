package yearreview.app.data.sources.audio.database;

import yearreview.app.data.processor.toplist.TopListCompatibleItem;
import yearreview.app.util.image.ImageReference;

import java.net.URL;

public class AudioData implements Comparable<AudioData>, TopListCompatibleItem {
	public final String name;
	public final Type type;
	private ImageReference cover;

	public AudioData(String name, Type type) {
		this.name = name;
		this.type = type;
		cover = null;
	}

	public AudioData(String name, Type type, ImageReference cover) {
		this.name = name;
		this.type = type;
		this.cover = cover;
	}

	@Override
	public int compareTo(AudioData other) {
		return name.compareTo(other.name);
	}

	public void addCover(ImageReference coverUrl) {
		this.cover = coverUrl;
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

	public ImageReference getImage() {
		return cover;
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