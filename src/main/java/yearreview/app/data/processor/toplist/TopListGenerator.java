package yearreview.app.data.processor.toplist;

import java.time.Instant;
import java.util.*;

/**
 * Generates a TopList of {@link TopListElement TopListElements} by sorting them by their associated {@link yearreview.app.util.value.Value}.
 *
 * @author ColdStone37
 */
public class TopListGenerator {
	/**
	 * Adapter used to get the {@link TopListElement TopListElements} from a {@link yearreview.app.data.sources.DataSource}.
	 */
	private final TopListAdapter adapter;
	/**
	 * Length of the TopList to generate.
	 */
	private final int topListLength;

	/**
	 * Constructs a new TopListGenerator from an Adapter and a length.
	 * @param adapter Adapter to use for getting the data from a {@link yearreview.app.data.sources.DataSource}
	 * @param topListLength Length of the TopList to generate
	 */
	public TopListGenerator(TopListAdapter adapter, int topListLength) {
		this.adapter = adapter;
		this.topListLength = topListLength;
		assert(topListLength > 0);
	}

	/**
	 * Gets a List of {@link TopListElement TopListElements} sorted by their {@link yearreview.app.util.value.Value Values} of at most size {@link TopListGenerator#topListLength}.
	 * @param t time until which to generate the TopList
	 * @return TopList of the elements
	 */
	public List<TopListElement> getTopList(Instant t) {
		// Gets the elements from the adapter
		Collection<TopListElement> elements = adapter.getElements(t);

		// TreeSet can be used for sorting since it is a sorted datastructure
		TreeSet<TopListElement> sorted = new TreeSet<TopListElement>();

		// Add all elements to the set and remove to small values to save some performance
		for(TopListElement elem : elements) {
			sorted.add(elem);
			if(sorted.size() > topListLength)
				sorted.remove(sorted.first());
		}

		// Created ArrayList from reversed Set
		return new ArrayList<TopListElement>(sorted.descendingSet());
	}
}
