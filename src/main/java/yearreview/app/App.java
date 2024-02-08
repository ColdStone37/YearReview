package yearreview.app;

import yearreview.app.grid.GridManager;
import yearreview.app.render.Renderer;
import yearreview.app.config.ConfigParser;

/**
 * Main Application
 */
public class App {
	/**
	 * Renders the video.
	 */
	public static void main(String[] args) {
		ConfigParser c = new ConfigParser(args);
		GridManager grid = new GridManager(c.getWidgetSettings());
		Renderer r = new Renderer(grid);
		r.renderVideo();
	}
}