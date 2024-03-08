package yearreview.app;

import yearreview.app.data.DataManager;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.data.processor.toplist.TopListGenerator;
import yearreview.app.grid.GridManager;
import yearreview.app.render.Renderer;
import yearreview.app.config.ConfigParser;
import yearreview.app.util.value.ValueType;

import java.time.Instant;
import java.util.List;
import java.util.logging.*;

/**
 * Main Application
 */
public class App {
	/**
	 * Renders the video.
	 */
	public static void main(String[] args) {
		Logger global = Logger.getGlobal();
		global.setLevel(Level.FINEST);
		ConfigParser c = new ConfigParser(args);
		DataManager data = new DataManager(c.getDataSourcesSettings(), c.getDataProcessorsSettings());
		data.loadData();
		GridManager grid = new GridManager(c.getWidgetSettings(), data);
		Renderer r = new Renderer(grid);
		r.renderVideo();
	}
}