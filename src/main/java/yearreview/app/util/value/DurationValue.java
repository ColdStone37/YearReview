package yearreview.app.util.value;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Value} that represents a Duration, internally using the {@link Duration}-Datatype.
 *
 * @author ColdStone37
 */
public class DurationValue extends Value {
	/**
	 * Parts of the Duration to output in the {@link DurationValue#toString()}-function.
	 */
	private final static int DURATION_SHOW_PARTS = 2;
	/**
	 * Amount of values in each Interval (e.g. 1000ms = 1s; 60s = 1min; ...).
	 */
	private final static long[] INTERVAL_SIZES = {1000, 60, 60, 24, 365, Integer.MAX_VALUE};
	/**
	 * Maximum amount of spaces needed for Values in each interval (equivalent to floor(log10(INTERVAL_SIZES))).
	 */
	private final static int [] INTERVAL_VALUE_LENGTHS = {3, 2, 2, 2, 3, 1};
	/**
	 * Abbreviations of the intervals.
	 */
	private final static String[] INTERVAL_NAMES = {"ms", "s", "m", "h", "d", "y"};
	/**
	 * Duration stored in this object.
	 */
	private final Duration duration;

	/**
	 * Constructs a new DurationValue from a Duration
	 * @param d duration
	 */
	public DurationValue(Duration d) {
		duration = d;
	}

	/**
	 * Gets the Type of this Value.
	 * @return {@link ValueType#DURATION}
	 */
	@Override
	public ValueType getType() {
		return ValueType.DURATION;
	}

	/**
	 * Formats the Duration to a String.
	 * @return formatted duration (e.g. "2d 19h")
	 */
	@Override
	public String toString() {
		long currentVal = duration.toMillis();

		// To avoid later problems
		if(currentVal == 0)
			return "0ms";

		StringBuilder durationString = new StringBuilder();

		// Add minus sign if needed and make value positive
		if(currentVal < 0) {
			durationString.append("-");
			currentVal *= -1;
		}

		// Get the values of all units bigger than 0
		List<Long> unitValues = new ArrayList<>(6);
		for(int i = 0; i < INTERVAL_SIZES.length && currentVal > 0; i++) {
			unitValues.add((currentVal % INTERVAL_SIZES[i]));
			currentVal /= INTERVAL_SIZES[i];
		}

		// Add the values to the StringBuilder with their abbreviations
		for(int i = unitValues.size()-1; i >= Math.max(0, unitValues.size() - DURATION_SHOW_PARTS); i--)
			durationString.append(String.format("%0"+(i == unitValues.size()-1 ? 1 : INTERVAL_VALUE_LENGTHS[i])+"d", unitValues.get(i)))
					.append(INTERVAL_NAMES[i]).append(" ");

		// Delete the last space
		durationString.deleteCharAt(durationString.length()-1);

		// return string
		return durationString.toString();
	}

	/**
	 * Gets a DurationValue that has to sum of this object and the other-object.
	 * @param other Value to add
	 * @return DurationValue or null if the other Value is not of the same type
	 */
	@Override
	public Value plus(Value other) {
		if(other instanceof DurationValue) {
			Duration otherDuration = (Duration) other.getValue();
			return new DurationValue(duration.plus(otherDuration));
		}
		return null;
	}

	/**
	 * Gets the internally used Value to measure the duration.
	 * @return {@link Duration}-object
	 */
	@Override
	protected Object getValue() {
		return duration;
	}

	/**
	 * Compares to another Value-object.
	 * @param other Value-object to compare against
	 * @return comparison of durations or {@link Integer#MAX_VALUE} if the other Value isn't a DurationValue
	 */
	@Override
	public int compareTo(Value other) {
		if(other instanceof DurationValue)
			return duration.compareTo((Duration) other.getValue());
		return Integer.MAX_VALUE;
	}
}
