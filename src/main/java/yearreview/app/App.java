package yearreview.app;

import yearreview.app.render.Renderer;

/**
 * Hello world!
 */
public class App {
	/**
	 * Default main function.
	 */
	public static void main(String[] args) {
		System.out.println("Hello World!");
		Renderer r = new Renderer();
		r.renderVideo();
	}
}