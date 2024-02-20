package yearreview.app.data.sources.fitness.adapters;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Length;
import yearreview.app.config.GlobalSettings;
import yearreview.app.data.sources.fitness.databse.Activity;
import yearreview.app.data.sources.fitness.databse.ActivityType;
import yearreview.app.data.sources.fitness.databse.FitnessDatabase;
import yearreview.app.util.csv.CSVReader;
import yearreview.app.util.value.DistanceValue;
import yearreview.app.util.xml.XmlNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StravaAdapter extends FitnessAdapter {
	private final String dataPath;
	private final int language;

	private final static Map<String, Integer> languageNameToIdMapMap = new HashMap<String, Integer>() {{
		put("de", 0);
	}};

	private final static List<Map<String, ActivityType>> activityStringToType;

	private final static DateTimeFormatter[] languageToDateformat = {
			DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss").withZone(ZoneId.of("UTC"))
	};

	static {
		activityStringToType = new ArrayList<Map<String, ActivityType>>(1);
		activityStringToType.add(new HashMap<String, ActivityType>() {{
			put("Radfahrt", ActivityType.CYCLING);
			put("Lauf", ActivityType.RUNNING);
			put("Spaziergang", ActivityType.WALKING);
			put("Training", ActivityType.TRAINING);
			put("Wandern", ActivityType.HIKING);
		}});
	}

	public StravaAdapter(FitnessDatabase database, XmlNode config) {
		super(database);
		String path = config.getChildContent("Path");
		if(path.charAt(path.length()-1) == '/')
			path = path.substring(path.length()-1);
		dataPath = path;
		language = languageNameToIdMapMap.get(config.getChildContent("Language"));
	}

	@Override
	public void loadData(Instant start, Instant end) throws IOException {
		File input = GlobalSettings.getRelativePath(dataPath + "/activities.csv");

		CSVReader.getLineStream(input).skip(1).forEach(line -> parseCSVLine(line, start, end));
	}

	private void parseCSVLine(List<String> line, Instant start, Instant end) {
		if(line.size() == 86){
			String activityName = line.get(2);
			Instant startActivity = languageToDateformat[language].parse(line.get(1), Instant::from);
			Instant endActivity = startActivity.plusSeconds(Integer.parseInt(line.get(5)));
			DistanceValue distance = new DistanceValue(Double.parseDouble(line.get(6).replace(",", ".")), Length.Unit.KILOMETER);
			ActivityType type = activityStringToType.get(language).get(line.get(3));
			if(type == null)
				System.out.println(line.get(3));
			if(startActivity.isAfter(start) && startActivity.isBefore(end)) {
				if(!line.get(12).isEmpty()){
					Path activityPath = GlobalSettings.getRelativePath(dataPath + "/" + line.get(12)).toPath();
					try {
						database.insertActivity(new Activity(activityName, type, startActivity, endActivity, distance, GPX.read(activityPath)));
					} catch (IOException e) {
						System.out.println("Couldn't load activity " + activityPath);
					}
				} else {
					database.insertActivity(new Activity(activityName, type, startActivity, endActivity, distance));
				}
			}
		}
	}
}
