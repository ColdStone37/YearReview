package yearreview.app.data;

import yearreview.app.data.processor.DataProcessor;
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

	private final Map<String, DataProcessor> processors;
	private final static Logger logger = Logger.getLogger(DataManager.class.getName());
	private boolean isLoaded = false;

	/**
	 * Creates a DataManager from a given configuration.
	 * @param dataConfig configuration
	 */
	public DataManager(XmlNode dataConfig, XmlNode processorsConfig) {
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

		processors = new TreeMap<>();
		for(XmlNode processorConfig : processorsConfig) {
			DataProcessor newProcessor = DataProcessor.getDataProcessor(processorConfig);
			if(newProcessor == null) {
				logger.log(Level.WARNING, "DataProcessor couldn't be initialized, no DataProcessor with name \"" + processorConfig.getName() + "\" exists.");
			} else {
				if(processors.put(newProcessor.tag, newProcessor) != null) {
					logger.severe("Multiple DataProcessors with the name \"" + newProcessor.tag + "\" found, consider adding a tag to one of them.");
					System.exit(1);
				}
			}
		}
	}

	/**
	 * Gets a DataSource by its tag. If a DataSource with the given tag doesn't exist it will throw a severe warning and quit.
	 * @param tag tag to search for
	 * @return the DataSource with the given tag
	 */
	public DataSource getSourceByTag(String tag) {
		DataSource ds = sources.get(tag);
		if(ds == null) {
			logger.severe("A DataSource with the tag \"" + tag + "\" doesn't exists.");
			System.exit(1);
		}
		return ds;
	}

	/**
	 * Gets a DataProcessor by its tag. If a DataProcessor with the given tag doesn't exist it will throw a severe warning and quit.
	 * @param tag tag to search for
	 * @return the DataProcessor with the given tag
	 */
	public DataProcessor getProcessorByTag(String tag) {
		DataProcessor dp = processors.get(tag);
		if(dp == null) {
			logger.severe("A DataProcessor with the tag \"" + tag + "\" doesn't exists.");
			System.exit(1);
		}
		return dp;
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

		// Initialize Processors
		for(DataProcessor processor : processors.values())
			processor.init(this);

		logger.log(Level.INFO, "Finished loading DataSources.");

		isLoaded = true;
	}

	public boolean isLoaded(){
		return isLoaded;
	}
}