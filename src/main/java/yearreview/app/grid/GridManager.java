package yearreview.app.grid;

import yearreview.app.config.ConfigNode;
import yearreview.app.config.GlobalSettings;
import yearreview.app.grid.widgets.Widget;
import yearreview.app.grid.widgets.WidgetFactory;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import java.util.*;

/**
 * Parses the configuration of the grid and creates the segments with according sizes.
 *
 * @author ColdStone37
 */
public class GridManager implements Iterable<Widget> {

	private final float xScale, yScale;

	private final List<Widget> widgets;

	public GridManager(ConfigNode gridConfig) {
		xScale = ((float) GlobalSettings.getRenderWidth() - GlobalSettings.getScaledGridOuterSpacing() * 2f + (float) GlobalSettings.getScaledGridInnerSpacing()) / (float) GlobalSettings.getGridWidth();
		yScale = ((float) GlobalSettings.getRenderHeight() - GlobalSettings.getScaledGridOuterSpacing() * 2f + (float) GlobalSettings.getScaledGridInnerSpacing()) / (float) GlobalSettings.getGridHeight();

		widgets = new ArrayList<Widget>();
		for (ConfigNode widgetConfig : gridConfig) {
			widgetConfig.assertAttributesExist("x", "y", "w", "h");
			int x = Integer.parseInt(widgetConfig.getAttributeByName("x"));
			int y = Integer.parseInt(widgetConfig.getAttributeByName("y"));
			int w = Integer.parseInt(widgetConfig.getAttributeByName("w"));
			int h = Integer.parseInt(widgetConfig.getAttributeByName("h"));

			WidgetPosition wPos = transform(x, y, w, h);

			widgets.add(WidgetFactory.getWidget(wPos.x, wPos.y, wPos.w, wPos.h, widgetConfig));
		}
	}

	private WidgetPosition transform(int x_i, int y_i, int w_i, int h_i) {
		float x = x_i * xScale;
		float y = y_i * yScale;
		float w = (x_i + w_i) * xScale - x - GlobalSettings.getScaledGridInnerSpacing();
		float h = (y_i + h_i) * yScale - y - GlobalSettings.getScaledGridInnerSpacing();

		x += GlobalSettings.getScaledGridOuterSpacing();
		y += GlobalSettings.getScaledGridOuterSpacing();

		return new WidgetPosition(x, y, w, h);
	}

	private static class WidgetPosition {
		final float x, y, w, h;

		protected WidgetPosition(float x, float y, float w, float h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}

	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}
}