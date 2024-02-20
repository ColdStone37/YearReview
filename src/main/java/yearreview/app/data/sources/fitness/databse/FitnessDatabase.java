package yearreview.app.data.sources.fitness.databse;

import yearreview.app.data.sources.audio.database.ListeningEvent;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class FitnessDatabase  implements Iterable<Activity> {
	private final SortedSet<Activity> activities;

	public FitnessDatabase() {
		activities = new TreeSet<Activity>();
	}

	public void insertActivity(Activity a) {
		activities.add(a);
	}

	@Override
	public Iterator<Activity> iterator() {
		return activities.iterator();
	}
}
