package yearreview.app.data.processor.toplist;

import java.net.URL;

public interface TopListCompatibleItem {
	public String getMainText();
	public String getSubText();
	public boolean hasSubText();
	public URL getCover();
}
