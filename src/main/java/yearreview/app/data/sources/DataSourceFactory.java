package yearreview.app.data.sources;

import yearreview.app.data.sources.audio.AudioDataSource;
import yearreview.app.util.xml.XmlNode;

public abstract class DataSourceFactory {
	public static DataSource getDataSource(XmlNode config) {
		switch (config.getName()) {
			case "Audio":
				return new AudioDataSource(config);
		}
		return null;
	}
}