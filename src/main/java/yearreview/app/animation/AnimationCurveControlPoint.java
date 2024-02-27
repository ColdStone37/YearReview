package yearreview.app.animation;

import java.util.ArrayList;
import java.util.List;

/**
 * A Control point used in the Creation of an {@link AnimationCurve}.
 *
 * @author ColdStone37
 */
public class AnimationCurveControlPoint {
	/**
	 * Position of the Control-Point.
	 */
	public final Vector2D pos;
	/**
	 * Direction of the Control-Point. The x-value of this vector will always be positive.
	 */
	public final Vector2D dir;

	/**
	 * Constructs a Control-Point from a position and direction.
	 * If the direction has a negative x-value the direction-vector will be inverted.
	 * @param pos position of the Control-Point
	 * @param dir direction of the Control-Point
	 */
	public AnimationCurveControlPoint(Vector2D pos, Vector2D dir) {
		this.pos = pos;
		if(dir.x < 0.0f)
			dir = dir.getInverted();
		this.dir = dir;
	}

	/**
	 * Constructs a Control-Point from a position.
	 * The resulting Control-Point will not have a direction/handle.
	 * @param pos position of the Control-Point
	 */
	public AnimationCurveControlPoint(Vector2D pos) {
		this.pos = pos;
		dir = null;
	}

	/**
	 * Constructs a Control-Point from a position and direction.
	 * @param x x-coordinate of the Control-Point
	 * @param y y-coordinate of the Control-Point
	 * @param dx x-direction of the Control-Point
	 * @param dy y-direction of the Control-Point
	 */
	public AnimationCurveControlPoint(float x, float y, float dx, float dy){
		this(new Vector2D(x, y), new Vector2D(dx, dy));
	}

	/**
	 * Constructs a Control-Point from a position.
	 * @param x x-coordinate of the Control-Point
	 * @param y y-coordinate of the Control-Point
	 */
	public AnimationCurveControlPoint(float x, float y) {
		this(new Vector2D(x, y));
	}

	/**
	 * Whether this ControlPoint has a direction/handle.
	 * @return true if the ControlPoint has a direction, false otherwise
	 */
	public boolean hasHandle() {
		return dir != null;
	}

	/**
	 * Constructs a {@link BezierCurve Bezier} between two Control-Points.
	 * @param p1 start of Bezier
	 * @param p2 end of Bezier
	 * @return Bezier Curve from p1 to p2
	 */
	protected static BezierCurve constructBezierBetween(AnimationCurveControlPoint p1, AnimationCurveControlPoint p2) {
		List<Vector2D> controlPoints = new ArrayList<>(4);

		// First Control-Point
		controlPoints.add(p1.pos);

		// Handles of the Control-Points will be added if available
		if(p1.hasHandle())
			controlPoints.add(p1.pos.plus(p1.dir));
		if(p2.hasHandle())
			controlPoints.add(p2.pos.plus(p2.dir.getInverted()));

		// Last Control-Point
		controlPoints.add(p2.pos);

		// Construct Bezier
		return new BezierCurve(controlPoints);
	}

	/**
	 * Outputs a text representation of the Control-Point. (e.g. "pos: (0.1, 0.3) dir: (0.5, 0.75)")
	 * @return text representation of Control-Points
	 */
	@Override
	public String toString() {
		return "pos: " + pos + (hasHandle() ? " dir: " + dir : "");
	}
}
