package yearreview.app.data.sources.audio.database;

import java.time.Duration;
import java.time.Instant;

/**
 * An Event that represents a {@link AudioPiece piece} that was listened to at a certain time.
 *
 * @author ColdStone37
 */
public class ListeningEvent implements Comparable<ListeningEvent> {
	/**
	 * Piece that was listened to.
	 */
	public final AudioPiece listenedTo;
	/**
	 * Time the piece was listened to.
	 */
	public final Instant time;
	/**
	 * Duration of listening.
	 */
	public final Duration duration;

	/**
	 * Constructs a new ListeningEvent.
	 * @param listenedTo piece that was listened to
	 * @param time time at which it was listened
	 * @param duration duration of listening
	 */
	public ListeningEvent(AudioPiece listenedTo, Instant time, Duration duration) {
		this.listenedTo = listenedTo;
		this.time = time;
		this.duration = duration;
	}

	/**
	 * Compares two ListeningEvents by their {@link ListeningEvent#time}. Needed to implement the {@link Comparable}-interface.
	 * @param other event to compare against
	 * @return negative value if this event is before the other event. Zero if the events were at the same time. Positive value otherwise
	 */
	@Override
	public int compareTo(ListeningEvent other) {
		return time.compareTo(other.time);
	}
}