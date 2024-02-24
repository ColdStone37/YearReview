package yearreview.app.grid.widgets;

import yearreview.app.util.xml.XmlNode;

/**
 * Factory for Widget objects.
 *
 * @author ColdStone37
 */
public abstract class WidgetFactory {
	/**
	 * Creates a widget from positional data and a configuration.
	 *
	 * @param x      x-position of the widget
	 * @param y      y-position of the widget
	 * @param w      width of the widget
	 * @param h      height of the widget
	 * @param config configuration of the widget
	 * @return requested widget or null if the configuration is invalid
	 */
	public static Widget getWidget(float x, float y, float w, float h, XmlNode config) {
		switch (config.getName()) {
			case "Test":
				return new TestWidget(x, y, w, h, config);
			case "TestAnimation":
				return new TestWidgetAnimation(x, y, w, h, config);
		}
		return null;
	}
}