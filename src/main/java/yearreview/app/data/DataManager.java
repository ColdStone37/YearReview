package yearreview.app.data;

import yearreview.app.config.ConfigNode;
import yearreview.app.data.sources.DataSource;
import yearreview.app.data.sources.DataSourceFactory;

import java.util.*;

public class DataManager {
	List<DataSource> sources;

	public DataManager(ConfigNode dataConfig) {
		sources = new ArrayList<DataSource>();
		for (ConfigNode sourceConfig : dataConfig) {
			DataSource newSource = DataSourceFactory.getDataSource(sourceConfig);
			if (newSource == null)
				throw new Error("Data Source " + sourceConfig.getName() + " isn't valid.");
			sources.add(newSource);
		}
	}

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