package yearreview.app.data.processor.toplist;

import yearreview.app.util.image.ImageReference;

/**
 * Basic Implementation of {@link TopListCompatibleItem} that can be used if no special functionality is needed.
 *
 * @author ColdStone37
 */
public class TopListItem implements TopListCompatibleItem {
	/**
	 * Main text to display for the item.
	 */
	private final String mainText;
	/**
	 * Sub text to display for the item.
	 */
	private final String subText;
	/**
	 * Image to display alongside the item.
	 */
	private final ImageReference img;

	/**
	 * Constructs a TopListItem from all Values.
	 * @param main main text to display
	 * @param sub sub text to display
	 * @param img image to display alongside
	 */
	public TopListItem(String main, String sub, ImageReference img) {
		mainText = main;
		subText = sub;
		this.img = img;
	}

	/**
	 * Constructs a TopListItem with only text given
	 * @param main main text to display
	 * @param sub sub text to display
	 */
	public TopListItem(String main, String sub) {
		mainText = main;
		subText = sub;
		this.img = null;
	}

	/**
	 * Constructs a TopListItem with only main text given
	 * @param main main text to display
	 */
	public TopListItem(String main) {
		mainText = main;
		subText = null;
		this.img = null;
	}

	/**
	 * Gets the main text.
	 * @return main text
	 */
	@Override
	public String getMainText() {
		return mainText;
	}

	/**
	 * Gets the sub text. Value Does not necessarily need to be set (Can be checked using the {@link TopListItem#hasSubText()}-function).
	 * @return sub text
	 */
	@Override
	public String getSubText() {
		return subText;
	}

	/**
	 * Gets whether the item has a sub text.
	 * @return true if <code>subText != null</code>, false otherwise
	 */
	@Override
	public boolean hasSubText() {
		return subText != null;
	}

	/**
	 * Gets the Image that should be displayed alongside the data. Might be null if it was not set.
	 * @return Reference to Image
	 */
	@Override
	public ImageReference getImage() {
		return img;
	}
}
