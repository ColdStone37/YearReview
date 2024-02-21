package yearreview.app.data.processor.toplist;

import yearreview.app.util.value.Value;

/**
 * A TopListElement contains a {@link TopListCompatibleItem} and it's associated {@link Value}.
 * The {@link Value} can be updated using the {@link TopListElement#addValue}-function to make implementing the {@link TopListAdapter adapters} easier.
 *
 * @author ColdStone37
 */
public class TopListElement implements Comparable<TopListElement> {
	/**
	 * Counts the TopListElements already in existence.
	 */
	private static int count = 0;
	/**
	 * Identifier of the element. Unique over all TopListElements.
	 */
	private final int id;
	/**
	 * Item to display information from.
	 */
	private final TopListCompatibleItem item;
	/**
	 * Value of the element, used for sorting and displaying.
	 */
	private Value value;

	/**
	 * Constructs a TopListElement from an item and a Value.
	 * @param item item to get information from
	 * @param value Value to associate to the item
	 */
	public TopListElement(TopListCompatibleItem item, Value value) {
		id = count;
		count++;
		this.item = item;
		this.value = value;
	}

	/**
	 * Adds a given Value to the internal Value of the element.
	 * @param value Value to add to internal Value
	 */
	public void addValue(Value value) {
		this.value = this.value.plus(value);
	}

	/**
	 * Gets the Value associated to the item.
	 * @return associated Value
	 */
	public Value getValue() {
		return value;
	}

	/**
	 * Gets the item of the element.
	 * @return Item used to get information from
	 */
	public TopListCompatibleItem getItem() {
		return item;
	}

	/**
	 * Needed to implement the {@link Comparable}-interface. Compares elements by value at first. If both items have the same value they are compared by their {@link TopListCompatibleItem#getMainText mainText}.
	 * @param other TopListElement to compare against
	 * @return negative value if <code>this &lt; other</code>, positive value if <code>this &gt; other</code>
	 */
	@Override
	public int compareTo(TopListElement other) {
		// First compare by Value
		int valCompare = value.compareTo(other.getValue());
		if(valCompare != 0)
			return valCompare;

		// If the values are the same compare by MainText
		return item.getMainText().compareTo(other.getItem().getMainText());
	}
}