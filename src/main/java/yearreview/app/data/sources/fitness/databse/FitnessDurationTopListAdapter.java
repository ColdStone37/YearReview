package yearreview.app.data.sources.fitness.databse;

import io.jenetics.jpx.Length;
import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.data.processor.toplist.TopListItem;
import yearreview.app.util.value.DistanceValue;
import yearreview.app.util.value.DurationValue;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FitnessDurationTopListAdapter implements TopListAdapter {
	private final FitnessDatabase database;
	private final Iterator<Activity> activityIterator;
	private Activity currentActivity;
	private Duration currentActivityDuration;
	private final Map<ActivityType, TopListElement> typeMap;

	public FitnessDurationTopListAdapter(FitnessDatabase database) {
		this.database = database;
		activityIterator = database.iterator();
		typeMap = new HashMap<ActivityType, TopListElement>();
		if(activityIterator.hasNext())
			currentActivity = activityIterator.next();
		currentActivityDuration = Duration.ZERO;
	}
	@Override
	public Collection<TopListElement> getElements(Instant t) {
		while(currentActivity != null && currentActivity.time.isBefore(t)) {
			ActivityType type = currentActivity.type;
			Instant activityEnd = currentActivity.time.plus(currentActivity.duration);
			TopListElement elem = typeMap.computeIfAbsent(type, k -> new TopListElement(new TopListItem(type.name(), null, null), new DurationValue(Duration.ZERO)));

			if(activityEnd.isAfter(t)) {
				Duration durationUntil = Duration.between(currentActivity.time, t);
				Duration addDuration = durationUntil.minus(currentActivityDuration);
				elem.addValue(new DurationValue(addDuration));
				currentActivityDuration = durationUntil;
			} else {
				elem.addValue(new DurationValue(currentActivity.duration.minus(currentActivityDuration)));

				if(activityIterator.hasNext()){
					currentActivity = activityIterator.next();
					currentActivityDuration = Duration.ZERO;
				} else {
					currentActivity = null;
				}
			}
		}
		return typeMap.values();
	}
}
