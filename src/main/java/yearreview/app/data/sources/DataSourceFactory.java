package yearreview.app.data.sources;

import yearreview.app.config.ConfigNode;
import yearreview.app.data.sources.audio.SpotifyDataSource;

public abstract class DataSourceFactory {
	public static DataSource getDataSource(ConfigNode config) {
		switch (config.getName()) {
			case "Spotify":
				return new SpotifyDataSource(config);
		}
		return null;
	}
}