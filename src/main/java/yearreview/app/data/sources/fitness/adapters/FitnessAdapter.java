package yearreview.app.data.sources.fitness.adapters;

import yearreview.app.data.sources.fitness.databse.FitnessDatabase;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;

/**
 * Abstract class that defines how an adapter to a {@link FitnessDatabase} should work.
 *
 * @author ColdStone37
 */
public abstract class FitnessAdapter {
	/**
	 * Database to insert the data into.
	 */
	protected final FitnessDatabase database;

	/**
	 * Constructs a FitnessAdapter given a database to use for inserting the data
	 * @param database database to insert the data into
	 */
	public FitnessAdapter(FitnessDatabase database) {
		this.database = database;
	}

	/**
	 * Static function to get an adapter by its name
	 * @param database database to use for inserting the data
	 * @param config configuration file for the adapter
	 * @return the adapter or null if an adapter with the given name does not exist
	 */
	public static FitnessAdapter getAdapter(FitnessDatabase database, XmlNode config) {
		switch(config.getName()){
			case "Strava":
				return new StravaAdapter(database, config);
		}
		return null;
	}

	/**
	 * Function that loads the data into the {@link FitnessDatabase}.
	 * @param start start time of data to load
	 * @param end end time of data to load
	 * @throws IOException if the all or some of the files cannot be read
	 */
	public abstract void loadData(Instant start, Instant end) throws IOException;
}
