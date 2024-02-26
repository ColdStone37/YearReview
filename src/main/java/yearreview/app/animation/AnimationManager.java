package yearreview.app.animation;

import java.util.HashSet;

public class AnimationManager {
	private final static HashSet<AnimatedNumber> animations = new HashSet<AnimatedNumber>();

	protected static void addAnimation(AnimatedNumber animation) {
		animations.add(animation);
	}

	public static void updateAnimations(int millis) {
		animations.removeIf(animatedNumber -> animatedNumber.updateAnimation(millis));
	}
}
