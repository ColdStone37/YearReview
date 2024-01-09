package yearreview.app.grid.segments;

import yearreview.app.config.GlobalSettings;

import java.awt.*;

public class TestSegment extends GridSegment {

	public TestSegment(float x, float y, float w, float h) {
		super(x * GlobalSettings.getSuperSampling(), y * GlobalSettings.getSuperSampling(), w * GlobalSettings.getSuperSampling(), h * GlobalSettings.getSuperSampling());
	}

	@Override
	protected void renderLocalSpace(Graphics2D graphic) {
		graphic.setColor(Color.BLUE);
		graphic.fillRect(0, 0, (int) w, (int) h);
	}
}