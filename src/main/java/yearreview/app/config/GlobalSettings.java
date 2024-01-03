package yearreview.app.config;

/**
 * A class that stores general settings for the video.
 * TODO: Create seperate class in config that reads in the settings from an XML-File.
 *
 * @author ColdStone37
 */
public abstract class GlobalSettings {
	/**
	 * Stores the width of the video
	 */
	private static int videoWidth = 1920;
	/**
	 * Stores the height of the video
	 */
	private static int videoHeight = 1080;
	/**
	 * Stores the framerate of the video
	 */
	private static int videoFramerate = 60;
	/**
	 * Stores the number of threads to use for rendering
	 */
	private static int renderThreads = 2;

	/**
	 * Sets the output video resolution.
	 *
	 * @param width  width of the video
	 * @param height height of the video
	 */
	protected static void setVideoResolution(int width, int height) {
		assert width > 0 && height > 0;
		videoWidth = width;
		videoHeight = height;
	}

	/**
	 * Sets the framerate of the output video.
	 *
	 * @param framerate framerate to use for the video
	 */
	protected static void setVideoFramerate(int framerate) {
		assert framerate > 0;
		videoFramerate = framerate;
	}

	/**
	 * Sets the number of threads to use for rendering.
	 *
	 * @param threads number of threads
	 */
	protected static void setRenderThreads(int threads) {
		assert threads > 0;
		renderThreads = threads;
	}

	/**
	 * Gets the pixel-width of the output video.
	 *
	 * @return pixel-width
	 */
	public static int getVideoWidth() {
		return videoWidth;
	}

	/**
	 * Gets the pixel-height of the output video.
	 *
	 * @return pixel-height
	 */
	public static int getVideoHeight() {
		return videoHeight;
	}

	/**
	 * Gets the framerate of the output video.
	 *
	 * @return framerate
	 */
	public static int getVideoFramerate() {
		return videoFramerate;
	}

	/**
	 * Gets the number of threads to use when rendering the video.
	 *
	 * @return number of threads
	 */
	public static int getRenderThreads() {
		return renderThreads;
	}
}