package yearreview.app.data.sources.audio.database;

import java.util.ArrayList;
import java.util.List;

/**
 * An AudioPiece extends {@link AudioData} by adding a {@link List} of {@link AudioData data} associated to the piece and a subtext.
 *
 * @author ColdStone37
 */
public class AudioPiece extends AudioData {
	/**
	 * List of data associated to the piece.
	 */
	private final List<AudioData> data;

	/**
	 * Sub text displayed alongside the name of the piece (mostly names of authors).
	 */
	private final String subtext;

	/**
	 * Constructs and AudioPiece from name, type and associated data.
	 * @param name name of the piece
	 * @param type type of the piece ({@link Type#isPiece()} must be true)
	 * @param data data to store alongside the piece
	 */
	public AudioPiece(String name, Type type, List<AudioData> data) {
		super(name, type);
		assert(type.isPiece());
		this.data = data;

		// Choose a different sub text for different types of pieces
		switch(type) {
			case SONG:
			case AUDIOBOOK:
				// For songs and audiobooks construct a comma seperated list of authors
				StringBuilder artistString = new StringBuilder();
				for(AudioData d : data)
					if(d.type == Type.ARTIST)
						artistString.append(d.getMainText()).append(", ");
				subtext = artistString.length() >= 2 ? artistString.delete(artistString.length()-2, artistString.length()).toString() : null;
				return;
			case EPISODE:
				// For podcasts add the podcast-name to the sub text
				for(AudioData d : data)
					if(d.type == Type.PODCAST){
						subtext = d.getMainText();
						return;
					}
			default:
				subtext = null;
		}
	}

	/**
	 * Get the sub text of the piece.
	 * @return sub text
	 */
	@Override
	public String getSubText() {
		return subtext;
	}

	/**
	 * Gets whether the piece has a sub text. Pieces might not have a subtext if it could not be created from the associated data.
	 * @return true if the piece has a sub text, otherwise null
	 */
	@Override
	public boolean hasSubText() {
		return subtext != null;
	}

	/**
	 * Filters the {@link AudioData data} associated with the piece by a given type.
	 * @param type type to filter the data by
	 * @return List of data filtered by the given type
	 */
	public final List<AudioData> filterData(Type type) {
		List<AudioData> filtered = new ArrayList<AudioData>();
		for (AudioData a : data)
			if (a.type == type)
				filtered.add(a);
		return filtered;
	}
}