package yearreview.app.data.sources.fitness;

import yearreview.app.data.sources.DataSource;
import yearreview.app.data.sources.fitness.adapters.FitnessAdapter;
import yearreview.app.data.sources.fitness.databse.Activity;
import yearreview.app.data.sources.fitness.databse.FitnessDatabase;
import yearreview.app.util.xml.XmlNode;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FitnessDataSource extends DataSource {
	private List<FitnessAdapter> adapters;
	private FitnessDatabase database;
	public FitnessDataSource(XmlNode c) {
		super(c);
	}

	@Override
	public void parseConfig(XmlNode config) {
		adapters = new ArrayList<FitnessAdapter>();
		database = new FitnessDatabase();
		for(XmlNode adapter : config)
			adapters.add(FitnessAdapter.getAdapter(database, adapter));
	}

	@Override
	public void loadData(Instant start, Instant end) throws IOException {
		for(FitnessAdapter adapter : adapters)
			adapter.loadData(start, end);
		for(Activity a : database)
			System.out.println(a);
	}
}