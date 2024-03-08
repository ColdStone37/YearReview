package yearreview.app.data.sources.audio.database;

import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.util.value.CounterValue;
import yearreview.app.util.value.DurationValue;
import yearreview.app.util.value.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * An Adapter that transforms the data stored in an {@link AudioDatabase database} so it can be processed by a {@link yearreview.app.data.processor.toplist.TopListGenerator}.
 *
 * @author ColdStone37
 */
public class AudioTopListAdapter implements TopListAdapter {
	private final static List<Value> defaultValues = new ArrayList<Value>() {
		{
			add(new DurationValue(Duration.ZERO));
			add(new CounterValue(0));
		}
	};
	/**
	 * Database to adapt for the TopList.
	 */
	private final AudioDatabase database;
	/**
	 * Type of data passed to the {@link yearreview.app.data.processor.toplist.TopListGenerator}.
	 */
	private final AudioData.Type type;
	/**
	 * Iterator over the {@link ListeningEvent events} of the {@link AudioTopListAdapter#database}.
	 */
	private final Iterator<ListeningEvent> eventIterator;
	/**
	 * Events that is currently processed.
	 */
	private ListeningEvent currentEvent;
	/**
	 * Duration of the {@link ListeningEvent event} that was already added to the {@link TopListElement}.
	 */
	private Duration currentEventListeningDuration;
	/**
	 * Maps all AudioData object to their associated TopListElements.
	 */
	private final Map<AudioData, TopListElement> audioMap;

	/**
	 * Constructs a AudioTopListAdapter from a given database and {@link AudioData.Type} to get the {@link DurationValue DuraitonValues} for.
	 * @param database database to get the data from
	 * @param type type of data to filter by
	 */
	public AudioTopListAdapter(AudioDatabase database, AudioData.Type type) {
		this.database = database;
		this.type = type;

		// Get iterator for the events
		eventIterator = database.iterator();

		// Get first element if available
		if(eventIterator.hasNext()){
			currentEvent = eventIterator.next();
			currentEventListeningDuration = Duration.ZERO;
		} else {
			currentEvent = null;
		}

		// Initialize audioMap
		audioMap = new HashMap<AudioData, TopListElement>();
	}

	/**
	 * Gets the Elements of the TopList with their values at a certain time.
	 * @param t time until which all values should be added up to
	 * @return elements with their associated {@link yearreview.app.util.value.Value values}
	 */
	@Override
	public Collection<TopListElement> getElements(Instant t) {
		// Iterate over the events until there
		// are none left, or they are after the given time
		while(currentEvent != null && currentEvent.time.isBefore(t)) {
			// Get the piece that was listened to
			AudioPiece listenedTo = currentEvent.listenedTo;

			// Filter the data: If the type is a piece it will only be added if the piece matches,
			// otherwise all matching types from the data-list are added
			List<AudioData> data = new ArrayList<AudioData>();
			if(type.isPiece()) {
				if(listenedTo.type == type)
					data.add(currentEvent.listenedTo);
			} else {
				data.addAll(listenedTo.filterData(type));
			}

			// Test if the event ends before t
			Instant eventEnd = currentEvent.time.plus(currentEvent.duration);
			if(eventEnd.isAfter(t)) {
				// Event ends after t

				// Duration that has to be added to the Values
				Duration part = Duration.between(currentEvent.time.plus(currentEventListeningDuration), t);

				// Add Duration to the TopListElements
				for(AudioData d : data)
					audioMap.computeIfAbsent(d, k -> new TopListElement(d, defaultValues))
							.addValue(new DurationValue(part));

				// Update the Duration that was already added to not add it again in the next iteration
				currentEventListeningDuration = currentEventListeningDuration.plus(part);
				break;
			} else {
				// Add the complete Duration of the event to the TopListElements
				for(AudioData d : data)
					audioMap.computeIfAbsent(d, k -> new TopListElement(d, defaultValues))
							.addValues(new DurationValue(currentEvent.duration.minus(currentEventListeningDuration)), new CounterValue(1));

				// Move the iterator to the next position if possible
				if(eventIterator.hasNext()){
					currentEvent = eventIterator.next();
					currentEventListeningDuration = Duration.ZERO;
				} else {
					currentEvent = null;
				}
			}
		}

		// Return the TopListElements
		return audioMap.values();
	}
}
