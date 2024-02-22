package yearreview.app.util.value;

import io.jenetics.jpx.Length;
import yearreview.app.config.GlobalSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Value} that represents a Distance, internally using the {@link Length}-Datatype.
 *
 * @author ColdStone37
 */
public class DistanceValue extends Value {
	/**
	 * Precision to use in the {@link DistanceValue#toString}-function.
	 */
	private final static int DISTANCE_PRECISION = 2;
	/**
	 * Internal representation of the Distance.
	 */
	private final Length distance;

	/**
	 * Mapping from all units to their abbreviations.
	 */
	Map<Length.Unit, String> lengthUnitShort = new HashMap<Length.Unit, String>() {{
		put(Length.Unit.METER, "m");
		put(Length.Unit.KILOMETER, "km");
		put(Length.Unit.INCH, "in");
		put(Length.Unit.FOOT, "ft");
		put(Length.Unit.YARD, "yd");
		put(Length.Unit.MILE, "mi");
		put(Length.Unit.FATHOM, "fm");
		put(Length.Unit.CABLE, "cbl");
		put(Length.Unit.NAUTICAL_MILE, "nmi");
	}};

	/**
	 * Constructs a DistanceValue from a {@link Length}.
	 * @param distance distance to initialize the DistanceValue with
	 */
	public DistanceValue(Length distance) {
		this.distance = distance;
	}

	/**
	 * Constructs a DistanceValue from a distance in a certain unit.
	 * @param distance distance to initialize the DistanceValue with
	 * @param unit unit of the value in the distance argument
	 */
	public DistanceValue(double distance, Length.Unit unit) {
		this.distance = Length.of(distance, unit);
	}

	/**
	 * Gets the Type of this Value.
	 * @return {@link ValueType#DISTANCE}
	 */
	@Override
	public ValueType getType() {
		return ValueType.DISTANCE;
	}

	/**
	 * Formats the Distance to a String.
	 * @return formatted distance (e.g. "42.34 m")
	 */
	@Override
	public String toString() {
		return String.format("%." + DISTANCE_PRECISION + "f", distance.to(GlobalSettings.getLengthUnit())) + " " + lengthUnitShort.get(GlobalSettings.getLengthUnit());
	}

	/**
	 * Formats the Distance to a String in a certain unit.
	 * @param unit unit to format the distance to
	 * @return formatted distance (e.g. 123.45 ft)
	 */
	public String toString(Length.Unit unit) {
		return String.format("%." + DISTANCE_PRECISION + "f", distance.to(unit)) + " " + lengthUnitShort.get(unit);
	}

	/**
	 * Gets a DistanceValue that has to sum of this object and the other-object.
	 * @param other Value to add
	 * @return DistanceValue or null if the other Value is not of the same type
	 */
	@Override
	public Value plus(Value other) {
		if(other instanceof DistanceValue) {
			DistanceValue otherDistance = (DistanceValue) other;
			return new DistanceValue(((Length)(otherDistance.getValue())).doubleValue() + distance.doubleValue(), Length.Unit.METER);
		}
		return null;
	}

	/**
	 * Gets the internally used Value to measure the distance.
	 * @return {@link Length}-object
	 */
	@Override
	protected Object getValue() {
		return distance;
	}

	/**
	 * Compares to another Value-object.
	 * @param other Value-object to compare against
	 * @return comparison of distances or {@link Integer#MAX_VALUE} if the other Value isn't a DistanceValue
	 */
	@Override
	public int compareTo(Value other) {
		if(other instanceof DistanceValue)
			return distance.compareTo(((DistanceValue) other).distance);
		return Integer.MAX_VALUE;
	}
}
