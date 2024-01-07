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
	 * Stores the width of the video. (Default: 1920)
	 */
	private static int videoWidth = 1920;
	/**
	 * Stores the height of the video. (Default: 1080)
	 */
	private static int videoHeight = 1080;
	/**
	 * Stores the framerate of the video. (Default: 60)
	 */
	private static int videoFramerate = 60;

	/**
	 * Resolution at which the frames will be rendered before downscaling. The render resolution will be videoWidth*superSampling x videoHeight*supersampling. 1 means no supersampling will be happening. (Default: 1)
	 */
	private static int superSampling = 1;

	/**
	 * Background of the video. (Default: {@link Color#BLACK})
	 */
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

	/**
	 * Sets the supersampling resolution of the video.
	 *
	 * @param quality quality of the supersampling
	 */
	protected static void setSupersampling(int quality) {
		superSampling = quality;
	}

	/**
	 * Sets the bacground color for the video.
	 *
	 * @param c background color
	 */
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

	/**
	 * Gets the supersampling-resolution.
	 *
	 * @return quality of supersampling
	 */
	public static int getSuperSampling() {
		return superSampling;
	}

	/**
	 * Gets the background-color of the video.
	 *
	 * @return background-color
	 */
	public static Color getBackgroundColor() {
		return backgroundColor;
	}
}