package yearreview.app.render;

import yearreview.app.config.GlobalSettings;
import yearreview.app.grid.segments.TestSegment;

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
		renderWidth = GlobalSettings.getRenderWidth();
		renderHeight = GlobalSettings.getRenderHeight();
		renderingSurface = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_3BYTE_BGR);
		graphics = renderingSurface.createGraphics();
		graphics.setBackground(GlobalSettings.getBackgroundColor());
	}

	/**
	 * Renders the video and saves it to a file.
	 */
	public void renderVideo() {
		VideoWorker v = new VideoWorker();
		for (int i = 0; i < 500; i++) {
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

		graphics.fillRect(frameIndex % renderWidth, frameIndex % renderHeight, 50, 50);
		TestSegment t1 = new TestSegment(10, 10, 200, 200);
		TestSegment t2 = new TestSegment(220, 0, 200, 1080);
		t1.setPosition(10, frameIndex + 10);
		t1.renderGlobalSpace(graphics);
		t2.renderGlobalSpace(graphics);
	}

	/**
	 * Resets the {@link Graphics2D} to a blank frame.
	 */
	private void drawBackground() {
		graphics.clearRect(0, 0, renderWidth, renderHeight);
	}
}