package yearreview.app.animation;

import java.util.ArrayList;
import java.util.List;

public class BezierControlPoint {
	public final Vector2D pos;
	public final Vector2D dir;

	public BezierControlPoint(Vector2D pos, Vector2D dir) {
		this.pos = pos;
		if(dir.x < 0.0f)
			dir = dir.getInverted();
		this.dir = dir;
	}

	public BezierControlPoint(Vector2D pos) {
		this.pos = pos;
		dir = null;
	}

	public BezierControlPoint(float x, float y, float dx, float dy){
		this(new Vector2D(x, y), new Vector2D(dx, dy));
	}

	public BezierControlPoint(float x, float y) {
		this(new Vector2D(x, y));
	}

	public boolean hasHandle() {
		return dir != null;
	}

	public static BezierCurve constructBezierBetween(BezierControlPoint p1, BezierControlPoint p2) {
		List<Vector2D> controlPoints = new ArrayList<>(4);
		controlPoints.add(p1.pos);
		if(p1.hasHandle())
			controlPoints.add(p1.pos.plus(p1.dir));
		if(p2.hasHandle())
			controlPoints.add(p2.pos.plus(p2.dir.getInverted()));
		controlPoints.add(p2.pos);
		return new BezierCurve(controlPoints);
	}

	@Override
	public String toString() {
		return "pos: " + pos + " dir: " + dir;
	}
}
