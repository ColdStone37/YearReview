package yearreview.app.data.sources.fitness.adapters;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Length;
import yearreview.app.config.GlobalSettings;
import yearreview.app.data.sources.fitness.databse.Activity;
import yearreview.app.data.sources.fitness.databse.ActivityType;
import yearreview.app.data.sources.fitness.databse.FitnessDatabase;
import yearreview.app.util.csv.CSVReader;
import yearreview.app.util.xml.XmlNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.*;

/**
 * An adapter to input the data that can be exported from Strava <a href="https://www.strava.com/athlete/delete_your_account">here</a>.
 *
 * @author ColdStone37
 */
public class StravaAdapter extends FitnessAdapter {
	/**
	 * Relative path where to load the data from.
	 */
	private final String dataPath;
	/**
	 * Index of the language of the data. (currently only supports german)
	 */
	private final int language;

	private final static Logger logger = Logger.getLogger(StravaAdapter.class.getName());

	/**
	 * Map to convert the shorthand of the language to the index.
	 */
	private final static Map<String, Integer> languageNameToIdMapMap = new HashMap<String, Integer>() {{
		put("de", 0);
	}};

	/**
	 * Array of DateTimeFormatters for all languages.
	 */
	private final static DateTimeFormatter[] languageToDateformat = {
			DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss").withZone(ZoneId.of("UTC"))
	};

	/**
	 * List of Maps for all languages to convert the names of activities to the {@link ActivityType ActivityTypes}.
	 */
	private final static List<Map<String, ActivityType>> activityStringToType;

	static {
		activityStringToType = new ArrayList<>(1);
		activityStringToType.add(new HashMap<String, ActivityType>() {{
			put("Radfahrt", ActivityType.CYCLING);
			put("Lauf", ActivityType.RUNNING);
			put("Spaziergang", ActivityType.WALKING);
			put("Training", ActivityType.TRAINING);
			put("Wandern", ActivityType.HIKING);
		}});
	}

	/**
	 * Constructs a StravaAdapter from a FitnessDatabase and configuration
	 * @param database database to insert the activities into
	 * @param config configuration
	 */
	public StravaAdapter(FitnessDatabase database, XmlNode config) {
		super(database);
		// Get the path from the configuration and remove the last slash if needed
		String path = config.getChildContent("Path");
		if(path.charAt(path.length()-1) == '/')
			path = path.substring(path.length()-1);
		dataPath = path;

		// Get the language from the configuration
		language = languageNameToIdMapMap.get(config.getChildContent("Language"));
	}

	/**
	 * Load the Activities from Strava by parsing the activities.csv-file inside the export-folder.
	 * @param start start time of data to load
	 * @param end end time of data to load
	 * @throws IOException if the {@link CSVReader} cannot read the file
	 */
	@Override
	public void loadData(Instant start, Instant end) throws IOException {
		// Get the path to the activities file
		File input = GlobalSettings.getRelativePath(dataPath + "/activities.csv");

		// Create a CSVReader from the input-file
		CSVReader.getRowStream(input).skip(1).forEach(row -> parseCSVLine(row, start, end));
	}

	/**
	 * Parses a line of the CSV representing a single Activity
	 * @param line line to parse
	 * @param start start time of Activities to consider
	 * @param end end time of Activities to consider
	 */
	private void parseCSVLine(List<String> line, Instant start, Instant end) {
		// If the line is a valid Strava-Export line
		if(line.size() == 86){
			// Get the data from the Line
			String activityName = line.get(2);
			Instant startActivity = languageToDateformat[language].parse(line.get(1), Instant::from);
			Duration duration = Duration.ofSeconds(Integer.parseInt(line.get(5)));
			Length distance = Length.of(Double.parseDouble(line.get(6).replace(",", ".")), Length.Unit.KILOMETER);
			ActivityType type = activityStringToType.get(language).get(line.get(3));

			// If the Activity lies in the interval add it to the Database, otherwise ignore it
			if(startActivity.isAfter(start) && startActivity.isBefore(end)) {
				if(!line.get(12).isEmpty()){
					// If the Activity has a .gpx-file...
					Path activityPath = GlobalSettings.getRelativePath(dataPath + "/" + line.get(12)).toPath();
					try {
						// try to parse it
						database.insertActivity(new Activity(activityName, type, startActivity, duration, distance, GPX.read(activityPath)));
					} catch (IOException e) {
						logger.log(Level.WARNING, "Couldn't load .gpx-file \"" + activityPath + "\".");
						database.insertActivity(new Activity(activityName, type, startActivity, duration, distance));
					}
				} else {
					database.insertActivity(new Activity(activityName, type, startActivity, duration, distance));
				}
			}
		}
	}
}
