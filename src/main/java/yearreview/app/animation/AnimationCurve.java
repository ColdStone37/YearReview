package yearreview.app.animation;

import java.util.TreeMap;

/**
 * Curve used for animating. AnimationCurves can consist of multiple {@link BezierCurve BezierCurves}.
 * A Curve is {@link AnimationCurve#isNormalized() normalized} if it starts at (0, 0) and ends at (1, 1).
 *
 * @author ColdStone37
 */
public class AnimationCurve {
	/**
	 * A Curve for linear animations.
	 */
	public final static AnimationCurve LINEAR = new AnimationCurve(new BezierControlPoint(0f, 0f), new BezierControlPoint(1f, 1f));
	/**
	 * A Curve for ease-in-out animations.
	 */
	public final static AnimationCurve EASE_IN_OUT = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.5f, 0f), new BezierControlPoint(1f, 1f, 0.5f, 0f));
	/**
	 * A Curve for ease-in animations.
	 */
	public final static AnimationCurve EASE_IN = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.5f, 0f), new BezierControlPoint(1f, 1f));
	/**
	 * A Curve for ease-out animations.
	 */
	public final static AnimationCurve EASE_OUT = new AnimationCurve(new BezierControlPoint(0f, 0f), new BezierControlPoint(1f, 1f, 0.5f, 0f));
	/**
	 * Precision of the binary search when finding a certain x-value of a {@link BezierCurve}.
	 */
	private final static float BINARY_SEARCH_PRECISION = 0.001f;
	/**
	 * Allows access to each Bezier by its starting x-value.
	 */
	private final TreeMap<Float, BezierCurve> curves;
	/**
	 * Initial position of the Curve.
	 */
	private final Vector2D posStart;
	/**
	 * Final position of the Curve.
	 */
	private final Vector2D posEnd;
	/**
	 * Whether the animation can continue another animation. True if the first {@link BezierControlPoint} has a handle.
	 */
	private final boolean canContinue;
	/**
	 * Whether the AnimationCurve is normalized, meaning that it starts at (0, 0) and ends at (1, 1).
	 */
	private final boolean isNormalized;
	/**
	 * BezierControlPoints used to initially create the curve. Only stored if the Curve {@link this#canContinue} a previous animation.
	 */
	private final BezierControlPoint[] controlPoints;

	/**
	 * Constructs a AnimationCurve from a set of Control-Points.
	 * @param controlPoints Control-Points to construct the Curve
	 */
	public AnimationCurve(BezierControlPoint... controlPoints) {
		if(!validateControlPoints(controlPoints))
			throw new Error("Invalid configuration of AnimationCurve");
		canContinue = controlPoints[0].hasHandle();
		if(canContinue){
			this.controlPoints = controlPoints;
		} else {
			this.controlPoints = null;
		}
		curves = new TreeMap<>();
		posStart = controlPoints[0].pos;
		posEnd = controlPoints[controlPoints.length-1].pos;
		isNormalized = (posStart.x == 0f && posStart.y == 0f && posEnd.x == 1f && posEnd.y == 1f);
		for(int i = 0; i < controlPoints.length-1; i++)
			curves.put(controlPoints[i].pos.x, BezierControlPoint.constructBezierBetween(controlPoints[i], controlPoints[i+1]));
	}

	/**
	 * Gets a new AnimationCurve with the start angled at a certain slope.
	 * @param slope slope to angle the start of the new Curve at
	 * @return Curve with angled start
	 */
	protected AnimationCurve getCurveWithSlope(Vector2D slope){
		if(!canContinue)
			return this;

		BezierControlPoint[] newControlPoints = new BezierControlPoint[controlPoints.length];
		Vector2D bezierDir = slope;

		bezierDir = bezierDir.getScaled(Math.min(bezierDir.x, controlPoints[1].pos.x - controlPoints[0].pos.x - (controlPoints[1].hasHandle() ? controlPoints[1].dir.x : 0f)) / bezierDir.x);
		newControlPoints[0] = new BezierControlPoint(controlPoints[0].pos, bezierDir);
		System.arraycopy(controlPoints, 1, newControlPoints, 1, newControlPoints.length - 1);
		return new AnimationCurve(newControlPoints);
	}

	public boolean canContinue() {
		return canContinue;
	}

	/**
	 * Samples the AnimationCurve at a certain x-position.
	 * @param x position to sample the curve at
	 * @return AnimationCurve at that position
	 */
	public float sampleCurve(float x) {
		if(x <= posStart.x)
			return posStart.y;
		if(x >= posEnd.x)
			return posEnd.y;

		BezierCurve curve = curves.floorEntry(x).getValue();
		return sampleBezierAtX(curve, x, curve.getStart().x, curve.getEnd().x).y;
	}

	/**
	 * Samples the slope of the Curve at a certain x-value.
	 * @param x position to sample the slope at
	 * @return normalized Vector pointing in the direction of the Curve at the given x-position
	 */
	public Vector2D sampleCurveSlope(float x) {
		if(x < posStart.x)
			return Vector2D.RIGHT;
		if(x > posEnd.x)
			return Vector2D.RIGHT;

		BezierCurve curve = curves.floorEntry(x).getValue();
		return sampleBezierDeltaAtX(curve, x, curve.getStart().x, curve.getEnd().x);
	}

	/**
	 * Samples the direction of the given Bezier at a certain x-position using binary search.
	 * @param curve Bezier to sample direction of
	 * @param xVal x-position to sample the direction at
	 * @param min minimum of current interval in the binary search
	 * @param max maximum of current interval in the binary search
	 * @return normalized Vector pointing in the direction of the Bezier at the given x-position
	 */
	private Vector2D sampleBezierDeltaAtX(BezierCurve curve, float xVal, float min, float max) {
		// If the interval is small enough -> sample the borders of the interval and calculate the normalized difference
		if(max - min < BINARY_SEARCH_PRECISION) {
			Vector2D v1 = curve.samplePoint(min);
			Vector2D v2 = curve.samplePoint(max);

			return v2.minus(v1).getNormalized();
		}

		// Otherwise -> continue binary search
		float mid = (min + max) / 2f;
		if(curve.samplePoint(mid).x > xVal) {
			// Left half
			return sampleBezierDeltaAtX(curve, xVal, min, mid);
		} else {
			// Right half
			return sampleBezierDeltaAtX(curve, xVal, mid, max);
		}
	}

	/**
	 * Samples the Bezier at a certain x-position by doing binary search over the interval.
	 * X-Value of the output vector represents the result of the binary search such that curve.samplePoint(vec.x).x == xVal.
	 * @param curve curve to sample the position of
	 * @param xVal x-value to find using binary search
	 * @param min left border of the interval
	 * @param max right border of the interval
	 * @return new Vector storing the value input into the Bezier and the y-value at that position
	 */
	private Vector2D sampleBezierAtX(BezierCurve curve, float xVal, float min, float max) {
		float mid = (min + max) / 2f;

		if(max - min < BINARY_SEARCH_PRECISION)
			return new Vector2D(mid, curve.samplePoint(mid).y);

		if(curve.samplePoint(mid).x > xVal) {
			return sampleBezierAtX(curve, xVal, min, mid);
		} else {
			return sampleBezierAtX(curve, xVal, mid, max);
		}
	}

	/**
	 * Validates the Control-Points of an AnimationCurve. The following has to be true for a valid AnimationCurve:
	 * - Atleast two {@link BezierControlPoint}
	 * - A BezierControlPoint b2 following a BezierControlPoint b1 has to have a higher x-value than b1: b1.pos.x <= b2.pos.x
	 * - If atleast one of the BezierControlPoints has handles also b1.pos.x + b1.dir.x <= b2.pos.x - b2.dir.x has to be fulfilled
	 * @param controlPoints Control-Points to validate
	 * @return true if they are valid Control-Points for an AnimationCurve, false otherwise
	 */
	private static boolean validateControlPoints(BezierControlPoint[] controlPoints) {
		int n = controlPoints.length;

		// There have to be atleast 2 Control-Points
		if(n < 2)
			return false;
		for(int i = 0; i < n-1; i++) {
			// x-values have to be increasing
			if(controlPoints[i].pos.x > controlPoints[i+1].pos.x)
				return false;
			// Handles also need to have higher x-values than previous handles
			if(controlPoints[i].pos.x + (controlPoints[i].hasHandle() ? controlPoints[i].dir.x : 0f) > controlPoints[i+1].pos.x - (controlPoints[i+1].hasHandle() ? controlPoints[i+1].dir.x : 0f))
				return false;
		}

		// -> Control Point is valid
		return true;
	}

	/**
	 * Gets whether the AnimationCurve is normalized (meaning that the first Control-Point is at (0, 0) and the last Control-Point is at (1,1)).
	 * Only normalized AnimationCurves can be used in {@link AnimatedNumber AnimatedNumbers}.
	 * @return whether the AnimationCurve is normalized
	 */
	public boolean isNormalized() {
		return isNormalized;
	}
}
