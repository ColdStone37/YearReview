package yearreview.app.data.sources;

import yearreview.app.config.GlobalSettings;
import yearreview.app.util.xml.XmlNode;

import java.time.Instant;

public abstract class DataSource extends Thread {
	public final String tag;

	public DataSource(XmlNode c) {
		tag = c.getAttributeByName("tag");
		parseConfig(c);
	}

	@Override
	public void run() {
		loadData(GlobalSettings.getStartTime(), GlobalSettings.getEndTime());
	}

	public abstract void parseConfig(XmlNode c);

	public abstract void loadData(Instant start, Instant end);
}