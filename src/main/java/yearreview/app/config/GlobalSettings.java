package yearreview.app.config;

import io.jenetics.jpx.Length;

import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

/**
 * A class that stores general settings for the video.
 *
 * @author ColdStone37
 */
public abstract class GlobalSettings {
	/**
	 * Stores the path to the configuration file. (Used to construct relative paths)
	 */
	private static Path inputPath;
	/**
	 * Stores the name of the output video file. (Default: out.mp4)
	 */
	private static String outputFilename = "out.mp4";
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
	 * Stores the time at that the video should start. (No default value)
	 */
	private static Instant startTime;
	/**
	 * Stores the time at that the video should end. (No default value)
	 */
	private static Instant endTime;

	/**
	 * Background of the video. (Default: {@link Color#BLACK})
	 */
	private static Color backgroundColor = Color.BLACK;

	/**
	 * Radius of the corners of the {@link yearreview.app.grid.widgets.Widget widgets}. (Default: 10f)
	 */
	private static float cornerRadius = 10f;

	/**
	 * Width of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets}. (Default: 16)
	 */
	private static int gridWidth = 16;

	/**
	 * Height of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets}. (Default: 9)
	 */
	private static int gridHeight = 9;

	/**
	 * Spacing in pixels between two {@link yearreview.app.grid.widgets.Widget widgets} next to each other. (Default: 10)
	 */
	private static int gridInnerSpacing = 10;

	/**
	 * Spacing in pixels between the {@link yearreview.app.grid.widgets.Widget widgets} and the border of the video. (Default: 15)
	 */
	private static int gridOuterSpacing = 15;

	/**
	 * Minimum size for covers downloaded for {@link yearreview.app.data.sources.audio.database.AudioPiece AudioPieces}.
	 */
	private static int audioMinCoverResolution = 200;

	/**
	 * Unit used for all length. (Default: {@link Length.Unit#METER})
	 */
	private static Length.Unit lengthUnit = Length.Unit.KILOMETER;

	/**
	 * Duration of all Animations happening in the video. (Default: 750ms)
	 */
	private static Duration animationDuration = Duration.ofMillis(750);

	/**
	 * Sets the filename of the input file.
	 *
	 * @param filename name of the input file
	 */
	protected static void setInputFilename(String filename) {
		inputPath = new File(filename).toPath().getParent();
	}

	/**
	 * Sets the name of the output file.
	 *
	 * @param filename name that is used for the video file
	 */
	protected static void setOutputFilename(String filename) {
		outputFilename = filename;
	}

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
	 * Sets the output video width.
	 *
	 * @param width width of the video
	 */
	protected static void setVideoWidth(int width) {
		assert width > 0;
		videoWidth = width;
	}

	/**
	 * Sets the output video height.
	 *
	 * @param height height of the video
	 */
	protected static void setVideoHeight(int height) {
		assert height > 0;
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
	 * Sets the starting time of the video.
	 *
	 * @param time time at which the animation should start
	 */
	protected static void setVideoStart(Instant time) {
		startTime = time;
	}

	/**
	 * Sets the ending time of the video.
	 *
	 * @param time time at which the animation should end
	 */
	protected static void setVideoEnd(Instant time) {
		endTime = time;
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
	 * Sets the corner radius of the {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @param r radius of the corners in pixels
	 */
	protected static void setCornerRadius(float r) {
		cornerRadius = r;
	}

	/**
	 * Sets the size of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets} by the {@link yearreview.app.grid.GridManager manager}.
	 *
	 * @param width  width of the grid
	 * @param height height of the grid
	 */
	protected static void setGridSize(int width, int height) {
		assert width > 0 && height > 0;
		gridWidth = width;
		gridHeight = height;
	}

	/**
	 * Sets the width of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets} by the {@link yearreview.app.grid.GridManager manager}.
	 *
	 * @param width width of the grid
	 */
	protected static void setGridWidth(int width) {
		assert width > 0;
		gridWidth = width;
	}

	/**
	 * Sets the height of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets} by the {@link yearreview.app.grid.GridManager manager}.
	 *
	 * @param height height of the grid
	 */
	protected static void setGridHeight(int height) {
		assert height > 0;
		gridHeight = height;
	}

	/**
	 * Sets the inner spacing between two {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @param spacing spacing in pixels
	 */
	protected static void setGridInnerSpacing(int spacing) {
		assert spacing >= 0;
		gridInnerSpacing = spacing;
	}

	/**
	 * Sets the outer spacing between the {@link yearreview.app.grid.widgets.Widget widgets} and the border of the video.
	 *
	 * @param spacing spacing in pixels
	 */
	protected static void setGridOuterSpacing(int spacing) {
		assert spacing >= 0;
		gridOuterSpacing = spacing;
	}

	/**
	 * Sets the minimum resolution for covers downloaded for {@link yearreview.app.data.sources.audio.database.AudioPiece AudioPieces}
	 *
	 * @param resolution minimum resolution
	 */
	protected static void setAudioMinCoverResolution(int resolution) {
		audioMinCoverResolution = resolution;
	}

	/**
	 * Sets the length unit to use.
	 * @param unit unit to use
	 */
	protected static void setLengthUnit(Length.Unit unit) {
		lengthUnit = unit;
	}

	/**
	 * Sets the Duration of all animations.
	 * @param d  duration of the animations
	 */
	protected static void setAnimationDuration(Duration d) {
		animationDuration = d;
	}

	/**
	 * Gets a relative path from the config file.
	 *
	 * @param file file to construct a relative path for
	 * @return file relative from config path
	 */
	public static File getRelativePath(String file) {
		return inputPath.resolve(file).toFile();
	}

	/**
	 * Gets the name of the output file of the video.
	 *
	 * @return name to be used for the output file
	 */
	public static String getOutputFilename() {
		return outputFilename;
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
	 * Gets the starting time of the video.
	 *
	 * @return time at which the animation should start
	 */
	public static Instant getStartTime() {
		return startTime;
	}

	/**
	 * Gets the ending time of the video.
	 *
	 * @return time at which the animation should end
	 */
	public static Instant getEndTime() {
		return endTime;
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
	 * Gets the corner radius of the {@link yearreview.app.grid.widgets.Widget widgets} automatically scaled by the supersapling resolution.
	 *
	 * @return scaled corner radius
	 */
	public static float getScaledCornerRadius() {
		return cornerRadius * (float) superSampling;
	}

	/**
	 * Gets the width of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @return width of the grid
	 */
	public static int getGridWidth() {
		return gridWidth;
	}

	/**
	 * Gets the height of the grid used for placing the {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @return height of the grid
	 */
	public static int getGridHeight() {
		return gridHeight;
	}

	/**
	 * Gets the scaled spacing between two {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @return spacing in pixels
	 */
	public static int getScaledGridInnerSpacing() {
		return gridInnerSpacing * superSampling;
	}

	/**
	 * Gets the scaled spacing between {@link yearreview.app.grid.widgets.Widget widgets} and the border of the video.
	 *
	 * @return spacing in pixels
	 */
	public static int getScaledGridOuterSpacing() {
		return gridOuterSpacing * superSampling;
	}

	/**
	 * Gets the minimum resolution for covers to be downloaded for {@link yearreview.app.data.sources.audio.database.AudioPiece AudioPieces}.
	 *
	 * @return minimum resolution
	 */
	public static int getAudioMinCoverResolution() {
		return audioMinCoverResolution;
	}

	/**
	 * Gets the unit used for all lengths.
	 *
	 * @return length unit used
	 */
	public static Length.Unit getLengthUnit() {
		return lengthUnit;
	}

	/**
	 * Gets the duration of all animations.
	 * @return duration of animations
	 */
	public static Duration getAnimationDuration() {
		return animationDuration;
	}
}