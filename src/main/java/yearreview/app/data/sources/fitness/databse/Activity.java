package yearreview.app.data.sources.fitness.databse;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import yearreview.app.util.value.DistanceValue;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Internal representation of a fitness-activity, stored inside the {@link FitnessDatabase}.
 *
 * @author ColdStone37
 */
public class Activity implements Comparable<Activity> {
	/**
	 * Name of the activity.
	 */
	public final String name;
	/**
	 * Type of the activity.
	 */
	public final ActivityType type;
	/**
	 * GPX-track of the activity if available.
	 */
	public final GPX track;
	/**
	 * Start time of the activity.
	 */
	public final Instant time;
	/**
	 * Duration of the activity.
	 */
	public final Duration duration;
	/**
	 * Distance of the activity. (Might not make sense in some cases)
	 */
	public final Length distance;


	/**
	 * Constructor for an activity that gets timing values from the GPX-track.
	 * @param name name of the activity
	 * @param type type of the activity
	 * @param track GPX-Track used to gather the rest of the needed data
	 */
	public Activity(String name, ActivityType type, GPX track) {
		this.type = type;
		this.name = name;
		this.track = track;

		// Get remaining data from the GPX-track
		time = getTrackStart(track);
		assert(time != null);
		duration = Duration.between(time, getTrackEnd(track));
		distance = getTrackLength(track);
	}

	/**
	 * Constructor for an activity with all values.
	 * @param name name of the activity
	 * @param type type of the activity
	 * @param time starting time of the activity
	 * @param duration duration of the activity
	 * @param distance distance of the activity (might not make sense in some cases)
	 * @param track GPX-track of the activity
	 */
	public Activity(String name, ActivityType type, Instant time, Duration duration, Length distance, GPX track) {
		this.type = type;
		this.name = name;
		this.time = time;
		this.duration = duration;
		this.distance = distance;
		this.track = track;
	}

	/**
	 * Constructor for an activity without a GPX-track.
	 * @param name name of the activity
	 * @param type type of the activity
	 * @param time starting time of the activity
	 * @param duration duration of the activity
	 * @param distance distance of the activity (might not make sense in some cases)
	 */
	public Activity(String name, ActivityType type, Instant time, Duration duration, Length distance) {
		this.type = type;
		this.name = name;
		this.time = time;
		this.duration = duration;
		this.distance = distance;
		this.track = null;
	}

	/**
	 * Gets the Length of a GPX-track by summing up the distances between the Waypoints.
	 * @param track GPX-track to calculate the length of
	 * @return total Length of the passed track
	 */
	private static Length getTrackLength(GPX track) {
		return track.tracks()
				.flatMap(Track::segments)
				.findFirst()
				.map(TrackSegment::points).orElse(Stream.empty())
				.collect(Geoid.WGS84.toPathLength());
	}

	/**
	 * Gets the starting time of a GPX-track by finding the time of the first {@link WayPoint}.
	 * @param track GPX-track to find the starting time of
	 * @return starting time if available otherwise null
	 */
	private static Instant getTrackStart(GPX track) {
		Optional<WayPoint> wPoint = track.tracks().flatMap(Track::segments).findFirst().map(TrackSegment::points).orElse(Stream.empty()).findFirst();
		if(wPoint.isPresent() && wPoint.get().getTime().isPresent())
			return wPoint.get().getTime().get();
		return null;
	}

	/**
	 * Gets the ending time of a GPX-track by finding the time of the last {@link WayPoint}.
	 * @param track GPX-track to find the ending time of
	 * @return ending time if available otherwise null
	 */
	private static Instant getTrackEnd(GPX track) {
		Optional<WayPoint> wPoint = track.tracks().flatMap(Track::segments).findFirst().map(TrackSegment::points).orElse(Stream.empty()).reduce((first, second) -> second);
		if(wPoint.isPresent() && wPoint.get().getTime().isPresent())
			return wPoint.get().getTime().get();
		return null;
	}

	/**
	 * Gets the Distance of the activity until a certain time.
	 * Clamps values before the starting time of the activity to 0 and values after the activity to the length of the activity.
	 * @param t time until the Length of the track should be calculated
	 * @return Length of the track before t
	 */
	public Length getDistanceUntil(Instant t) {
		// Clamping
		if(t.isBefore(time))
			return Length.of(0, Length.Unit.METER);
		if(t.isAfter(time.plus(duration)))
			return distance;
		return getTrackLengthUntil(t);
	}

	/**
	 * Calculates the length of a GPX-track until a certain time.
	 * @param t time until the Length of the track should be summed up to
	 * @return Length of track before t
	 */
	private Length getTrackLengthUntil(Instant t) {
		return track.tracks()
				.flatMap(Track::segments)
				.findFirst()
				.map(TrackSegment::points).orElse(Stream.empty())
				.filter(e -> e.getTime().orElse(t).isBefore(t))
				.collect(Geoid.WGS84.toPathLength());
	}

	/**
	 * Custom toString()-function for activities.
	 * @return String with name of the activity, the starting and ending times and the distance
	 */
	@Override
	public String toString() {
		return type.name() + " from " + time + " to " + time.plus(duration) + " for " + new DistanceValue(distance);
	}

	/**
	 * Compares one activity to another by comparing the starting times.
	 * @param other Activity to compare against
	 * @return negative value if this activity is before other, positive if it is after other
	 */
	@Override
	public int compareTo(Activity other) {
		return time.compareTo(other.time);
	}
}
