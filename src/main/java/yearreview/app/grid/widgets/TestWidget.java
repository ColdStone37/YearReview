package yearreview.app.grid.widgets;

import yearreview.app.config.ConfigNode;

import java.awt.*;
import java.time.Instant;

public class TestWidget extends Widget {

	public TestWidget(float x, float y, float w, float h, ConfigNode c) {
		super(x, y, w, h);
	}

	@Override
	protected void renderLocalSpace(Graphics2D graphic, Instant time) {
		graphic.setColor(Color.BLUE);
		graphic.fillRect(0, 0, (int) w, (int) h);
	}
}