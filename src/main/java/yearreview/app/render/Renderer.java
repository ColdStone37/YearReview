package yearreview.app.render;

import yearreview.app.animation.AnimationManager;
import yearreview.app.config.GlobalSettings;
import yearreview.app.grid.GridManager;
import yearreview.app.grid.widgets.Widget;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.logging.*;

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
	 * The {@link GridManager} used to get the {@link Widget widgets}.
	 */
	private final GridManager grid;
	private final static Logger logger = Logger.getLogger(Renderer.class.getName());

	/**
	 * Default Constructor for a Renderer.
	 *
	 * @param grid manager to get the widgets from
	 */
	public Renderer(GridManager grid) {
		renderWidth = GlobalSettings.getRenderWidth();
		renderHeight = GlobalSettings.getRenderHeight();
		renderingSurface = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_3BYTE_BGR);
		graphics = renderingSurface.createGraphics();
		graphics.setBackground(GlobalSettings.getBackgroundColor());
		this.grid = grid;
	}

	/**
	 * Renders the video and saves it to a file.
	 */
	public void renderVideo() {
		VideoWorker v = new VideoWorker();
		logger.log(Level.INFO, "Started rendering video.");
		for (int i = 0; i < 500; i++) {
			renderFrame(i);
			v.writeFrame(renderingSurface);
			AnimationManager.updateAnimations(1000 / GlobalSettings.getVideoFramerate());
		}
		logger.log(Level.INFO, "Finished rendering video.");
		v.end();
	}

	/**
	 * Renders a single frame of the video.
	 *
	 * @param frameIndex index of the frame to be rendered
	 */
	private void renderFrame(int frameIndex) {
		drawBackground();

		for (Widget w : grid)
			w.renderGlobalSpace(graphics, Instant.EPOCH);
	}

	/**
	 * Resets the {@link Graphics2D} to a blank frame.
	 */
	private void drawBackground() {
		graphics.clearRect(0, 0, renderWidth, renderHeight);
	}
}