package yearreview.app.util.value;

import io.jenetics.jpx.Length;

public class CounterValue extends Value {
	private final int count;

	public CounterValue(int val) {
		count = val;
	}

	/**
	 * Gets the Type of the Value.
	 *
	 * @return type
	 */
	@Override
	public ValueType getType() {
		return ValueType.COUNTER;
	}

	/**
	 * Formats the stored Value to a String.
	 *
	 * @return formatted value
	 */
	@Override
	public String toString() {
		return count + "x";
	}

	/**
	 * Gets the sum of this Value and another Value
	 *
	 * @param other Value to add
	 * @return summed Value or null if the Values weren't of the same {@link ValueType}
	 */
	@Override
	public Value plus(Value other) {
		if(other instanceof CounterValue) {
			return new CounterValue((int)other.getValue() + count);
		}
		return null;
	}

	/**
	 * Gets the internal representation of the Value.
	 *
	 * @return internal representation
	 */
	@Override
	protected Object getValue() {
		return count;
	}

	@Override
	public int compareTo(Value other) {
		if(other instanceof CounterValue)
			return count - ((CounterValue)other).count;
		return Integer.MAX_VALUE;
	}
}
