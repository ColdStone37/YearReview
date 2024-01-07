package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class that manages the renderprocess by generating the frames and passing them to a {@link VideoWorker}.
 *
 * @author ColdStone37
 */
public class Renderer {
	/**
	 * Pixel-Width of the render-surface, calculated by {@link GlobalSettings#getVideoWidth() width}*{@link GlobalSettings#getSuperSampling() supersampling}.
	 */
	private final int renderWidth;

	/**
	 * Pixel-Height of the render-surface, calculated by {@link GlobalSettings#getVideoHeight() height}*{@link GlobalSettings#getSuperSampling() supersampling}.
	 */
	private final int renderHeight;
	/**
	 * The Image where the frames of the animation are drawn on.
	 */
	private final BufferedImage renderingSurface;
	/**
	 * The Graphics object used for rendering.
	 */
	private final Graphics2D graphics;

	/**
	 * Default Constructor for a Renderer.
	 */
	public Renderer() {
		renderWidth = GlobalSettings.getVideoWidth() * GlobalSettings.getSuperSampling();
		renderHeight = GlobalSettings.getVideoHeight() * GlobalSettings.getSuperSampling();
		renderingSurface = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_3BYTE_BGR);
		graphics = renderingSurface.createGraphics();
		graphics.setBackground(GlobalSettings.getBackgroundColor());
	}

	/**
	 * Renders the video and saves it to a file.
	 */
	public void renderVideo() {
		VideoWorker v = new VideoWorker();
		for (int i = 0; i < 10000; i++) {
			renderFrame(i);
			v.writeFrame(renderingSurface);
		}
		v.end();
	}

	/**
	 * Renders a single frame of the video.
	 *
	 * @param frameIndex index of the frame to be rendered
	 */
	private void renderFrame(int frameIndex) {
		drawBackground();
	}

	/**
	 * Resets the {@link Graphics2D} to a blank frame.
	 */
	private void drawBackground() {
		graphics.clearRect(0, 0, renderWidth, renderHeight);
	}
}