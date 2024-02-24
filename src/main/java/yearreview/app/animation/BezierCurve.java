package yearreview.app.animation;

import java.util.Arrays;
import java.util.List;

/**
 * A Bezier-Curve used for calculation of smooth animations.
 *
 * @author ColdStone37
 */
public class BezierCurve {
	/**
	 * Array of controlPoints of the curve.
	 */
	private final Vector2D[] controlPoints;

	/**
	 * Constructs a new BezierCurve from atleast two Control Points.
	 * @param controlPoints control points of the Bezier
	 */
	public BezierCurve(Vector2D... controlPoints) {
		assert(controlPoints.length >= 2);
		this.controlPoints = controlPoints;
	}

	/**
	 * Constructs a new BezierCurve from a list of Control Points.
	 * @param controlPoints control points of the Bezier
	 */
	public BezierCurve(List<Vector2D> controlPoints) {
		assert(controlPoints.size() >= 2);
		this.controlPoints = new Vector2D[controlPoints.size()];
		for(int i = 0; i < controlPoints.size(); i++)
			this.controlPoints[i] = controlPoints.get(i);
	}

	/**
	 * Samples the Bezier at a certain point along the curve.
	 * @param val how far along the curve
	 * @return sampled position
	 */
	public Vector2D samplePoint(float val) {
		Vector2D[] copiedPoints = Arrays.copyOf(controlPoints, controlPoints.length);

		for(int i = copiedPoints.length - 1; i > 0; i--)
			for(int j = 0; j < i; j++)
				copiedPoints[j] = Vector2D.mix(val, copiedPoints[j+1], copiedPoints[j]);

		return copiedPoints[0];
	}
}
