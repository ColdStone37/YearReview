package yearreview.app.config;

import java.awt.Color;

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

	private static int superSampling = 1;

	private static Color backgroundColor = Color.BLACK;

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

	protected static void setSupersampling(int quality) {
		superSampling = quality;
	}

	protected static void setBackgroundColor(Color c) {
		backgroundColor = c;
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

	public static int getSuperSampling() {
		return superSampling;
	}

	public static Color getBackgroundColor() {
		return backgroundColor;
	}
}