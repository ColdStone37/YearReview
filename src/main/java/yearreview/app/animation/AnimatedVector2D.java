package yearreview.app.animation;

import java.time.Duration;

public class AnimatedVector2D {
	public AnimatedNumber x;
	public AnimatedNumber y;

	public AnimatedVector2D(Vector2D initialVector, AnimationCurve defaultCurve) {
		x = new AnimatedNumber(initialVector.x, defaultCurve);
		y = new AnimatedNumber(initialVector.y, defaultCurve);
	}

	public void animateTo(Vector2D pos) {
		x.animateTo(pos.x);
		y.animateTo(pos.y);
	}

	public void animateTo(Vector2D pos, Duration d){
		x.animateTo(pos.x, d);
		y.animateTo(pos.y, d);
	}

	public void animateTo(Vector2D pos, Duration d, AnimationCurve c) {
		x.animateTo(pos.x, d, c);
		y.animateTo(pos.y, d, c);
	}

	public Vector2D getVector(){
		return new Vector2D(x.floatValue(), y.floatValue());
	}

	public float getX() {
		return x.floatValue();
	}

	public float getY() {
		return y.floatValue();
	}
}
