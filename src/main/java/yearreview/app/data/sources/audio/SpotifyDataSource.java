package yearreview.app.data.sources.audio;

import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;
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
import yearreview.app.data.sources.audio.database.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SpotifyDataSource extends DataSource {
	private String dataFolder;
	private ClientCredentials credentials;
	private SpotifyApi api;

	private final static int MAX_REQUEST_BATCH = 50;

	private final AudioDatabase database;

	private Map<String, RawTrack> toBeRequested;
	private List<RawEvent> eventBuffer;

	public SpotifyDataSource(ConfigNode config) {
		super(config);
		database = new AudioDatabase();
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
				System.out.println("Started parsing " + listOfFiles[i]);
				parseFile(listOfFiles[i], start, end);
			}
		}
		getArtistCovers();

		System.out.println("Added " + database.getFilteredData(AudioData.Type.Song).size() + " songs");
		System.out.println("Added " + database.getFilteredData(AudioData.Type.Artist).size() + " artists");
		System.out.println("Added " + database.getFilteredData(AudioData.Type.Album).size() + " albums");
		System.out.println("Listened to music for " + database.getTotalDuration().toMinutes() + " hours");
	}

	private void getArtistCovers() {
		List<AudioData> artists = database.getFilteredData(AudioData.Type.Artist);
		while (!artists.isEmpty()) {
			int n = Math.min(MAX_REQUEST_BATCH, artists.size());
			String[] artistIds = new String[n];
			for (int i = 0; i < n; i++)
				artistIds[i] = artists.get(i).id.split(":")[2];
			Artist[] apiArtists = runRequest(api.getSeveralArtists(artistIds).build());
			for (int i = 0; i < n; i++)
				artists.get(i).addCover(selectPreferredCoverUrl(apiArtists[i].getImages()));
			if (n < MAX_REQUEST_BATCH)
				break;
			artists = artists.subList(MAX_REQUEST_BATCH, artists.size());
		}
	}

	private void parseFile(File f, Instant start, Instant end) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
			int c_i;
			int layer = 0;
			boolean inString = false;
			String buffer = "";
			String keyValue = "";
			boolean isValid = false;
			boolean lastWasBackSlash = false;

			Instant eventTime = null;
			String trackId = "";
			String episodeId = "";
			int listeningTime = 0;

			toBeRequested = new TreeMap<String, RawTrack>();
			eventBuffer = new ArrayList<RawEvent>();

			while ((c_i = reader.read()) != -1) {
				char c = (char) c_i;
				if (inString) {
					switch (c) {
						case '"':
							if (lastWasBackSlash) {
								buffer += '"';
								lastWasBackSlash = false;
							} else {
								inString = false;
							}
							break;
						case '\\':
							lastWasBackSlash = true;
							break;
						default:
							buffer += c;
							lastWasBackSlash = false;
					}
				} else {
					switch (c) {
						case '"':
							inString = true;
							continue;
						case '[':
						case '{':
							layer++;
							continue;
						case ']':
							layer--;
							continue;
						case '}':
							layer--;
							if (layer == 1) {
								if (trackId != null) {
									addTrack(trackId, eventTime, listeningTime);
								}
							}
							continue;
						case ',':
							switch (keyValue) {
								case "ts":
									eventTime = Instant.parse(buffer);
									isValid = eventTime.isBefore(end) && eventTime.isAfter(start);
									break;
								case "spotify_track_uri":
									if (isValid && !buffer.isEmpty() && !buffer.equals("null")) {
										trackId = buffer;
									} else {
										trackId = null;
									}
									break;
								case "spotify_episode_uri":
									if (isValid && !buffer.isEmpty() && !buffer.equals("null")) {
										episodeId = buffer;
									} else {
										episodeId = null;
									}
									break;
								case "ms_played":
									if (isValid)
										listeningTime = Integer.parseInt(buffer);
									break;
								case "username":
								case "platform":
								case "conn_country":
								case "ip_addr_decrypted":
								case "user_agent_decrypted":
								case "master_metadata_track_name":
								case "master_metadata_album_artist_name":
								case "master_metadata_album_album_name":
								case "episode_name":
								case "episode_show_name":
								case "reason_start":
								case "reason_end":
								case "shuffle":
								case "skipped":
								case "offline":
								case "offline_timestamp":
								case "incognito_mode":
									break;
								default:
									System.out.println("Weird key: " + keyValue);
							}
							buffer = "";
							continue;
						case ':':
							keyValue = buffer;
							buffer = "";
							break;
						default:
							buffer += c;
					}
				}
			}
			addTracks(new ArrayList<RawTrack>(toBeRequested.values()));
			toBeRequested.clear();
			reader.close();
		} catch (IOException e) {
			throw new Error("Couldn't load file " + f);
		}
	}

	private void addTrack(String trackId, Instant eventTime, int duration) {
		// Process event
		AudioData p = database.getDataById(trackId);

		if (p == null) {
			RawTrack rawTrack = toBeRequested.get(trackId);
			if (rawTrack == null) {
				RawTrack newTrack = new RawTrack(trackId);

				toBeRequested.put(trackId, newTrack);
				eventBuffer.add(new RawEvent(trackId, eventTime, duration));
				if (toBeRequested.size() >= MAX_REQUEST_BATCH) {
					addTracks(new ArrayList<RawTrack>(toBeRequested.values()));
					toBeRequested.clear();
				}
			} else {
				eventBuffer.add(new RawEvent(trackId, eventTime, duration));
			}
		} else {
			ListeningEvent event = new ListeningEvent((AudioPiece) p, eventTime, Duration.ofMillis(duration));
			database.insertEvent(event);
		}
	}

	private class RawTrack {
		public final String id;

		RawTrack(String id) {
			this.id = id;
		}
	}

	private class RawEvent {
		public final String pieceId;
		public final Instant time;
		public final int duration;

		RawEvent(String pieceId, Instant time, int duration) {
			this.pieceId = pieceId;
			this.time = time;
			this.duration = duration;
		}
	}

	private void addTracks(List<RawTrack> tracks) {
		List<AudioPiece> pieces = new ArrayList<AudioPiece>();
		if (tracks.isEmpty())
			return;

		String[] ids = new String[tracks.size()];

		for (int i = 0; i < tracks.size(); i++)
			ids[i] = tracks.get(i).id.split(":")[2];

		Track[] apiTracks = runRequest(api.getSeveralTracks(ids).build());

		for (Track t : apiTracks) {
			URL albumCoverUrl = selectPreferredCoverUrl(t.getAlbum().getImages());
			List<AudioData> trackData = new ArrayList<AudioData>();
			String albumId = "spotify:album:" + t.getAlbum().getId();
			AudioData album = database.getDataById(albumId);
			if (album == null) {
				album = new AudioData(albumId, t.getAlbum().getName(), AudioData.Type.Album, albumCoverUrl);
				database.insertData(album);
			}
			trackData.add(album);

			for (ArtistSimplified a : t.getArtists()) {
				String artistId = "spotify:artist:" + a.getId();
				AudioData artist = database.getDataById(artistId);
				if (artist == null) {
					artist = new AudioData(artistId, a.getName(), AudioData.Type.Artist);
					database.insertData(artist);
				}
				trackData.add(artist);
			}

			database.insertData(new Song("spotify:track:" + t.getId(), t.getName(), albumCoverUrl, trackData));
		}

		List<RawEvent> newBuffer = new ArrayList<RawEvent>();
		for (RawEvent e : eventBuffer) {
			if (database.hasData(e.pieceId)) {
				database.insertEvent(new ListeningEvent((AudioPiece) database.getDataById(e.pieceId), e.time, Duration.ofMillis(e.duration)));
			} else {
				newBuffer.add(e);
			}
		}
		eventBuffer = newBuffer;
	}

	private URL selectPreferredCoverUrl(Image[] images) {
		int res = GlobalSettings.getAudioMinCoverResolution();
		Image img = null;
		for (Image i : images)
			if (i.getWidth() >= res && i.getWidth().equals(i.getHeight()) && (img == null || img.getWidth() > i.getWidth()))
				img = i;
		if (img == null)
			return null;
		try {
			return new URL(img.getUrl());
		} catch (MalformedURLException e) {
			return null;
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