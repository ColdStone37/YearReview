package yearreview.app.data.processor.toplist;

import yearreview.app.util.image.ImageReference;

public class TopListItem implements TopListCompatibleItem {
	private final String mainText;
	private final String subText;
	private final ImageReference img;

	public TopListItem(String main, String sub, ImageReference img) {
		mainText = main;
		subText = sub;
		this.img = img;
	}

	public TopListItem(String main, String sub) {
		mainText = main;
		subText = sub;
		this.img = null;
	}

	public TopListItem(String main) {
		mainText = main;
		subText = null;
		this.img = null;
	}

	@Override
	public String getMainText() {
		return mainText;
	}

	@Override
	public String getSubText() {
		return subText;
	}

	@Override
	public boolean hasSubText() {
		return subText != null;
	}

	@Override
	public ImageReference getImage() {
		return img;
	}
}
