package yearreview.app.animation;

import java.util.HashSet;

/**
 * Class that Stores all currently running animations and allows updating all of them in one function-call.
 *
 * @author ColdStone37
 */
public abstract class AnimationManager {
	/**
	 * Set that stores all currently running animations.
	 */
	private final static HashSet<AnimatedNumber> animations = new HashSet<>();

	/**
	 * Adds an animation to the Manager.
	 * It will automatically be removed if the animation is over and has to be reinserted if the animation starts again.
	 * @param animation animation to insert into the manager
	 */
	protected static void addAnimation(AnimatedNumber animation) {
		animations.add(animation);
	}

	/**
	 * Updates all animations by a certain amount of milliseconds.
	 * Animations that have finished will be removed from the Manager.
	 * @param millis milliseconds to update the animations with
	 */
	public static void updateAnimations(int millis) {
		animations.removeIf(animatedNumber -> animatedNumber.updateAnimation(millis));
	}
}
