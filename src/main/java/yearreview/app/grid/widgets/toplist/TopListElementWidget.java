package yearreview.app.grid.widgets.toplist;

import yearreview.app.config.GlobalSettings;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.grid.widgets.AnimatedWidget;

import java.awt.*;
import java.time.Instant;

public class TopListElementWidget extends AnimatedWidget {

	/**
	 * Constructor for a GridSegment that initializes the position of the segment and the Shape for the {@link Graphics2D#clip}-function.
	 *
	 * @param x x-position of the widget
	 * @param y y-position of the widget
	 * @param w width of the widget
	 * @param h height of the widget
	 */
	public TopListElementWidget(float x, float y, float w, float h, TopListElement element) {
		super(x, y, w, h);
	}

	/**
	 * Renders the widget in its local space (meaning that the coordinate system goes from (0, 0) to (w, h)).
	 *
	 * @param g    graphic to render to
	 * @param time time to render the widget at
	 */
	@Override
	protected void renderLocalSpace(Graphics2D g, Instant time) {
		// Background
		g.setColor(GlobalSettings.getAccentColor2());
		g.fillRect(0, 0, (int) w, (int) h);
	}
}
