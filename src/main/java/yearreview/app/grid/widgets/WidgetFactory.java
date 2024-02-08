package yearreview.app.grid.widgets;

import yearreview.app.config.ConfigNode;

public abstract class WidgetFactory {
	public static Widget getWidget(float x, float y, float w, float h, ConfigNode config) {
		switch (config.getName()) {
			case "Test":
				return new TestWidget(x, y, w, h, config);
		}
		System.out.println(config.getName() + " isn't valid");
		return null;
	}
}