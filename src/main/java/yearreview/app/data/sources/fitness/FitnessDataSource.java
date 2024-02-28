package yearreview.app.data.sources.fitness;

import yearreview.app.data.processor.toplist.TopListAdapter;
import yearreview.app.data.sources.DataSource;
import yearreview.app.data.sources.fitness.adapters.FitnessAdapter;
import yearreview.app.data.sources.fitness.databse.*;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * A {@link DataSource} that loads the Fitness Data exported from services like Strava using {@link FitnessAdapter adapters}.
 *
 * @author ColdStone37
 */
public class FitnessDataSource extends DataSource {
	/**
	 * List of adapters to use for loading data.
	 */
	private List<FitnessAdapter> adapters;
	/**
	 * Database to store the activities in.
	 */
	private FitnessDatabase database;
	private final static Logger logger = Logger.getLogger(FitnessDataSource.class.getName());

	/**
	 * Constructs a FitnessDataSource using a configuration.
	 * @param c configuration of the DataSource
	 */
	public FitnessDataSource(XmlNode c) {
		super(c);
	}

	/**
	 * Parses the configuration and initializes the adapters.
	 * @param config configuration to parse
	 */
	@Override
	public void parseConfig(XmlNode config) {
		adapters = new ArrayList<>();
		database = new FitnessDatabase();
		for(XmlNode adapter : config){
			FitnessAdapter fa = FitnessAdapter.getAdapter(database, adapter);
			if(fa == null) {
				logger.log(Level.WARNING, "Adapter couldn't be initialized, no FitnessAdapter with name \"" + adapter.getName() + "\" was found.");
			} else {
				adapters.add(FitnessAdapter.getAdapter(database, adapter));
			}
		}
	}

	/**
	 * Loads the data from the {@link FitnessAdapter adapters}.
	 * @param start start time for Activities to consider
	 * @param end end time for Activities to consider
	 * @throws IOException if some files cannot be loaded
	 */
	@Override
	public void loadData(Instant start, Instant end) throws IOException {
		for(FitnessAdapter adapter : adapters)
			adapter.loadData(start, end);
	}

	/**
	 * Gets a TopListAdapter for the Database that allows for creation of TopLists using the {@link yearreview.app.data.processor.toplist.TopListGenerator}.
	 * @return Adapter to create TopLists from the Database
	 */
	public TopListAdapter getTopListAdapter() {
		return new FitnessTopListAdapter(database);
	}
}