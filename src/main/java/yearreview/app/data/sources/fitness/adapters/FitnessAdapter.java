package yearreview.app.data.sources.fitness.adapters;

import yearreview.app.data.sources.fitness.databse.FitnessDatabase;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;

public abstract class FitnessAdapter {
	protected final FitnessDatabase database;
	public FitnessAdapter(FitnessDatabase database) {
		this.database = database;
	}

	public static FitnessAdapter getAdapter(FitnessDatabase database, XmlNode config) {
		switch(config.getName()){
			case "Strava":
				return new StravaAdapter(database, config);
		}
		return null;
	}

	public abstract void loadData(Instant start, Instant end) throws IOException;
}
