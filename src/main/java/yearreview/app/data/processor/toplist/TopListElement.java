package yearreview.app.data.processor.toplist;

import yearreview.app.util.value.Value;
import yearreview.app.util.value.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A TopListElement contains a {@link TopListCompatibleItem} and it's associated {@link Value Values}.
 * The {@link Value Values} can be updated using the {@link TopListElement#addValue}-function to make implementing the {@link TopListAdapter adapters} easier.
 *
 * @author ColdStone37
 */
public class TopListElement {
	/**
	 * Counts the TopListElements already in existence.
	 */
	private static int count = 0;
	/**
	 * Identifier of the element. Unique over all TopListElements.
	 */
	public final int id;
	/**
	 * Item to display information from.
	 */
	private final TopListCompatibleItem item;
	/**
	 * Values of the element, used for sorting and displaying.
	 */
	private final Map<ValueType, Value> values;

	/**
	 * Constructs a TopListElement from an item and a Value.
	 * @param item item to get information from
	 * @param value Value to associate to the item
	 */
	public TopListElement(TopListCompatibleItem item, Value value) {
		id = count;
		count++;
		this.item = item;
		values = new HashMap<>();
		values.put(value.getType(), value);
	}

	/**
	 * Constructs a TopListElement from an item and a list of Values.
	 * @param item item to get information from
	 * @param values Values to associate to the item
	 */
	public TopListElement(TopListCompatibleItem item, List<Value> values) {
		id = count;
		count++;
		this.item = item;
		this.values = new HashMap<>();
		for(Value v:values)
			this.values.put(v.getType(), v);
	}

	/**
	 * Adds a given Value to the internal Value of that type.
	 * @param value Value to add to internal Value
	 */
	public TopListElement addValue(Value value) {
		values.compute(value.getType(), (k, v) -> (v == null) ? null : v.plus(value));
		return this;
	}

	/**
	 * Adds the passed Values to the internal Values matching the same type.
	 * @param addValues values to add to the internal values
	 */
	public void addValues(Value... addValues) {
		for(Value value : addValues)
			values.compute(value.getType(), (k, v) -> (v == null) ? null : v.plus(value));
	}

	/**
	 * Gets the Value associated to the item with a certain type.
	 * @param type type of Value to get from the element
	 * @return associated Value or null if no Value of that type exists
	 */
	public Value getValue(ValueType type) {
		return values.get(type);
	}

	/**
	 * Gets the item of the element.
	 * @return Item used to get information from
	 */
	public TopListCompatibleItem getItem() {
		return item;
	}
}