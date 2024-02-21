package yearreview.app.data.processor.toplist;

import yearreview.app.util.image.ImageReference;

public interface TopListCompatibleItem {
	public String getMainText();
	public String getSubText();
	public boolean hasSubText();
	public ImageReference getImage();
}
