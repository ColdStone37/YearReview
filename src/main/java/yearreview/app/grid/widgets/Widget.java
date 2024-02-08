package yearreview.app.grid.widgets;

import yearreview.app.config.GlobalSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

import java.time.Instant;

/**
 * Abstract class that defines Methods that need to be implemented in every Widget.
 *
 * @author ColdStone37
 */
public abstract class Widget {
	/**
	 * x-position of the widget.
	 */
	private float x;
	/**
	 * y-position of the widget.
	 */
	private float y;
	/**
	 * Width of the widget.
	 */
	protected final float w;
	/**
	 * Height of the widget.
	 */
	protected final float h;
	/**
	 * Shape that is used to clip the graphic so only the local space can be rendered to.
	 */
	private final RoundRectangle2D.Float clipShape;

	/**
	 * Constructor for a GridSegment that initializes the position of the segment and the Shape for the {@link Graphics2D#clip}-function.
	 *
	 * @param x x-position of the widget
	 * @param y y-position of the widget
	 * @param w width of the widget
	 * @param h height of the widget
	 */
	public Widget(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		clipShape = new RoundRectangle2D.Float(0, 0, w, h, GlobalSettings.getScaledCornerRadius(), GlobalSettings.getScaledCornerRadius());
	}

	/**
	 * Changes the position of the widget.
	 *
	 * @param x new x-position
	 * @param y new y-position
	 */
	public final void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Converts the global space to the local space of the widget and sets the clip.
	 *
	 * @param graphic graphic to render to
	 * @param time    time at which to render the widget
	 */
	public final void renderGlobalSpace(Graphics2D graphic, Instant time) {
		AffineTransform prevTransform = graphic.getTransform();
		Shape prevClip = graphic.getClip();
		graphic.translate(x, y);
		graphic.clip(clipShape);
		renderLocalSpace(graphic, time);
		graphic.setTransform(prevTransform);
		graphic.setClip(prevClip);
	}

	/**
	 * Renders the widget in it's local space (meaning that the coordinate system goes from (0, 0) to (w, h)).
	 *
	 * @param graphic graphic to render to
	 * @param time    time to render the widget at
	 */
	protected abstract void renderLocalSpace(Graphics2D graphic, Instant time);
}