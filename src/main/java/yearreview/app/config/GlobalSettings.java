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
	 * Radius of the corners of the {@link yearreview.app.grid.segments.GridSegment GridSegments}. (Default: 10f)
	 */
	private static float cornerRadius = 10f;

	/**
	 * Width of the grid used for placing the {@link yearreview.app.grid.segments.GridSegment segments}. (Default: 16)
	 */
	private static int gridWidth = 16;

	/**
	 * Height of the grid used for placing the {@link yearreview.app.grid.segments.GridSegment segments}. (Default: 9)
	 */
	private static int gridHeight = 9;

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
	 * Sets the background color for the video.
	 *
	 * @param c background color
	 */
	protected static void setBackgroundColor(Color c) {
		backgroundColor = c;
	}

	/**
	 * Sets the corner radius of the {@link yearreview.app.grid.segments.GridSegment GridSegments}.
	 *
	 * @param r radius of the corners in pixels
	 */
	protected static void setCornerRadius(float r) {
		cornerRadius = r;
	}

	/**
	 * Sets the size of the grid used for placing the {@link yearreview.app.grid.segments.GridSegment GridSegments} by the {@link yearreview.app.grid.GridManager manager}.
	 *
	 * @param w width of the grid
	 * @param h height of the grid
	 */
	protected static void setGridSize(int w, int h) {
		gridWidth = w;
		gridHeight = h;
	}

	/**
	 * Gets the width of the render target-resolution. (meaning videoWidth * supersampling)
	 *
	 * @return width of the render
	 */

	public static int getRenderWidth() {
		return videoWidth * superSampling;
	}

	/**
	 * Gets the height of the render target-resolution. (meaning videoHeight * supersampling)
	 *
	 * @return height of the render
	 */
	public static int getRenderHeight() {
		return videoHeight * superSampling;
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

	/**
	 * Gets the corner radius of the {@link yearreview.app.grid.segments.GridSegment GridSegements} automatically scaled by the supersapling resolution.
	 *
	 * @return scaled corner radius
	 */
	public static float getScaledCornerRadius() {
		return cornerRadius * (float) superSampling;
	}

	/**
	 * Gets the width of the grid used for placing the {@link yearreview.app.grid.segments.GridSegment GridSegements}.
	 *
	 * @return width of the grid
	 */
	public static int getGridWidth() {
		return gridWidth;
	}

	/**
	 * Gets the height of the grid used for placing the {@link yearreview.app.grid.segments.GridSegment GridSegements}.
	 *
	 * @return height of the grid
	 */
	public static int getGridHeight() {
		return gridHeight;
	}
}