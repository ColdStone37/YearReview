package yearreview.app.animation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class AnimationManager {
	private final static HashSet<AnimatedNumber> animations = new HashSet<AnimatedNumber>();

	protected static void addAnimation(AnimatedNumber animation) {
		animations.add(animation);
	}

	public static void updateAnimations(int millis) {
		Iterator<AnimatedNumber> iter = animations.iterator();
		while(iter.hasNext())
			if(iter.next().updateAnimation(millis))
				iter.remove();
	}
}
