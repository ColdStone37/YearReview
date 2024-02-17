package yearreview.app.data.sources;

import yearreview.app.util.xml.XmlNode;

public abstract class DataSourceFactory {
	public static DataSource getDataSource(XmlNode config) {
		switch (config.getName()) {
		}
		return null;
	}
}