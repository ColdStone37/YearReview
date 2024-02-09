package yearreview.app.data.sources.audio.general;

import java.util.TreeMap;

public class AudioDatabase {
	private TreeMap<String, AudioPiece> pieces;

	public AudioDatabase() {
		pieces = new TreeMap<String, AudioPiece>();
	}

	public AudioPiece getPieceById(String id) {
		return pieces.get(id);
	}

	public void insertAudioPiece(AudioPiece piece) {
		//System.out.println(piece.id);
		pieces.put(piece.id, piece);
		System.out.println(pieces.size());
	}
}