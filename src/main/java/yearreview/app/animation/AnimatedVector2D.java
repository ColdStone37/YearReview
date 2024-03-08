package yearreview.app.animation;

import java.time.Duration;

/**
 * A Vector that is animated using two {@link AnimatedNumber AnimatedNumbers} for the x- and y-values.
 *
 * @author ColdSotne37
 */
public class AnimatedVector2D {
	/**
	 * Animator for the x-value.
	 */
	private final AnimatedNumber x;
	/**
	 * Animator for the y-value.
	 */
	private final AnimatedNumber y;

	/**
	 * Constructs a new AnimatedVector2D from an initial position.
	 * @param x initial x-position of the AnimatedVector
	 * @param y initial y-position of the AnimatedVector
	 */
	public AnimatedVector2D(float x, float y) {
		this.x = new AnimatedNumber(x);
		this.y = new AnimatedNumber(y);
	}

	/**
	 * Constructs a new AnimatedVector2D from an initial position.
	 * @param initialVector initial position of the AnimatedVector
	 */
	public AnimatedVector2D(Vector2D initialVector) {
		x = new AnimatedNumber(initialVector.x);
		y = new AnimatedNumber(initialVector.y);
	}

	/**
	 * Constructs a new AnimatedVector2D from an initial position and a Curve to use for animation.
	 * @param initialVector initial position of the AnimatedVector
	 * @param defaultCurve default curve for all animations
	 */
	public AnimatedVector2D(Vector2D initialVector, AnimationCurve defaultCurve) {
		x = new AnimatedNumber(initialVector.x, defaultCurve);
		y = new AnimatedNumber(initialVector.y, defaultCurve);
	}

	/**
	 * Animates the Vector to a new position using the default Duration and Curve.
	 * @param x new x-position to animate to
	 * @param y new y-position to animate to
	 */
	public void animateTo(float x, float y) {
		this.x.animateTo(x);
		this.y.animateTo(y);
	}

	/**
	 * Animates the Vector to a new position using the default Duration and Curve.
	 * @param pos new position to animate to
	 */
	public void animateTo(Vector2D pos) {
		x.animateTo(pos.x);
		y.animateTo(pos.y);
	}

	/**
	 * Animates the Vector to a new position using the default Curve.
	 * @param pos new position to animate to
	 * @param d duration of the animation
	 */
	public void animateTo(Vector2D pos, Duration d){
		x.animateTo(pos.x, d);
		y.animateTo(pos.y, d);
	}

	/**
	 * Animates the Vector to a new position.
	 * @param pos new position to animate to
	 * @param c Curve to use for the animation
	 */
	public void animateTo(Vector2D pos, AnimationCurve c) {
		x.animateTo(pos.x, c);
		y.animateTo(pos.y, c);
	}

	/**
	 * Animates the Vector to a new position.
	 * @param pos new position to animate to
	 * @param d duration of the animation
	 * @param c Curve to use for the animation
	 */
	public void animateTo(Vector2D pos, Duration d, AnimationCurve c) {
		x.animateTo(pos.x, d, c);
		y.animateTo(pos.y, d, c);
	}

	/**
	 * Gets the vector at the current time in the animation.
	 * @return animated vector
	 */
	public Vector2D getVector(){
		return new Vector2D(x.floatValue(), y.floatValue());
	}

	/**
	 * Gets the current x-value of the vector.
	 * @return animated x-value
	 */
	public float getX() {
		return x.floatValue();
	}

	/**
	 * Gets the current y-value of the vector.
	 * @return animated y-value
	 */
	public float getY() {
		return y.floatValue();
	}
}
