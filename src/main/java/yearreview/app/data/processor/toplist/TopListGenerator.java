package yearreview.app.data.processor.toplist;

import java.time.Instant;
import java.util.*;

public class TopListGenerator {
	private final TopListAdapter adapter;
	private final int topListLength;
	public TopListGenerator(TopListAdapter adapter, int topListLength) {
		this.adapter = adapter;
		this.topListLength = topListLength;
	}

	public List<TopListElement> getTopList(Instant t) {
		Collection<TopListElement> elements = adapter.getElements(t);
		TreeSet<TopListElement> sorted = new TreeSet<TopListElement>();
		for(TopListElement elem : elements) {
			sorted.add(elem);
			if(sorted.size() > topListLength)
				sorted.remove(sorted.first());
		}
		return new ArrayList<TopListElement>(sorted.descendingSet());
	}
}
