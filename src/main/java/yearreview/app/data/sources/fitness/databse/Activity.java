package yearreview.app.data.sources.fitness.databse;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import yearreview.app.data.processor.toplist.TopListCompatibleItem;
import yearreview.app.util.value.DistanceValue;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

public class Activity implements Comparable<Activity> {
	public final String name;
	public final ActivityType type;
	public final GPX track;
	public final Instant time;
	public final Duration duration;
	public final Length distance;


	public Activity(String name, ActivityType type, GPX track) {
		this.type = type;
		this.name = name;
		this.track = track;
		time = getTrackStart(track);
		duration = Duration.between(time, getTrackEnd(track));
		distance = getTrackLength(track);
	}

	public Activity(String name, ActivityType type, Instant time, Duration duration, Length distance, GPX track) {
		this.type = type;
		this.name = name;
		this.time = time;
		this.duration = duration;
		this.distance = distance;
		this.track = track;
	}

	public Activity(String name, ActivityType type, Instant time, Duration duration, Length distance) {
		this.type = type;
		this.name = name;
		this.time = time;
		this.duration = duration;
		this.distance = distance;
		this.track = null;
	}

	private static Length getTrackLength(GPX track) {
		return track.tracks()
				.flatMap(Track::segments)
				.findFirst()
				.map(TrackSegment::points).orElse(Stream.empty())
				.collect(Geoid.WGS84.toPathLength());
	}

	private static Instant getTrackStart(GPX track) {
		Optional<WayPoint> wPoint = track.tracks().flatMap(Track::segments).findFirst().map(TrackSegment::points).orElse(Stream.empty()).findFirst();
		if(wPoint.isPresent() && wPoint.get().getTime().isPresent())
			return wPoint.get().getTime().get();
		return null;
	}

	private static Instant getTrackEnd(GPX track) {
		Optional<WayPoint> wPoint = track.tracks().flatMap(Track::segments).findFirst().map(TrackSegment::points).orElse(Stream.empty()).reduce((first, second) -> second);
		if(wPoint.isPresent() && wPoint.get().getTime().isPresent())
			return wPoint.get().getTime().get();
		return null;
	}

	private Length getTrackLengthUntil(Instant t) {
		return track.tracks()
				.flatMap(Track::segments)
				.findFirst()
				.map(TrackSegment::points).orElse(Stream.empty())
				.filter(e -> e.getTime().orElse(t).isBefore(t))
				.collect(Geoid.WGS84.toPathLength());
	}

	public Length getDistanceUntil(Instant t) {
		if(t.isBefore(time))
			return Length.of(0, Length.Unit.METER);
		if(t.isAfter(time.plus(duration)))
			return distance;
		return getTrackLengthUntil(t);
	}

	@Override
	public String toString() {
		return type.name() + " from " + time + " to " + time.plus(duration) + " for " + new DistanceValue(distance);
	}

	@Override
	public int compareTo(Activity other) {
		return time.compareTo(other.time);
	}
}
