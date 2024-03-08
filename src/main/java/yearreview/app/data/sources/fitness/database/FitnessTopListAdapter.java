package yearreview.app.data.sources.fitness.database;

import io.jenetics.jpx.Length;
import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.data.processor.toplist.TopListItem;
import yearreview.app.util.value.CounterValue;
import yearreview.app.util.value.DistanceValue;
import yearreview.app.util.value.DurationValue;
import yearreview.app.util.value.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * A {@link TopListAdapter} for the {@link FitnessDatabase} that outputs the Activities with their Distances and Durations.
 *
 * @author ColdStone37
 */
public class FitnessTopListAdapter implements TopListAdapter {
	private final static List<Value> defaultValues = new ArrayList<Value>() {
		{
			add(new DistanceValue(LENGTH_ZERO));
			add(new DurationValue(Duration.ZERO));
			add(new CounterValue(0));
		}
	};
	/**
	 * Constant that stores a Length of 0 meters since {@link Length} does not provide one.
	 */
	private final static Length LENGTH_ZERO = Length.of(0, Length.Unit.METER);
	/**
	 * Database to adapt for the {@link yearreview.app.data.processor.toplist.TopListGenerator}.
	 */
	private final FitnessDatabase database;
	/**
	 * Iterator for the Activities provided by the {@link FitnessDatabase}.
	 */
	private final Iterator<Activity> activityIterator;
	/**
	 * Activity that is currently processed by the Adapter.
	 */
	private Activity currentActivity;
	/**
	 * Distance of the current {@link Activity} that was already added to the {@link TopListElement}.
	 */
	private Length currentActivityDistance;
	/**
	 * Duration of the current {@link Activity} that was already added to the {@link TopListElement}.
	 */
	private Duration currentActivityDuration;
	/**
	 * Maps all {@link ActivityType ActivityTypes} to their {@link TopListElement TopListElements}.
	 */
	private final Map<ActivityType, TopListElement> typeMap;

	/**
	 * Constructs a FitnessDistanceTopListAdapter from a Database.
	 * @param database database to adapt for the {@link yearreview.app.data.processor.toplist.TopListGenerator}
	 */
	public FitnessTopListAdapter(FitnessDatabase database) {
		this.database = database;
		activityIterator = database.iterator();
		typeMap = new HashMap<>();

		// Try to get the first activity
		if(activityIterator.hasNext())
			currentActivity = activityIterator.next();
		currentActivityDistance = LENGTH_ZERO;
		currentActivityDuration = Duration.ZERO;
	}

	/**
	 * Gets the TopListElements at a certain time. Iterates over the activity until one starts after t.
	 * @param t time until which all values should be added up to
	 * @return List of TopListElements with the Values summed up
	 */
	@Override
	public Collection<TopListElement> getElements(Instant t) {
		// Iterate the set until an activity is after t
		while(currentActivity != null && currentActivity.time.isBefore(t)) {
			// Get data of the Activity
			ActivityType type = currentActivity.type;
			Instant activityEnd = currentActivity.time.plus(currentActivity.duration);

			// Get TopListElement or create new one if needed
			TopListElement elem = typeMap.computeIfAbsent(type, k -> new TopListElement(new TopListItem(type.name()), defaultValues));

			if(activityEnd.isAfter(t)) {
				// If the end of the activity is after t only add part of the activity to the TopListElement

				// Distance
				Length distanceDriven = currentActivity.getDistanceUntil(t);
				Length addLength = Length.of(distanceDriven.doubleValue() - currentActivityDistance.doubleValue(), Length.Unit.METER);
				elem.addValue(new DistanceValue(addLength));
				currentActivityDistance = distanceDriven;

				// Duration
				Duration durationUntil = Duration.between(currentActivity.time, t);
				Duration addDuration = durationUntil.minus(currentActivityDuration);
				elem.addValue(new DurationValue(addDuration));
				currentActivityDuration = durationUntil;
			} else {
				// Add remaining part of the activity to the TopListElement
				elem.addValue(new DistanceValue(currentActivity.distance.doubleValue() - currentActivityDistance.doubleValue(), Length.Unit.METER));
				elem.addValue(new DurationValue(currentActivity.duration.minus(currentActivityDuration)));
				elem.addValue(new CounterValue(1));

				// Iterate to the next element
				if(activityIterator.hasNext()){
					currentActivity = activityIterator.next();
					currentActivityDistance = LENGTH_ZERO;
					currentActivityDuration = Duration.ZERO;
				} else {
					currentActivity = null;
				}
			}
		}

		// Return the Collection of TopListElements
		return typeMap.values();
	}
}
