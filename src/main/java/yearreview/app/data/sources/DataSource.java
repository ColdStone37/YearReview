package yearreview.app.data.sources;

import yearreview.app.config.GlobalSettings;
import yearreview.app.data.DataManager;
import yearreview.app.data.sources.audio.AudioDataSource;
import yearreview.app.data.sources.fitness.FitnessDataSource;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Datasource loads data from paths specified in the configuration.
 * Each Datasource runs on a separate Thread to speed up loading.
 *
 * @author ColdStone37
 */
public abstract class DataSource extends Thread {
	/**
	 * Tag of the source. Might be used to specify dependencies.
	 */
	public final String tag;
	protected final static Logger logger = Logger.getLogger(DataSource.class.getName());

	/**
	 * Constructs a DataSource from a configuration.
	 * @param c configuration of the DataSource
	 */
	public DataSource(XmlNode c) {
		String t = c.getAttributeByName("tag");
		if(t == null)
			t = c.getName();
		tag = t;
		parseConfig(c);
	}

	/**
	 * Function that loads the data on a separate Thread.
	 * Gets called on {@link Thread#start}.
	 */
	@Override
	public void run() {
		try {
			logger.fine("Loading of DataSource \"" + tag + "\" started.");
			loadData(GlobalSettings.getStartTime(), GlobalSettings.getEndTime());
			logger.fine("Loading of DataSource \"" + tag + "\" finished.");
		} catch(IOException e){
			logger.log(Level.WARNING, "IOException while loading DataSource \"" + tag + "\".", e);
		}
	}

	/**
	 * Parses the configuration of the DataSource.
	 * @param c configuration
	 */
	public abstract void parseConfig(XmlNode c);

	/**
	 * Loads the data of the DataSource inside the given time interval.
	 * @param start start time of data to load
	 * @param end end time of data to load
	 * @throws IOException if some files cannot be accessed or parsed
	 */
	public abstract void loadData(Instant start, Instant end) throws IOException;

	/**
	 * Gets a DataSource by the name stored inside the configuration and initializes it with that configuration.
	 * @param config configuration
	 * @return the DataSource or null if a DataSource with the given name does not exist
	 */
	public static DataSource getDataSource(XmlNode config) {
		switch (config.getName()) {
			case "Audio":
				return new AudioDataSource(config);
			case "Fitness":
				return new FitnessDataSource(config);
		}
		return null;
	}
}