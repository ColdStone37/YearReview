package yearreview.app.data;

import yearreview.app.data.sources.DataSource;
import yearreview.app.util.xml.XmlNode;

import java.util.*;

/**
 * Manages all {@link DataSource} by creating them from a given configuration and loading them.
 *
 * @author ColdStone37
 */
public class DataManager {
	/**
	 * List of DataSources loaded from the configuration.
	 */
	private final List<DataSource> sources;

	/**
	 * Creates a DataManager from a given configuration.
	 * @param dataConfig configuration
	 */
	public DataManager(XmlNode dataConfig) {
		// Initialize the DataSources and throw an Error if the configuration is invalid
		sources = new ArrayList<>();
		for (XmlNode sourceConfig : dataConfig) {
			DataSource newSource = DataSource.getDataSource(sourceConfig);
			if (newSource == null)
				throw new Error("Data Source " + sourceConfig.getName() + " isn't valid.");
			sources.add(newSource);
		}
	}

	/**
	 * Gets a DataSource by its tag.
	 * @param tag tag to search for
	 * @return the DataSource or null if no DataSource with that tag exists
	 */
	public DataSource getSourceByTag(String tag) {
		for(DataSource source : sources)
			if(source.tag.equals(tag))
				return source;
		return null;
	}

	/**
	 * Loads the data of all {@link DataSource DataSources} on multiple Threads.
	 */
	public void loadData() {
		// Start all loading threads
		for (DataSource source : sources)
			source.start();

		// Join all loading threads
		try {
			for (DataSource source : sources)
				source.join();
		} catch (InterruptedException e) {
			throw new Error("Atleast one of the DataSources failed to load.");
		}
	}
}