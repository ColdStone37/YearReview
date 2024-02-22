package yearreview.app.data.sources.audio.database;

import java.util.*;

/**
 * Database that stores the {@link ListeningEvent events} of the listening history and the {@link AudioData data} associated to these events.
 *
 * @author ColdStone37
 */
public class AudioDatabase implements Iterable<ListeningEvent> {
	/**
	 * Set of all events sorted by time.
	 */
	private final SortedSet<ListeningEvent> events;
	/**
	 * Map that stores the data with an associated key for fast access.
	 * Data of different {@link yearreview.app.data.sources.audio.database.AudioData.Type Types}
	 * is stored alongside each other and is differentiated by putting the name of the type in front.
	 */
	private final Map<String, AudioData> dataMap;

	/**
	 * Constructs a Database by initializing the internal Datastructures.
	 */
	public AudioDatabase() {
		events = new TreeSet<ListeningEvent>();
		dataMap = new TreeMap<String, AudioData>();
	}

	/**
	 * Gets AudioData inside the Database by type and name.
	 * @param type type of data to get
	 * @param name name of data to get
	 * @return AudioData if the value exists otherwise null
	 */
	public AudioData getDataById(AudioData.Type type, String name) {
		return dataMap.get(type.name() + ":" + name);
	}

	/**
	 * Gets a {@link List} of {@link AudioData} that is filtered by the {@link yearreview.app.data.sources.audio.database.AudioData.Type} of data.
	 * @param type type to filter by
	 * @return filtered data
	 */
	public List<AudioData> getFilteredData(AudioData.Type type) {
		List<AudioData> filteredData = new ArrayList<AudioData>();
		for (AudioData data : dataMap.values())
			if (data.type == type)
				filteredData.add(data);
		return filteredData;
	}

	/**
	 * Tests whether certain data is stored in the database.
	 * @param type type of data to test for
	 * @param name name of data to test for
	 * @return true if the database contains the data, false otherwise
	 */
	public boolean hasData(AudioData.Type type, String name) {
		return dataMap.containsKey(type.name() + ":" + name);
	}

	/**
	 * Inserts an event into the database.
	 * @param event event to insert
	 */
	public void insertEvent(ListeningEvent event) {
		events.add(event);
	}

	/**
	 * Gets AudioData from the database. If the data does not exist it will be created and inserted.
	 * @param name name of data to get
	 * @param type type of data to get
	 * @return requested data
	 */
	public AudioData getData(String name, AudioData.Type type) {
		if(type.isPiece())
			throw new Error("Invalid for data, use getPiece() for this");
		String key = type.name() + ":" + name;
		return dataMap.computeIfAbsent(key, k -> new AudioData(name, type));
	}

	/**
	 * Gets AudioPiece from the database. If the piece does not exist it will be created and inserted
	 * @param name name of piece to get
	 * @param data associated data of the piece
	 * @param type type of data to get (must be a piece)
	 * @return requested piece
	 */
	public AudioPiece getPiece(String name, List<AudioData> data, AudioData.Type type) {
		if(!type.isPiece())
			throw new Error("Invalid for piece, use getData() for this");
		String key = type.name() + ":" + name;
		return (AudioPiece) dataMap.computeIfAbsent(key, k -> new AudioPiece(name, type, data));
	}

	/**
	 * Gets the amount of {@link ListeningEvent events} stored in the database.
	 * @return amount of {@link ListeningEvent events}
	 */
	public int getEventCount(){
		return events.size();
	}

	/**
	 * Gets the amount of {@link AudioData}-elements stored in this database.
	 * @return amount of {@link AudioData}-elements
	 */
	public int getDataCount(){
		return dataMap.size();
	}

	/**
	 * Needed to implement the {@link Iterable}-interface.
	 * @return iterator for the {@link ListeningEvent events} stored in the database
	 */
	@Override
	public Iterator<ListeningEvent> iterator() {
		return events.iterator();
	}
}