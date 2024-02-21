package yearreview.app.data.sources.audio.database;

import yearreview.app.data.processor.toplist.TopListCompatibleItem;
import yearreview.app.util.image.ImageReference;

/**
 * Stores data about a {@link AudioPiece Piece} of audio (e.g. artists or albums).
 * An {@link AudioPiece} extends this class and adds a List of AudioData that stores Data associated to the Piece.
 *
 * @author ColdStone37
 */
public class AudioData implements Comparable<AudioData>, TopListCompatibleItem {
	/**
	 * Might store an artist-, album- or podcast-name.
	 */
	public final String name;
	/**
	 * Type of data stored in this Object.
	 */
	public final Type type;
	/**
	 * Image to display alongside this data if available.
	 */
	private ImageReference cover;

	/**
	 * Constructs an AudioData-object with name and type.
	 * @param name data to store
	 * @param type type of data stored in this object
	 */
	public AudioData(String name, Type type) {
		this.name = name;
		this.type = type;
		cover = null;
	}

	/**
	 * Constructs an AudioData-object with name, type and image.
	 * @param name data to store
	 * @param type type of data stored in this object
	 * @param cover cover to display alongside the name
	 */
	public AudioData(String name, Type type, ImageReference cover) {
		this.name = name;
		this.type = type;
		this.cover = cover;
	}

	/**
	 * Compares AudioData-objects alphabetically by name.
	 * @param other AudioData-object to compare against
	 * @return result of alphabetical comparison
	 */
	@Override
	public int compareTo(AudioData other) {
		return name.compareTo(other.name);
	}

	/**
	 * Adds a cover to an AudioData-object after initialization.
	 * @param cover cover to add ti the data
	 */
	public void addCover(ImageReference cover) {
		this.cover = cover;
	}

	/**
	 * Gets the main text.
	 * @return main text
	 */
	@Override
	public String getMainText() {
		return name;
	}

	/**
	 * Since no sub text is available null is returned.
	 * @return null
	 */
	@Override
	public String getSubText() {
		return null;
	}

	/**
	 * Always returns false since AudioData does not provide a sub text.
	 * @return false
	 */
	@Override
	public boolean hasSubText() {
		return false;
	}

	/**
	 * Gets the image associated the data. Might be null.
	 * @return associated image
	 */
	public ImageReference getImage() {
		return cover;
	}

	/**
	 * An enum containing the different types of AudioData available.
	 */
	public enum Type {
		SONG,
		AUDIOBOOK,
		EPISODE,
		PODCAST,
		ALBUM,
		ARTIST;

		/**
		 * Gets whether a Type represents a Piece (meaning that it should be represented by an {@link AudioPiece}-instance)
		 * @return true if Type is Song, Audiobook or Episode
		 */
		public boolean isPiece() {
			switch (this) {
				case SONG:
				case AUDIOBOOK:
				case EPISODE:
					return true;
				default:
					return false;
			}
		}
	}
}