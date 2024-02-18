package yearreview.app.data.sources;

import yearreview.app.config.GlobalSettings;
import yearreview.app.util.xml.XmlNode;

import java.text.ParseException;
import java.time.Instant;

public abstract class DataSource extends Thread {
	public final String tag;

	public DataSource(XmlNode c) {
		tag = c.getAttributeByName("tag");
		parseConfig(c);
	}

	@Override
	public void run() {
		try {
			loadData(GlobalSettings.getStartTime(), GlobalSettings.getEndTime());
		} catch(ParseException e){
			throw new Error("Couldn't load DataSource: " + e);
		}
	}

	public abstract void parseConfig(XmlNode c);

	public abstract void loadData(Instant start, Instant end) throws ParseException;
}