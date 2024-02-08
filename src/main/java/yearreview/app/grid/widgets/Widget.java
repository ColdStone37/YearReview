package yearreview.app.grid.widgets;

import yearreview.app.config.GlobalSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;


/**
 * Abstract class that defines Methods that need to be implemented in every Segment
 */
public abstract class Widget {
	private float x, y;
	protected final float w, h;
	private final RoundRectangle2D.Float clipShape;

	/**
	 * Constructor for a GridSegment that initializes the position of the segment and the Shape for the {@link Graphics2D#clip}-function.
	 */
	public Widget(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		clipShape = new RoundRectangle2D.Float(0, 0, w, h, GlobalSettings.getScaledCornerRadius(), GlobalSettings.getScaledCornerRadius());
	}

	public final void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public final void renderGlobalSpace(Graphics2D graphic) {
		AffineTransform prevTransform = graphic.getTransform();
		Shape prevClip = graphic.getClip();
		graphic.translate(x, y);
		graphic.clip(clipShape);
		renderLocalSpace(graphic);
		graphic.setTransform(prevTransform);
		graphic.setClip(prevClip);
	}

	protected abstract void renderLocalSpace(Graphics2D graphic);
}