package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A class that pipes the generated images from the  into ffmpeg to render the video.
 *
 * @author ColdStone37
 */
public class VideoWorker {
	private final Process ffmpeg;
	private final OutputStream ffmpegInput;

	public VideoWorker() {
		String[] cmd = {"ffmpeg", "-f", "image2pipe", "-codec", "bmp", "-i", "pipe:0", "out.mp4"};
		Runtime rt = Runtime.getRuntime();
		try {
			ffmpeg = rt.exec(cmd);
			ffmpegInput = ffmpeg.getOutputStream();
		} catch (IOException e) {
			throw new Error("Could not create ffmpeg process. Are you sure that ffmpeg is installed?");
		}
	}

	public void writeFrame(BufferedImage image) {
		try {
			ImageIO.write(image, "BMP", ffmpegInput);
		} catch (IOException e) {
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