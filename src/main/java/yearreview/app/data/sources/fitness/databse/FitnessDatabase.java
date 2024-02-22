package yearreview.app.data.sources.fitness.databse;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Database that stores {@link Activity Activities} sorted by their starting times.
 * The Database also allows for iteration of Activities by their starting time.
 *
 * @author ColdStone37
 */
public class FitnessDatabase  implements Iterable<Activity> {
	/**
	 * A Set that stores all {@link Activity Activities} by sorted by their starting times.
	 */
	private final SortedSet<Activity> activities;

	/**
	 * Default Constructor for a FitnessDatabase that initializes the Set used for storing the {@link Activity Activities}.
	 */
	public FitnessDatabase() {
		activities = new TreeSet<>();
	}

	/**
	 * Inserts an Activity into the Database.
	 * @param a Activity to insert
	 */
	public void insertActivity(Activity a) {
		activities.add(a);
	}

	/**
	 * Returns an iterator for the Database that provides the Activities sorted by their starting time.
	 * Needed to implement the {@link Iterable}-interface.
	 * @return Iterator for the Activities stored in the Database
	 */
	@Override
	public Iterator<Activity> iterator() {
		return activities.iterator();
	}
}