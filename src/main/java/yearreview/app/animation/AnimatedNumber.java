package yearreview.app.animation;

import yearreview.app.config.GlobalSettings;

import java.time.Duration;

public class AnimatedNumber extends Number {
	private float value;
	private float animationStart;
	private float animationEnd;
	private AnimationCurve curve;
	private final AnimationCurve defaultCurve;
	private int totalAnimationDurationMs;
	private int remainingAnimationDurationMs;

	public AnimatedNumber(float initialVal, AnimationCurve defaultCurve) {
		value = initialVal;
		animationStart = initialVal;
		animationEnd = initialVal;
		curve = AnimationCurve.LINEAR;
		this.defaultCurve = defaultCurve;
		totalAnimationDurationMs = (int) GlobalSettings.getAnimationDuration().toMillis();
		remainingAnimationDurationMs = 0;
	}

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

	public void animateTo(float newValue) {
		animateTo(newValue, GlobalSettings.getAnimationDuration(), defaultCurve);
	}

	public void animateTo(float newValue, Duration d) {
		animateTo(newValue, d, defaultCurve);
	}

	public void animateTo(float newValue, Duration d, AnimationCurve c) {
		if(remainingAnimationDurationMs <= 0 || !c.canContinue()) {
			animationStart = value;
			animationEnd = newValue;
			totalAnimationDurationMs = (int)d.toMillis();
			remainingAnimationDurationMs = totalAnimationDurationMs;
			curve = c;
		} else {
			float fraction = 1f - (float) remainingAnimationDurationMs / (float) totalAnimationDurationMs;
			Vector2D slope = curve.sampleCurveSlope(fraction);
			float yMultiplier = (float) d.toMillis() / (float) totalAnimationDurationMs;
			yMultiplier *= (animationEnd - animationStart) / (newValue - value);
			slope = new Vector2D(slope.x, slope.y * yMultiplier);
			curve = c.getCurveWithSlope(slope);
			animationStart = value;
			animationEnd = newValue;
			totalAnimationDurationMs = (int)d.toMillis();
			remainingAnimationDurationMs = totalAnimationDurationMs;
		}

		AnimationManager.addAnimation(this);
	}

	private static float map(float val, float min, float max) {
		return val * (max - min) + min;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return (long) value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return (double) value;
	}
}
