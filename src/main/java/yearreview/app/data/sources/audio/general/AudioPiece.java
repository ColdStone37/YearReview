package yearreview.app.data.sources.audio.general;

import java.time.Instant;
import java.util.TreeSet;

public abstract class AudioPiece implements Comparable<AudioPiece> {
	public final String id;
	public final TreeSet<ListeningEvent> events;

	public AudioPiece(String id) {
		this.id = id;
		events = new TreeSet<ListeningEvent>();
	}

	public void addEvent(ListeningEvent e) {
		events.add(e);
	}

	public static class ListeningEvent implements Comparable<ListeningEvent> {
		public final Instant time;
		public final int duration;

		public ListeningEvent(Instant time, int duration) {
			this.time = time;
			this.duration = duration;
		}

		@Override
		public int compareTo(ListeningEvent other) {
			return time.compareTo(other.time);
		}
	}

	@Override
	public int compareTo(AudioPiece other) {
		return id.compareTo(other.id);
	}
}