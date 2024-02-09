package yearreview.app.data.sources.audio.general;

public class Song extends AudioPiece {
	private final String name;
	private Author[] authors;

	public Song(String id, String name) {
		super(id);
		this.name = name;
	}
}