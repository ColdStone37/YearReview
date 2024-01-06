package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class that manages the renderprocess by generating frames and allowing the to get next frame for rendering.
 *
 * @author ColdStone37
 */
public class Renderer {
	private final int renderWidth, renderHeight;
	private final BufferedImage renderingSurface;
	private final Graphics2D graphics;

	public Renderer() {
		renderWidth = GlobalSettings.getVideoWidth() * GlobalSettings.getSuperSampling();
		renderHeight = GlobalSettings.getVideoHeight() * GlobalSettings.getSuperSampling();
		renderingSurface = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_RGB);
		graphics = renderingSurface.createGraphics();
		graphics.setBackground(GlobalSettings.getBackgroundColor());
	}

	public void renderVideo() {
		VideoWorker v = new VideoWorker();
		renderFrame();
		for (int i = 0; i < 100; i++) {
			drawBackground();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(i * 10, 0, 100, 100);
			v.writeFrame(renderingSurface);
		}
	}

	private void renderFrame() {
		drawBackground();
	}

	private void drawBackground() {
		graphics.clearRect(0, 0, renderWidth, renderHeight);
	}
}