package yearreview.app.util.value;

/**
 * Enumeration of all ValueTypes currently allowed.
 *
 * @author ColdStone37
 */
public enum ValueType {
	DISTANCE,
	DURATION,
	COUNTER;

	public static ValueType getTypeByName(String name) {
		switch(name){
			case "Distance":
				return DISTANCE;
			case "Duration":
				return DURATION;
			case "Counter":
				return COUNTER;
			default:
				return null;
		}
	}
}
