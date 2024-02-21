package yearreview.app.data.processor.toplist;

import yearreview.app.util.image.ImageReference;

/**
 * Interface for Items that can be displayed in a TopList.
 * {@link TopListItem} can be used if no special implementation is needed.
 * A TopListItem contains the data about an Item on the TopList and a
 * TopListElement additionally contains a Value that is displayed alongside and used for sorting.
 *
 * @author ColdStone37
 */
public interface TopListCompatibleItem {
	/**
	 * Gets the main text to display on the TopList.
	 * @return main text to display
	 */
	public String getMainText();

	/**
	 * Gets the sub text that can provide additional information on the TopList.
	 * @return sub text to display
	 */
	public String getSubText();

	/**
	 * Gets whether the Item has a subtext.
	 * @return true if the item has a subtext
	 */
	public boolean hasSubText();

	/**
	 * Gets the Image that should be displayed alongside the main and subtext if available.
	 * @return Image to display alongside
	 */
	public ImageReference getImage();
}
