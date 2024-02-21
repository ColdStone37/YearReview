package yearreview.app.util.value;

import io.jenetics.jpx.Length;
import yearreview.app.config.GlobalSettings;

import java.util.HashMap;
import java.util.Map;

public class DistanceValue extends Value {
	private final static int DISTANCE_PRECISION = 2;
	private Length distance;

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

	public DistanceValue(Length distance) {
		this.distance = distance;
	}

	public DistanceValue(double distance) {
		this.distance = Length.of(distance, GlobalSettings.getLengthUnit());
	}

	public DistanceValue(double distance, Length.Unit unit) {
		this.distance = Length.of(distance, unit);
	}

	@Override
	public String toString() {
		return String.format("%." + DISTANCE_PRECISION + "f", distance.to(GlobalSettings.getLengthUnit())) + " " + lengthUnitShort.get(GlobalSettings.getLengthUnit());
	}

	public String toString(Length.Unit unit) {
		return String.format("%." + DISTANCE_PRECISION + "f", distance.to(unit)) + " " + lengthUnitShort.get(unit);
	}

	@Override
	public Value plus(Value other) {
		if(other instanceof DistanceValue) {
			DistanceValue otherDistance = (DistanceValue) other;
			return new DistanceValue(((Length)(otherDistance.getValue())).doubleValue() + distance.doubleValue(), Length.Unit.METER);
		}
		return null;
	}

	@Override
	protected Object getValue() {
		return distance;
	}

	@Override
	public int compareTo(Value other) {
		if(other instanceof DistanceValue)
			return distance.compareTo(((DistanceValue) other).distance);
		return Integer.MAX_VALUE;
	}
}
