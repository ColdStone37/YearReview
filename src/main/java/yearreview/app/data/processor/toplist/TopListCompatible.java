package yearreview.app.data.processor.toplist;

import yearreview.app.util.xml.XmlNode;

public interface TopListCompatible {
	TopListAdapter getTopListAdapter(XmlNode config);
}
