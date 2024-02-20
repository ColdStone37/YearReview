package yearreview.app.data.sources.fitness.databse;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;
import yearreview.app.util.value.DistanceValue;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

public class Activity implements Comparable<Activity> {
	public final String name;
	public final ActivityType type;
	public final GPX track;
	public final Instant start;
	public final Instant end;
	public final DistanceValue distance;


	public Activity(String name, ActivityType type, GPX track) {
		this.type = type;
		this.name = name;
		this.track = track;
		start = getTrackStart(track);
		end = getTrackEnd(track);
		distance = getTrackLength(track);
	}

	public Activity(String name, ActivityType type, Instant start, Instant end, DistanceValue distance, GPX track) {
		this.type = type;
		this.name = name;
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.track = track;
	}

	public Activity(String name, ActivityType type, Instant start, Instant end, DistanceValue distance) {
		this.type = type;
		this.name = name;
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.track = null;
	}

	private static DistanceValue getTrackLength(GPX track) {
		return new DistanceValue(track.tracks()
				.flatMap(Track::segments)
				.findFirst()
				.map(TrackSegment::points).orElse(Stream.empty())
				.collect(Geoid.WGS84.toPathLength()));
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

	@Override
	public String toString() {
		return type.name() + " from " + start + " to " + end + " for " + distance;
	}

	@Override
	public int compareTo(Activity other) {
		return start.compareTo(other.start);
	}
}
