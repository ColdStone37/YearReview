package yearreview.app.util.value;

/**
 * A Value represents one of the types specified in the {@link ValueType ValueTypes}-enum.
 *
 * @author ColdStone37
 */
public abstract class Value implements Comparable<Value> {
	/**
	 * Gets the Type of the Value.
	 * @return type
	 */
	public abstract ValueType getType();
	/**
	 * Formats the stored Value to a String.
	 * @return formatted value
	 */
	public abstract String toString();

	/**
	 * Gets the sum of this Value and another Value
	 * @param other Value to add
	 * @return summed Value or null if the Values weren't of the same {@link ValueType}
	 */
	public abstract Value plus(Value other);

	/**
	 * Gets the internal representation of the Value.
	 * @return internal representation
	 */
	protected abstract Object getValue();
}
