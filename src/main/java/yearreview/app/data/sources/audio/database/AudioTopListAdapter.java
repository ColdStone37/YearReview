package yearreview.app.data.sources.audio.database;

import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.util.value.DurationValue;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AudioTopListAdapter implements TopListAdapter {
	private final AudioDatabase database;
	private final AudioData.Type type;
	private final Iterator<ListeningEvent> eventIterator;
	private ListeningEvent currentEvent;
	private final Map<AudioData, TopListElement> audioMap;
	public AudioTopListAdapter(AudioDatabase database, AudioData.Type type) {
		this.database = database;
		this.type = type;
		eventIterator = database.iterator();
		if(eventIterator.hasNext()){
			currentEvent = eventIterator.next();
		} else {
			currentEvent = null;
		}
		audioMap = new HashMap<AudioData, TopListElement>();
	}

	@Override
	public Collection<TopListElement> getElements(Instant t) {
		while(currentEvent != null && currentEvent.time.isBefore(t)) {
			List<AudioData> data = new ArrayList<AudioData>();
			AudioPiece listenedTo = currentEvent.listenedTo;
			if(type.isPiece()) {
				if(listenedTo.type == type)
					data.add(currentEvent.listenedTo);
			} else {
				data.addAll(listenedTo.filterData(type));
			}
			for(AudioData d : data){
				audioMap.computeIfAbsent(d, k -> new TopListElement(d, new DurationValue(Duration.ZERO)))
						.addValue(new DurationValue(currentEvent.duration));
			}
			if(eventIterator.hasNext()){
				currentEvent = eventIterator.next();
			} else {
				currentEvent = null;
			}
		}
		return audioMap.values();
	}
}
