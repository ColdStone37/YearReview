package yearreview.app.data.sources.audio.database;

import java.time.Duration;
import java.util.*;

public class AudioDatabase {
	private TreeSet<ListeningEvent> events;
	private Map<String, AudioData> dataMap;
	private Duration totalDuration = Duration.ZERO;

	public AudioDatabase() {
		events = new TreeSet<ListeningEvent>();
		dataMap = new TreeMap<String, AudioData>();
	}

	public AudioData getDataById(AudioData.Type type, String name) {
		return dataMap.get(type.name() + ":" + name);
	}

	public List<AudioData> getFilteredData(AudioData.Type type) {
		List<AudioData> filteredData = new ArrayList<AudioData>();
		for (AudioData data : dataMap.values())
			if (data.type == type)
				filteredData.add(data);
		return filteredData;
	}

	public boolean hasData(AudioData.Type type, String name) {
		return dataMap.containsKey(type.name() + ":" + name);
	}

	public void insertEvent(ListeningEvent event) {
		events.add(event);
		totalDuration = totalDuration.plus(event.duration);
	}

	public Duration getTotalDuration() {
		return totalDuration;
	}

	public AudioData getData(String name, AudioData.Type type) {
		if(type.isPiece())
			throw new Error("Invalid for data, use getPiece() for this");
		String key = type.name() + ":" + name;
		return dataMap.computeIfAbsent(key, k -> new AudioData(name, type));
	}

	public AudioPiece getPiece(String name, List<AudioData> data, AudioData.Type type) {
		if(!type.isPiece())
			throw new Error("Invalid for piece, use getData() for this");
		String key = type.name() + ":" + name;
		return (AudioPiece) dataMap.computeIfAbsent(key, k -> new AudioPiece(name, type, data));
	}

	public int getEventCount(){
		return events.size();
	}

	public int getDataCount(){
		return dataMap.size();
	}
}