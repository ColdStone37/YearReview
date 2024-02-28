package yearreview.app.grid;

import yearreview.app.config.GlobalSettings;
import yearreview.app.grid.widgets.Widget;
import yearreview.app.grid.widgets.WidgetFactory;
import yearreview.app.util.xml.XmlNode;

import java.util.*;
import java.util.logging.*;

/**
 * Parses the configuration of the grid and creates the segments with according sizes.
 *
 * @author ColdStone37
 */
public class GridManager implements Iterable<Widget> {
	/**
	 * Horizontal scale used to position the nodes.
	 **/
	private final float xScale;
	/**
	 * Vertical scale used to position the nodes.
	 */
	private final float yScale;
	/**
	 * A List of all Widgets managed by this {@link GridManager}.
	 */
	private final List<Widget> widgets;

	private final static Logger logger = Logger.getLogger(GridManager.class.getName());

	/**
	 * Constructs a {@link GridManager} from a given configuration.
	 *
	 * @param gridConfig configuration of the grid
	 */
	public GridManager(XmlNode gridConfig) {
		// Calculate scaling values
		xScale = ((float) GlobalSettings.getRenderWidth() - GlobalSettings.getScaledGridOuterSpacing() * 2f + (float) GlobalSettings.getScaledGridInnerSpacing()) / (float) GlobalSettings.getGridWidth();
		yScale = ((float) GlobalSettings.getRenderHeight() - GlobalSettings.getScaledGridOuterSpacing() * 2f + (float) GlobalSettings.getScaledGridInnerSpacing()) / (float) GlobalSettings.getGridHeight();

		// Creating the widgets
		widgets = new ArrayList<>();
		for (XmlNode widgetConfig : gridConfig) {
			widgetConfig.assertAttributesExist("x", "y", "w", "h");
			int x = Integer.parseInt(widgetConfig.getAttributeByName("x"));
			int y = Integer.parseInt(widgetConfig.getAttributeByName("y"));
			int w = Integer.parseInt(widgetConfig.getAttributeByName("w"));
			int h = Integer.parseInt(widgetConfig.getAttributeByName("h"));

			// Transform the position from grid space to screen space
			WidgetPosition wPos = transform(x, y, w, h);

			// Add the widget
			Widget newWidget = WidgetFactory.getWidget(wPos.x, wPos.y, wPos.w, wPos.h, widgetConfig);
			if (newWidget == null)
				logger.log(Level.WARNING, "Widget couldn't be initialized, no Widget with name \"" + widgetConfig.getName() + "\" exists.");
			widgets.add(newWidget);
		}
	}

	/**
	 * Transforms widget coordinates from grid space to screen space.
	 *
	 * @param x x-position of the widget in grid space
	 * @param y y-position of the widget in grid space
	 * @param w width of the widget in grid space
	 * @param h height of the widget in grid space
	 * @return new dimensions bundled in {@link WidgetPosition}-object
	 */
	private WidgetPosition transform(int x, int y, int w, int h) {
		float x_f = x * xScale;
		float y_f = y * yScale;
		float w_f = (x + w) * xScale - x_f - GlobalSettings.getScaledGridInnerSpacing();
		float h_f = (y + h) * yScale - y_f - GlobalSettings.getScaledGridInnerSpacing();

		x_f += GlobalSettings.getScaledGridOuterSpacing();
		y_f += GlobalSettings.getScaledGridOuterSpacing();

		return new WidgetPosition(x_f, y_f, w_f, h_f);
	}

	/**
	 * Class to store the dimensions of a widget in screen space.
	 */
	private static class WidgetPosition {
		/**
		 * Position and size of the widget.
		 */
		final float x, y, w, h;

		/**
		 * Constructs a new Widget with the given position and size.
		 * @param x x-position
		 * @param y y-position
		 * @param w width
		 * @param h height
		 */
		protected WidgetPosition(float x, float y, float w, float h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}

	/**
	 * Gets an iterator for the widgets contained in the manager.
	 *
	 * @return iterator over all widgets
	 */
	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}
}