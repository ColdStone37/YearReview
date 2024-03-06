package yearreview.app.data;

import yearreview.app.data.sources.DataSource;
import yearreview.app.util.xml.XmlNode;

import java.util.*;
import java.util.logging.*;

/**
 * Manages all {@link DataSource} by creating them from a given configuration and loading them.
 *
 * @author ColdStone37
 */
public class DataManager {
	/**
	 * List of DataSources loaded from the configuration.
	 */
	private final Map<String, DataSource> sources;
	private final static Logger logger = Logger.getLogger(DataManager.class.getName());

	/**
	 * Creates a DataManager from a given configuration.
	 * @param dataConfig configuration
	 */
	public DataManager(XmlNode dataConfig) {
		// Initialize the DataSources and throw an Error if the configuration is invalid
		sources = new TreeMap<>();
		for (XmlNode sourceConfig : dataConfig) {
			DataSource newSource = DataSource.getDataSource(sourceConfig);
			if (newSource == null) {
				logger.log(Level.WARNING, "DataSource couldn't be initialized, no DataSource with name \"" + sourceConfig.getName() + "\" exists.");
			} else {
				if(sources.put(newSource.tag, newSource) != null) {
					logger.severe("Multiple DataSources with the name \"" + newSource.tag + "\" found, consider adding a tag to one of them.");
					System.exit(1);
				}
			}
		}
	}

	/**
	 * Gets a DataSource by its tag.
	 * @param tag tag to search for
	 * @return the DataSource or null if no DataSource with that tag exists
	 */
	public DataSource getSourceByTag(String tag) {
		return sources.get(tag);
	}

	/**
	 * Loads the data of all {@link DataSource DataSources} on multiple Threads.
	 */
	public void loadData() {
		logger.log(Level.INFO, "Started loading DataSources.");

		// Start all loading threads
		for (DataSource source : sources.values())
			source.start();

		// Join all loading threads
		try {
			for (DataSource source : sources.values())
				source.join();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "InterruptedException during loading of DataSources.", e);
			System.exit(1);
		}

		logger.log(Level.INFO, "Finished loading DataSources.");
	}
}