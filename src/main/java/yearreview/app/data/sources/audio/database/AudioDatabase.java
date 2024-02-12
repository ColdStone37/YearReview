package yearreview.app.data.sources.audio.database;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class AudioDatabase {
	private TreeSet<ListeningEvent> events;
	private TreeMap<String, AudioData> dataMap;
	private Duration totalDuration = Duration.ZERO;

	public AudioDatabase() {
		events = new TreeSet<ListeningEvent>();
		dataMap = new TreeMap<String, AudioData>();
	}

	public AudioData getDataById(String id) {
		return dataMap.get(id);
	}

	public List<AudioData> getFilteredData(AudioData.Type type) {
		List<AudioData> filteredData = new ArrayList<AudioData>();
		for (AudioData data : dataMap.values())
			if (data.type == type)
				filteredData.add(data);
		return filteredData;
	}

	public boolean hasData(String id) {
		return dataMap.containsKey(id);
	}

	public void insertEvent(ListeningEvent event) {
		events.add(event);
		totalDuration = totalDuration.plus(event.duration);
	}

	public void insertData(AudioData data) {
		dataMap.put(data.id, data);
	}

	public Duration getTotalDuration() {
		return totalDuration;
	}
}