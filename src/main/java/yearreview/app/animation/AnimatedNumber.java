package yearreview.app.animation;

import yearreview.app.config.GlobalSettings;

import java.time.Duration;

/**
 * A Number that is animated using an AnimationCurve.
 * The animations are updated using the {@link AnimationManager}. Animations that have finished are automatically removed from the manager.
 *
 * @author ColdStone37
 */
public class AnimatedNumber extends Number {
	/**
	 * Current value of this number.
	 */
	private float value;
	/**
	 * Value at the start of the animation.
	 */
	private float animationStart;
	/**
	 * Value at the end of the animation.
	 */
	private float animationEnd;
	/**
	 * Curve used for the animation.
	 */
	private AnimationCurve curve;
	/**
	 * Default curve used for animations if no curve is specified.
	 */
	private final AnimationCurve defaultCurve;
	/**
	 * Total duration of the currently running animation in milliseconds.
	 */
	private int totalAnimationDurationMs;
	/**
	 * Remaining duration of the currently running animation in milliseconds.
	 */
	private int remainingAnimationDurationMs;

	/**
	 * Constructs an AnimatedNumber from a starting value and with {@link AnimationCurve#EASE_IN_OUT} as the default {@link AnimationCurve}.
	 * @param initialVal initial Value of the AnimatedNumber
	 */
	public AnimatedNumber(float initialVal) {
		value = initialVal;
		animationStart = initialVal;
		animationEnd = initialVal;
		curve = AnimationCurve.LINEAR;
		defaultCurve = AnimationCurve.EASE_IN_OUT;
		totalAnimationDurationMs = (int) GlobalSettings.getAnimationDuration().toMillis();
		remainingAnimationDurationMs = 0;
	}

	/**
	 * Constructs an AnimatedNumber from a starting value and AnimationCurve.
	 * @param initialVal initial Value of the AnimatedNumber
	 * @param defaultCurve curve to use for animations if no curve is specified (has to be {@link AnimationCurve#isNormalized() normalized})
	 */
	public AnimatedNumber(float initialVal, AnimationCurve defaultCurve) {
		value = initialVal;
		animationStart = initialVal;
		animationEnd = initialVal;
		curve = AnimationCurve.LINEAR;
		assert(defaultCurve.isNormalized());
		this.defaultCurve = defaultCurve;
		totalAnimationDurationMs = (int) GlobalSettings.getAnimationDuration().toMillis();
		remainingAnimationDurationMs = 0;
	}

	/**
	 * Updates the animations by a given time that has passed since the last update.
	 * @param deltaTimeMs time between updates
	 * @return true if the animation is finished and can be removed from the {@link AnimationManager}
	 */
	protected boolean updateAnimation(int deltaTimeMs) {
		remainingAnimationDurationMs = remainingAnimationDurationMs - deltaTimeMs;

		if(remainingAnimationDurationMs <= 0) {
			value = animationEnd;
			return true;
		} else {
			float fraction = 1f - (float) remainingAnimationDurationMs / (float) totalAnimationDurationMs;
			value = map(curve.sampleCurve(fraction), animationStart, animationEnd);
			return false;
		}
	}

	/**
	 * Animates the value to a given new value.
	 * Uses {@link GlobalSettings#getAnimationDuration()} to get the duration of the animation.
	 * @param newValue new value to animate to
	 */
	public void animateTo(float newValue) {
		animateTo(newValue, GlobalSettings.getAnimationDuration(), defaultCurve);
	}

	/**
	 * Animates the value to a given new value with a certain animation duration.
	 * @param newValue new value to animate to
	 * @param d duration of the animation to the new value
	 */
	public void animateTo(float newValue, Duration d) {
		animateTo(newValue, d, defaultCurve);
	}

	/**
	 * Animated the value to a given new value with a certain animation duration and AnimationCurve.
	 * @param newValue new value to animate to
	 * @param d duration of animation to the new value
	 * @param c curve to use for the animation to the new value (has to be {@link AnimationCurve#isNormalized() normalized})
	 */
	public void animateTo(float newValue, Duration d, AnimationCurve c) {
		assert(c.isNormalized());

		// To avoid division by zero
		if(value == newValue)
			value += 0.001f;

		if(remainingAnimationDurationMs <= 0 || !c.canContinue()) {
			// Start a new animation without continuing the previous one
			curve = c;
		} else {
			// Continue the previous animation by angling the first BezierControlPoint according to the slope of the current animation

			// Get Normalized Slope
			float fraction = 1f - (float) remainingAnimationDurationMs / (float) totalAnimationDurationMs;
			Vector2D slope = curve.sampleCurveSlope(fraction);

			// Figure out by how much to stretch the y-axis to account for the different animation-lengths and differences in value
			float yMultiplier = (float) d.toMillis() / (float) totalAnimationDurationMs;
			yMultiplier *= (animationEnd - animationStart) / (newValue - value);

			// Update slope with yMultiplier
			slope = new Vector2D(slope.x, slope.y * yMultiplier);

			// Get the new Curve
			curve = c.getCurveWithSlope(slope);
		}

		animationStart = value;
		animationEnd = newValue;
		totalAnimationDurationMs = (int)d.toMillis();
		remainingAnimationDurationMs = totalAnimationDurationMs;

		AnimationManager.addAnimation(this);
	}

	/**
	 * Maps a value in the interval [0, 1] to the interval [min, max].
	 * @param val value to map
	 * @param min minimum value of the interval
	 * @param max maximum value of the interval
	 * @return mapped value
	 */
	private static float map(float val, float min, float max) {
		return val * (max - min) + min;
	}

	/**
	 * Gets the animation value as an integer. Consider using {@link this#floatValue()} instead since the value is represented as a float internally.
	 * @return integer
	 */
	@Override
	public int intValue() {
		return (int) value;
	}

	/**
	 * Gets the animation value as a long. Consider using {@link this#floatValue()} instead since the value is represented as a float internally.
	 * @return long
	 */
	@Override
	public long longValue() {
		return (long) value;
	}

	/**
	 * Gets the animation value as a float.
	 * @return float
	 */
	@Override
	public float floatValue() {
		return value;
	}

	/**
	 * Gets the animation value as a double. Consider using {@link this#floatValue()} instead since the value is represented as a float internally.
	 * @return double
	 */
	@Override
	public double doubleValue() {
		return value;
	}
}
