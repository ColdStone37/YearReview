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
		System.out.println("Hello World!");

		ConfigParser c = new ConfigParser(new File("./test/config.xml"));


		Renderer r = new Renderer();
		r.renderVideo();
	}
}