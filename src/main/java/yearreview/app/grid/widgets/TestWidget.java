package yearreview.app.grid.widgets;

import yearreview.app.util.xml.XmlNode;

import java.awt.*;
import java.time.Instant;

/**
 * Test Widget thats just a blue rectangle.
 *
 * @author ColdStone37
 */
public class TestWidget extends Widget {
	/**
	 * Constructs a TestWidget at a given position with a configuration (that isn't used).
	 *
	 * @param x x-position of the widget
	 * @param y y-position of the widget
	 * @param w width of the widget
	 * @param h height of the widget
	 * @param c configuration
	 */
	public TestWidget(float x, float y, float w, float h, XmlNode c) {
		super(x, y, w, h);
	}

	/**
	 * Renders the widget in it's local space (meaning that the coordinate system goes from (0, 0) to (w, h)).
	 *
	 * @param graphic graphic that gets rendered to
	 * @param time    time at which the widget is rendered
	 */
	@Override
	protected void renderLocalSpace(Graphics2D graphic, Instant time) {
		graphic.setColor(Color.BLUE);
		graphic.fillRect(0, 0, (int) w, (int) h);
	}
}