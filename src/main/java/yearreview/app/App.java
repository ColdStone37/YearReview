package yearreview.app;

import yearreview.app.render.Renderer;
import yearreview.app.config.ConfigParser;

import java.io.File;

/**
 * Hello world!
 */
public class App {
	/**
	 * Default main function.
	 */
	public static void main(String[] args) {
		File f = new File(args[0]);
		ConfigParser c = new ConfigParser(f);
		Renderer r = new Renderer();
		r.renderVideo();
	}
}
