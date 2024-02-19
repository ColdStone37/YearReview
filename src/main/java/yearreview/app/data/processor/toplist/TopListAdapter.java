package yearreview.app.data.processor.toplist;

import java.time.Instant;
import java.util.Collection;

public interface TopListAdapter {
	public Collection<TopListElement> getElements(Instant t);
}
