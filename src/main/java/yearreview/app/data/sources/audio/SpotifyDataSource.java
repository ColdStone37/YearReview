package yearreview.app.data.sources.audio;

import se.michaelthelin.spotify.requests.data.AbstractDataRequest;
import yearreview.app.config.ConfigNode;
import yearreview.app.config.GlobalSettings;
import yearreview.app.data.sources.DataSource;

import se.michaelthelin.spotify.*;
import se.michaelthelin.spotify.model_objects.specification.Track;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class SpotifyDataSource extends DataSource {
	private String dataFolder;
	private ClientCredentials credentials;
	private SpotifyApi api;

	public SpotifyDataSource(ConfigNode config) {
		super(config);
	}

	@Override
	public void parseConfig(ConfigNode config) {
		config.assertChildNodesExist("Path", "Api");
		dataFolder = config.getChildContent("Path");
		ConfigNode apiConfig = config.getChildByName("Api");
		apiConfig.assertChildNodesExist("ClientId", "ClientSecret", "RedirectUri");
		String clientId = apiConfig.getChildContent("ClientId");
		String clientSecret = apiConfig.getChildContent("ClientSecret");
		URI redirectUri = SpotifyHttpManager.makeUri(apiConfig.getChildContent("RedirectUri"));
		api = new SpotifyApi.Builder()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.setRedirectUri(redirectUri)
				.build();
		try {
			ClientCredentialsRequest ccr = api.clientCredentials().build();
			credentials = ccr.execute();
			api.setAccessToken(credentials.getAccessToken());
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			throw new Error("Error whilst connecting to Spotify API: " + e);
		}
	}

	@Override
	public void loadData(Instant start, Instant end) {
		File folder = GlobalSettings.getRelativePath(dataFolder);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				parseFile(listOfFiles[i], start, end);
			}
		}
	}

	private void parseFile(File f, Instant start, Instant end) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
			int c_i;
			int layer = 0;
			boolean inString = false;
			String buffer = "";
			boolean key = true;
			String keyValue = "";
			boolean isValid = false;

			Instant eventTime;
			String trackId = "";
			int listeningTime = 0;

			long eventCount = 0;
			while ((c_i = reader.read()) != -1) {
				char c = (char) c_i;
				switch (c) {
					case '"':
						inString = !inString;
						continue;
					case '{':
						if (!inString) {
							layer++;
						} else {
							buffer += '{';
						}
						continue;
					case '}':
						if (!inString) {
							layer--;
							if (layer == 0) {
								if (isValid) {
									eventCount++;
									System.out.println("Found event: " + trackId + " for " + listeningTime + "ms");
								}
							}
						} else {
							buffer += '}';
						}
						continue;
					case ',':
						if (!inString) {
							switch (keyValue) {
								case "ts":
									eventTime = Instant.parse(buffer);
									isValid = eventTime.isBefore(end) && eventTime.isAfter(start);
									break;
								case "spotify_track_uri":
									if (isValid && !buffer.isEmpty() && !buffer.equals("null")) {
										trackId = buffer;
									} else {
										isValid = false;
									}
									break;
								case "ms_played":
									if (isValid)
										listeningTime = Integer.parseInt(buffer);
									break;
							}
							buffer = "";
						} else {
							buffer += ',';
						}
						continue;
					case ':':
						if (!inString) {
							key = false;
							keyValue = buffer;
							buffer = "";
						} else {
							buffer += ':';
						}
						break;
					default:
						buffer += c;
				}
			}
			System.out.println(eventCount);
		} catch (
				IOException e) {
			throw new Error("Couldn't load file " + f);
		}
	}

	public <T> T runRequest(AbstractDataRequest<T> request) {
		try {
			if (credentials.getExpiresIn() < 60) {
				ClientCredentialsRequest ccr = api.clientCredentials().build();
				credentials = ccr.execute();
				api.setAccessToken(credentials.getAccessToken());
			}
			return request.execute();
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			throw new Error("Error whilst requesting data from Spotify: " + e);
		}
	}
}