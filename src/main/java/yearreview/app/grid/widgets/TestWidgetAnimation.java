package yearreview.app.grid.widgets;

import yearreview.app.animation.AnimatedNumber;
import yearreview.app.animation.AnimationCurve;
import yearreview.app.util.xml.XmlNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

/**
 * Test Widget that's a blue rectangle with four green dots animated using different {@link AnimationCurve AnimationCurves}..
 *
 * @author ColdStone37
 */
public class TestWidgetAnimation extends Widget {
	public Duration ANIMATION_DURATION = Duration.ofMillis(750);
	public final static int ANIMATION_FRAME_COUNT = 90;
	private int frameCount = 0;
	private List<AnimatedNumber> animations;

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
		animations = new ArrayList<>(4);
		animations.add(new AnimatedNumber(0f, AnimationCurve.EASE_IN_OUT));
		animations.add(new AnimatedNumber(0f, AnimationCurve.EASE_IN));
		animations.add(new AnimatedNumber(0f, AnimationCurve.EASE_OUT));
		animations.add(new AnimatedNumber(0f, AnimationCurve.LINEAR));
	}

	/**
	 * Renders the widget in its local space (meaning that the coordinate system goes from (0, 0) to (w, h)).
	 *
	 * @param g       graphic that gets rendered to
	 * @param time    time at which the widget is rendered
	 */
	@Override
	protected void renderLocalSpace(Graphics2D g, Instant time) {
		// Animation
		if(frameCount % ANIMATION_FRAME_COUNT == 0) {
			if(frameCount % (ANIMATION_FRAME_COUNT * 2) == 0){
				for(AnimatedNumber num : animations)
					num.animateTo(h/2, ANIMATION_DURATION);
			} else {
				for(AnimatedNumber num : animations)
					num.animateTo(0f, ANIMATION_DURATION);
			}
		}

		// Background
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, (int) w, (int) h);

		// Render dots
		if(frameCount % (ANIMATION_FRAME_COUNT * 2) < ANIMATION_FRAME_COUNT) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}
		for(int i = 0; i < 4; i ++)
			g.fillOval((int)(((i+1)*w)/5f-h/16f), (int)(h/4f-h/16f) + (int)(animations.get(i).floatValue()), (int)h/8, (int)h/8);

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