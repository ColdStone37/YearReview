package yearreview.app.util.value;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DurationValue extends Value {
	private final static int DURATION_SHOW_PARTS = 2;
	private final static long[] INTERVAL_SIZES = {1000, 60, 60, 24, 365, Integer.MAX_VALUE};
	private final static int [] INTERVAL_VALUE_LENGTHS = {3, 2, 2, 2, 3, 1};
	private final static String[] INTERVAL_NAMES = {"ms", "s", "m", "h", "d", "y"};
	private final Duration duration;
	public DurationValue(Duration d) {
		duration = d;
	}
	@Override
	public String toString() {
		long currentVal = duration.toMillis();
		if(currentVal == 0)
			return "0ms";

		StringBuilder durationString = new StringBuilder();
		if(currentVal < 0) {
			durationString.append("-");
			currentVal *= -1;
		}

		List<Long> unitValues = new ArrayList<Long>(6);
		for(int i = 0; i < INTERVAL_SIZES.length && currentVal > 0; i++) {
			unitValues.add((currentVal % INTERVAL_SIZES[i]));
			currentVal /= INTERVAL_SIZES[i];
		}
		for(int i = unitValues.size()-1; i >= Math.max(0, unitValues.size() - DURATION_SHOW_PARTS); i--)
			durationString.append(String.format("%0"+(i == unitValues.size()-1 ? 1 : INTERVAL_VALUE_LENGTHS[i])+"d", unitValues.get(i)))
					.append(INTERVAL_NAMES[i]).append(" ");
		durationString.deleteCharAt(durationString.length()-1);
		return durationString.toString();
	}

	@Override
	public Value plus(Value other) {
		if(other instanceof DurationValue) {
			Duration otherDuration = (Duration) other.getValue();
			return new DurationValue(duration.plus(otherDuration));
		}
		return null;
	}

	@Override
	protected Object getValue() {
		return duration;
	}

	@Override
	public int compareTo(Value value) {
		if(value instanceof DurationValue)
			return duration.compareTo((Duration) value.getValue());
		return Integer.MAX_VALUE;
	}
}
