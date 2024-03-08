package yearreview.app.data.processor.toplist;

import yearreview.app.data.DataManager;
import yearreview.app.data.processor.DataProcessor;
import yearreview.app.util.value.ValueType;
import yearreview.app.util.xml.XmlNode;

import java.time.Instant;
import java.util.*;

/**
 * Generates a TopList of {@link TopListElement TopListElements} by sorting them by their associated {@link yearreview.app.util.value.Value}.
 *
 * @author ColdStone37
 */
public class TopListGenerator extends DataProcessor {
	/**
	 * Adapter used to get the {@link TopListElement TopListElements} from a {@link yearreview.app.data.sources.DataSource}.
	 */
	private TopListAdapter adapter;
	/**
	 * Comparator used to sort the {@link TopListElement TopListElements}.
	 */
	private TopListElementComparator comparator;

	public TopListGenerator(XmlNode config) {
		super(config);
	}

	public void init(DataManager dm, XmlNode config) {
		XmlNode input = config.getChildByName("Input");
		adapter = ((TopListCompatible) dm.getSourceByTag(input.getAttributeByName("name"))).getTopListAdapter(input);
		comparator = new TopListElementComparator();
	}

	/**
	 * Gets a List of {@link TopListElement TopListElements} sorted by a specified Value type and with a given length.
	 * @param t time until which to generate the TopList
	 * @param length length of the TopList to generate
	 * @param sortType type of Value to sort by
	 * @return TopList of the elements
	 */
	public List<TopListElement> getTopList(Instant t, int length, ValueType sortType) {
		// Gets the elements from the adapter
		Collection<TopListElement> elements = adapter.getElements(t);

		// Sets the type of the Value to compare by
		comparator.setComparisonValueType(sortType);

		// TreeSet can be used for sorting since it is a sorted datastructure
		TreeSet<TopListElement> sorted = new TreeSet<>(comparator);

		// Add all elements to the set and remove to small values to save some performance
		for(TopListElement elem : elements) {
			sorted.add(elem);
			if(sorted.size() > length)
				sorted.remove(sorted.first());
		}

		// Created ArrayList from reversed Set
		return new ArrayList<>(sorted.descendingSet());
	}
}
