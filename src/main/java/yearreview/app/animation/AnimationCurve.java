package yearreview.app.animation;

import java.util.TreeMap;

public class AnimationCurve {
	public final static AnimationCurve LINEAR = new AnimationCurve(new BezierControlPoint(0f, 0f), new BezierControlPoint(1f, 1f));
	public final static AnimationCurve EASE_IN_OUT = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.5f, 0f), new BezierControlPoint(1f, 1f, 0.5f, 0f));
	public final static AnimationCurve EASE_IN = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.5f, 0f), new BezierControlPoint(1f, 1f));
	public final static AnimationCurve EASE_OUT = new AnimationCurve(new BezierControlPoint(0f, 0f), new BezierControlPoint(1f, 1f, 0.5f, 0f));
	private final static float BINARY_SEARCH_PRECISION = 0.001f;
	private final TreeMap<Float, BezierCurve> curves;

	public AnimationCurve(BezierControlPoint... controlPoints) {
		if(!validateControlPoints(controlPoints))
			throw new Error("Invalid configuration of AnimationCurve");
		curves = new TreeMap<>();
		for(int i = 0; i < controlPoints.length-1; i++)
			curves.put(controlPoints[i].pos.x, BezierControlPoint.constructBezierBetween(controlPoints[i], controlPoints[i+1]));
	}

	/**
	 * Samples the AnimationCurve at a certain x-position.
	 * @param x position to sample the curve at
	 * @return AnimationCurve at that position
	 */
	public float sampleCurve(float x) {
		if(x <= 0f)
			return 0f;
		if(x >= 1f)
			return 1f;

		BezierCurve curve = curves.floorEntry(x).getValue();
		return sampleBezierAtX(curve, x, 0f, 1f).y;
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
	 * -
	 * @param controlPoints
	 * @return
	 */
	private static boolean validateControlPoints(BezierControlPoint[] controlPoints) {
		int n = controlPoints.length;
		if(n < 2)
			return false;
		if(controlPoints[0].pos.x != 0f || controlPoints[0].pos.y != 0f)
			return false;
		if(controlPoints[n-1].pos.x != 1f || controlPoints[n-1].pos.y != 1f)
			return false;
		for(int i = 0; i < n-1; i++) {
			if(controlPoints[i].pos.x > controlPoints[i+1].pos.x)
				return false;
			if(controlPoints[i].pos.x + (controlPoints[i].hasHandle() ? controlPoints[i].dir.x : 0f) > controlPoints[i+1].pos.x - (controlPoints[i+1].hasHandle() ? controlPoints[i+1].dir.x : 0f))
				return false;
		}
		return true;
	}
}
