package yearreview.app;

import yearreview.app.data.DataManager;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.data.processor.toplist.TopListGenerator;
import yearreview.app.data.sources.audio.AudioDataSource;
import yearreview.app.data.sources.audio.database.AudioData;
import yearreview.app.data.sources.audio.database.AudioTopListAdapter;
import yearreview.app.grid.GridManager;
import yearreview.app.render.Renderer;
import yearreview.app.config.ConfigParser;
import yearreview.app.util.value.DurationValue;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

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
		DataManager data = new DataManager(c.getDataSourcesSettings());
		data.loadData();
		TopListGenerator listGen = new TopListGenerator(((AudioDataSource) data.getSourceByTag("audio")).getTopListAdapter(AudioData.Type.Song), 50);
		List<TopListElement> l = listGen.getTopList(Instant.now());
		for(TopListElement elem:l){
			System.out.println(elem.getValue().toString() + " - " + elem.getItem().getMainText());
		}
		//Renderer r = new Renderer(grid);
		//r.renderVideo();
	}
}