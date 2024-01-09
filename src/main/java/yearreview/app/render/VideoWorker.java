package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * A class that pipes the generated images from the {@link Renderer} into an ffmpeg-subprocess to render the video.
 *
 * @author ColdStone37
 */
public class VideoWorker {
	/**
	 * ffmpeg-subprocess used for creating the video-file.
	 */
	private final Process ffmpeg;
	/**
	 * Stream used to input raw frames into ffmpeg.
	 */
	private final OutputStream ffmpegInput;

	/**
	 * Default Constructor for a VideoWorker that starts the ffmpeg-subprocess.
	 */
	public VideoWorker() {
		String[] cmd = {"ffmpeg", "-r", "" + GlobalSettings.getVideoFramerate(), "-s", GlobalSettings.getVideoWidth() * GlobalSettings.getSuperSampling() + "x" + GlobalSettings.getVideoHeight() * GlobalSettings.getSuperSampling(), "-vcodec", "rawvideo", "-f", "rawvideo", "-pix_fmt", "bgr24", "-i", "pipe:0", "-vf", "scale=" + GlobalSettings.getVideoWidth() + ":-1", "-r", "60", "out.mp4"};
		Runtime rt = Runtime.getRuntime();
		try {
			ffmpeg = rt.exec(cmd);
			ffmpegInput = ffmpeg.getOutputStream();
		} catch (IOException e) {
			throw new Error("Could not create ffmpeg process. Are you sure that ffmpeg is installed?");
		}
	}

	/**
	 * Writes a {@link BufferedImage} to the {@link OutputStream} connected to ffmpeg.
	 *
	 * @param image image that should be encoded by ffmpeg. Must use {@link BufferedImage#TYPE_3BYTE_BGR}.
	 */
	public void writeFrame(BufferedImage image) {
		try {
			WritableRaster raster = image.getRaster();
			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
			ffmpegInput.write(data.getData());
		} catch (IOException e) {
			throw new Error("IOException whilst piping image to ffmpeg.");
		}
	}

	/**
	 * Ends the ffmpeg-process.
	 */
	public void end() {
		try {
			ffmpegInput.flush();
			ffmpegInput.close();
		} catch (IOException e) {
			throw new Error("IOException whilst closing stream.");
		}
	}
}