package yearreview.app.data.processor;

import yearreview.app.data.DataManager;
import yearreview.app.data.processor.toplist.TopListGenerator;
import yearreview.app.util.xml.XmlNode;

/**
 * A DataProcessor processes data from a {@link yearreview.app.data.sources.DataSource} to e.g. create a TopList of the most listened Songs.
 *
 * @author ColdStone37
 */
public abstract class DataProcessor {
	public final String tag;
	private final XmlNode config;
	protected DataProcessor(XmlNode config){
		String t = config.getAttributeByName("tag");
		if(t == null)
			t = config.getName();
		tag = t;
		this.config = config;
	}

	public final void init(DataManager dm) {
		init(dm, config);
	}

	protected abstract void init(DataManager dm, XmlNode config);
	public static DataProcessor getDataProcessor(XmlNode config) {
		switch(config.getName()){
			case "TopListGenerator":
				return new TopListGenerator(config);
			default:
				return null;
		}
	}
}
