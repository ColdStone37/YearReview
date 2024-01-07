package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * A class that pipes the generated images from the  into ffmpeg to render the video.
 *
 * @author ColdStone37
 */
public class VideoWorker {
	private final Process ffmpeg;
	private final OutputStream ffmpegInput;
	private final InputStream ffmpegErrors;

	public VideoWorker() {
		String[] cmd = {"ffmpeg", "-r", "" + GlobalSettings.getVideoFramerate(), "-s", GlobalSettings.getVideoWidth() + "x" + GlobalSettings.getVideoHeight(), "-vcodec", "rawvideo", "-f", "rawvideo", "-pix_fmt", "bgr24", "-i", "pipe:0", "-r", "60", "out.mp4"};
		Runtime rt = Runtime.getRuntime();
		try {
			ffmpeg = rt.exec(cmd);
			ffmpegErrors = ffmpeg.getInputStream();
			ffmpegInput = ffmpeg.getOutputStream();
		} catch (IOException e) {
			System.out.println(e);
			throw new Error("Could not create ffmpeg process. Are you sure that ffmpeg is installed?");
		}
	}

	public void writeFrame(BufferedImage image) {
		try {
			WritableRaster raster = image.getRaster();
			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
			ffmpegInput.write(data.getData());
		} catch (IOException e) {
			System.out.println(e);
			throw new Error("IOException whilst piping image to ffmpeg.");
		}
	}

	public void end() {
		try {
			ffmpegInput.close();
		} catch (IOException e) {
			throw new Error("IOException whilst closing stream.");
		}
	}
}