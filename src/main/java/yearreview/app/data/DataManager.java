package yearreview.app.data;

import yearreview.app.data.sources.DataSource;
import yearreview.app.data.sources.DataSourceFactory;
import yearreview.app.util.xml.XmlNode;

import java.util.*;

public class DataManager {
	List<DataSource> sources;

	public DataManager(XmlNode dataConfig) {
		sources = new ArrayList<DataSource>();
		for (XmlNode sourceConfig : dataConfig) {
			DataSource newSource = DataSourceFactory.getDataSource(sourceConfig);
			if (newSource == null)
				throw new Error("Data Source " + sourceConfig.getName() + " isn't valid.");
			sources.add(newSource);
		}
	}

	public DataSource getSourceByTag(String tag) {
		for(DataSource source : sources)
			if(source.tag.equals(tag))
				return source;
		return null;
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