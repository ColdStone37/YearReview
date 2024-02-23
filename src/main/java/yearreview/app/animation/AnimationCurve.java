package yearreview.app.animation;

import java.util.TreeMap;

public class AnimationCurve {
	public final static AnimationCurve LINEAR = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.33f, 0.33f), new BezierControlPoint(1f, 1f, 0.33f, 0.33f));
	public final static AnimationCurve EASE_IN_OUT = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.5f, 0f), new BezierControlPoint(1f, 1f, 0.5f, 0f));
	public final static AnimationCurve EASE_IN = new AnimationCurve(new BezierControlPoint(0f, 0f, 0.5f, 0f), new BezierControlPoint(1f, 1f, 0.5f, 0f));
	private final static float BINARY_SEARCH_PRECISION = 0.001f;
	private final TreeMap<Float, BezierCurve> curves;

	public AnimationCurve(BezierControlPoint... controlPoints) {
		assert(validateControlPoints(controlPoints));
		curves = new TreeMap<>();
		for(int i = 0; i < controlPoints.length-1; i++)
			curves.put(controlPoints[i].pos.x, new BezierCurve(controlPoints[i].pos, controlPoints[i].pos.add(controlPoints[i].dir), controlPoints[i+1].pos.add(controlPoints[i+1].dir.getInverted()), controlPoints[i+1].pos));
	}

	public float sampleCurve(float x) {
		assert(x >= 0f && x <= 1f);

		BezierCurve curve = curves.floorEntry(x).getValue();
		return sampleBezierAtX(curve, x, 0f, 1f).y;
	}

	private Vector2D sampleBezierAtX(BezierCurve curve, float xVal, float min, float max) {
		float mid = (min + max) / 2f;

		if(max - min < BINARY_SEARCH_PRECISION)
			return curve.samplePoint(mid);

		if(curve.samplePoint(mid).x > xVal) {
			return sampleBezierAtX(curve, xVal, min, mid);
		} else {
			return sampleBezierAtX(curve, xVal, mid, max);
		}
	}

	private static boolean validateControlPoints(BezierControlPoint[] controlPoints) {
		int n = controlPoints.length;
		if(n < 2)
			return false;
		if(controlPoints[0].pos.x != 1f || controlPoints[0].pos.y != 1f)
			return false;
		if(controlPoints[n-1].pos.x != 1f || controlPoints[n-1].pos.y != 1f)
			return false;
		for(int i = 0; i < n-1; i++) {
			if(controlPoints[i].pos.x > controlPoints[i+1].pos.x)
				return false;
			if(controlPoints[i].pos.x + controlPoints[i].dir.x > controlPoints[i+1].pos.x - controlPoints[i+1].dir.x)
				return false;
		}
		return true;
	}
}
