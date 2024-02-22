package yearreview.app.data.processor.toplist;

import yearreview.app.util.value.ValueType;

import java.util.Comparator;

/**
 * Comparator for {@link TopListElement} which can specify a certain {@link ValueType} to compare by.
 *
 * @author ColdStone37
 */
public class TopListElementComparator implements Comparator<TopListElement> {
	/**
	 * Type of {@link yearreview.app.util.value.Value} to compare by.
	 */
	private final ValueType comparisonValue;

	/**
	 * Constructor for a TopListElementComparator with the type to compare by.
	 * @param type type of {@link yearreview.app.util.value.Value} to compare by
	 */
	public TopListElementComparator(ValueType type) {
		comparisonValue = type;
	}

	/**
	 * Function that compares to TopListElements.
	 * @param t1 first element to compare
	 * @param t2 second element to compare
	 * @return compareTo of the values of {@link ValueType} passed when initializing
	 */
	@Override
	public int compare(TopListElement t1, TopListElement t2) {
		return t1.getValue(comparisonValue).compareTo(t2.getValue(comparisonValue));
	}
}
