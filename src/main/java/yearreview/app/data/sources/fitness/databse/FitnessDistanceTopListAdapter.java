package yearreview.app.data.sources.fitness.databse;

import io.jenetics.jpx.Length;
import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.data.processor.toplist.TopListItem;
import yearreview.app.util.value.DistanceValue;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class FitnessDistanceTopListAdapter implements TopListAdapter {

	private final static Length LENGTH_ZERO = Length.of(0, Length.Unit.METER);
	private final FitnessDatabase database;
	private final Iterator<Activity> activityIterator;
	private Activity currentActivity;
	private Length currentActivityDistance;
	private final Map<ActivityType, TopListElement> typeMap;

	public FitnessDistanceTopListAdapter(FitnessDatabase database) {
		this.database = database;
		activityIterator = database.iterator();
		typeMap = new HashMap<ActivityType, TopListElement>();
		if(activityIterator.hasNext())
			currentActivity = activityIterator.next();
		currentActivityDistance = LENGTH_ZERO;
	}
	@Override
	public Collection<TopListElement> getElements(Instant t) {
		while(currentActivity != null && currentActivity.time.isBefore(t)) {
			ActivityType type = currentActivity.type;
			Instant activityEnd = currentActivity.time.plus(currentActivity.duration);
			TopListElement elem = typeMap.computeIfAbsent(type, k -> new TopListElement(new TopListItem(type.name(), null, null), new DistanceValue(LENGTH_ZERO)));

			if(activityEnd.isAfter(t)) {
				Length distanceDriven = currentActivity.getDistanceUntil(t);
				Length addLength = Length.of(distanceDriven.doubleValue() - currentActivityDistance.doubleValue(), Length.Unit.METER);
				elem.addValue(new DistanceValue(addLength));
				currentActivityDistance = distanceDriven;
			} else {
				elem.addValue(new DistanceValue(currentActivity.distance.doubleValue() - currentActivityDistance.doubleValue(), Length.Unit.METER));

				if(activityIterator.hasNext()){
					currentActivity = activityIterator.next();
					currentActivityDistance = LENGTH_ZERO;
				} else {
					currentActivity = null;
				}
			}
		}
		return typeMap.values();
	}
}
