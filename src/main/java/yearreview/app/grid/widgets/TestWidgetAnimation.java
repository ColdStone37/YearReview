package yearreview.app.grid.widgets;

import yearreview.app.animation.AnimationCurve;
import yearreview.app.util.xml.XmlNode;

import java.awt.*;
import java.time.Instant;

/**
 * Test Widget that's a blue rectangle with four green dots animated using different {@link AnimationCurve AnimationCurves}..
 *
 * @author ColdStone37
 */
public class TestWidgetAnimation extends Widget {
	public final static int ANIMATION_TIME = 200;
	public final static int ANIMATION_WAIT = 30;
	private int frameCount = 0;

	private final static Font FONT = new Font("Monospaced", Font.PLAIN, 15);

	/**
	 * Constructs a TestWidgetAnimation at a given position with a configuration (that isn't used).
	 *
	 * @param x x-position of the widget
	 * @param y y-position of the widget
	 * @param w width of the widget
	 * @param h height of the widget
	 * @param c configuration
	 */
	public TestWidgetAnimation(float x, float y, float w, float h, XmlNode c) {
		super(x, y, w, h);
	}

	/**
	 * Renders the widget in its local space (meaning that the coordinate system goes from (0, 0) to (w, h)).
	 *
	 * @param g       graphic that gets rendered to
	 * @param time    time at which the widget is rendered
	 */
	@Override
	protected void renderLocalSpace(Graphics2D g, Instant time) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, (int) w, (int) h);

		float curvePosition = (float) Math.min(frameCount % (ANIMATION_TIME+ANIMATION_WAIT), ANIMATION_TIME) / (float) ANIMATION_TIME;

		// Render dots
		g.setColor(Color.GREEN);
		g.fillOval((int)(w/5f-h/16f), (int)(h/4f-h/16f) + (int)(AnimationCurve.EASE_IN_OUT.sampleCurve(curvePosition) * h/2), (int)h/8, (int)h/8);
		g.fillOval((int)((2f*w)/5f-h/16f), (int)(h/4f-h/16f) + (int)(AnimationCurve.EASE_IN.sampleCurve(curvePosition) * h/2), (int)h/8, (int)h/8);
		g.fillOval((int)((3f*w)/5f-h/16f), (int)(h/4f-h/16f) + (int)(AnimationCurve.EASE_OUT.sampleCurve(curvePosition) * h/2), (int)h/8, (int)h/8);
		g.fillOval((int)((4f*w)/5f-h/16f), (int)(h/4f-h/16f) + (int)(AnimationCurve.LINEAR.sampleCurve(curvePosition) * h/2), (int)h/8, (int)h/8);

		// Render text descriptions
		g.setColor(Color.BLACK);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(FONT);
		g.translate(w/5f, h/6f);
		g.rotate(-Math.PI / 2f);
		g.drawString("EASE IN/OUT", 0f, 0f);
		g.drawString("EASE IN", 0f, w/5f);
		g.drawString("EASE OUT", 0f, (2f*w)/5f);
		g.drawString("LINEAR", 0f, (3f*w)/5f);
		frameCount++;
	}
}