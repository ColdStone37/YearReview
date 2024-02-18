package yearreview.app.data.sources.audio.database;

import yearreview.app.data.processor.toplist.TopListCompatibleItem;
import yearreview.app.data.processor.toplist.TopListEvent;

import java.time.Duration;
import java.time.Instant;

public class ListeningEvent implements Comparable<ListeningEvent>, TopListEvent {
	public final AudioPiece listenedTo;
	public final Instant time;
	public final Duration duration;

	public ListeningEvent(AudioPiece listenedTo, Instant time, Duration duration) {
		this.listenedTo = listenedTo;
		this.time = time;
		this.duration = duration;
	}

	@Override
	public int compareTo(ListeningEvent other) {
		return time.compareTo(other.time);
	}

	@Override
	public Instant getEventTime() {
		return time;
	}

	@Override
	public TopListCompatibleItem getItem() {
		return listenedTo;
	}

	@Override
	public Number getValue() {
		return null;
	}
}