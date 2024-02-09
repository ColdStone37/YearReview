package yearreview.app.data.sources;

import yearreview.app.config.ConfigNode;
import yearreview.app.config.GlobalSettings;

import java.time.Instant;

public abstract class DataSource extends Thread {
	public final String tag;

	public DataSource(ConfigNode c) {
		tag = c.getAttributeByName("tag");
		parseConfig(c);
	}

	@Override
	public void run() {
		loadData(GlobalSettings.getStartTime(), GlobalSettings.getEndTime());
	}

	public abstract void parseConfig(ConfigNode c);

	public abstract void loadData(Instant start, Instant end);
}