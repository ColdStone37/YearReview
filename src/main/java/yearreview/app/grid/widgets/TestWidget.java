package yearreview.app.grid.widgets;

import yearreview.app.config.GlobalSettings;

import java.awt.*;

public class TestWidget extends Widget {

	public TestWidget(float x, float y, float w, float h) {
		super(x * GlobalSettings.getSuperSampling(), y * GlobalSettings.getSuperSampling(), w * GlobalSettings.getSuperSampling(), h * GlobalSettings.getSuperSampling());
	}

	@Override
	protected void renderLocalSpace(Graphics2D graphic) {
		graphic.setColor(Color.BLUE);
		graphic.fillRect(0, 0, (int) w, (int) h);
	}
}