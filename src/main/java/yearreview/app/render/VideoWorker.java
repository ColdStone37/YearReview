package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.logging.*;

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
	private final static Logger logger = Logger.getLogger(VideoWorker.class.getName());

	/**
	 * Default Constructor for a VideoWorker that starts the ffmpeg-subprocess.
	 */
	public VideoWorker() {
		String[] cmd = {"ffmpeg", "-r", "" + GlobalSettings.getVideoFramerate(), "-s", GlobalSettings.getVideoWidth() * GlobalSettings.getSuperSampling() + "x" + GlobalSettings.getVideoHeight() * GlobalSettings.getSuperSampling(), "-vcodec", "rawvideo", "-f", "rawvideo", "-pix_fmt", "bgr24", "-i", "pipe:0", "-vf", "scale=" + GlobalSettings.getVideoWidth() + ":-1", "-r", "60", GlobalSettings.getOutputFilename()};
		Runtime rt = Runtime.getRuntime();
		try {
			ffmpeg = rt.exec(cmd);
			ffmpegInput = ffmpeg.getOutputStream();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Couldn't create ffmpeg-process. Make sure you have added ffmpeg to PATH.", e);
			throw new Error(e);
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
			logger.log(Level.SEVERE, "Couldn't write frame to ffmpeg.", e);
			throw new Error(e);
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
			logger.log(Level.WARNING, "Couldn't close OutputStream to ffmpeg.", e);
		}
	}
}