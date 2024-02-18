package yearreview.app.data.processor.toplist;

import java.time.Instant;

public interface TopListEvent {
	public Instant getEventTime();
	public TopListCompatibleItem getItem();
	public Number getValue();
}
