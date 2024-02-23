package yearreview.app.animation;

/**
 * A class representing an immutable 2D-Vector of {@link Float Floats}.
 * Similar to {@link java.awt.geom.Point2D} but allows for direct access of the x and y values.
 *
 * @author ColdStone37
 */
public class Vector2D {
	/**
	 * X-value of the Vector.
	 */
	public final float x;
	/**
	 * Y-value of the Vector.
	 */
	public final float y;

	/**
	 * Constructs a new Vector from an x- and y-value.
	 * @param x x-value of the vector
	 * @param y y-value of the vector
	 */
	public Vector2D(float x, float y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Mixes the values of two Vectors.
	 * Result: (v1 * mix + v2 * (1-mix))
	 * @param mix percentage of the mix
	 * @param v1 first vector
	 * @param v2 second vector
	 * @return mixed vector
	 */
	public static Vector2D mix(float mix, Vector2D v1, Vector2D v2) {
		float mixInv = 1.0f - mix;
		return new Vector2D(v1.x * mix + v2.x * mixInv, v1.y * mix + v2.y * mixInv);
	}

	/**
	 * Formatted output of a Vector (e.g.: "(4.3, -1.2)")
	 * @return formatted vector
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Gets a normalized version of this vector.
	 * If this vectors x and y values are zero a vector facing to the right is returned.
	 * @return vector of length 1 facing the same direction
	 */
	public Vector2D getNormalized() {
		if(x == 0 && y == 0)
			return new Vector2D(1.0f, 0.0f);
		float length = (float)Math.sqrt(x * x + y * y);
		return new Vector2D(x / length, y / length);
	}

	/**
	 * Gets a inverted version of this vector (-x, -y).
	 * @return inverted vector
	 */
	public Vector2D getInverted() {
		return new Vector2D(-x, -y);
	}

	/**
	 * Returns a new vector which is the result of the other vector added to this vector.
	 * @param other vector to add
	 * @return new vector representing the sum of both vectors
	 */
	public Vector2D add(Vector2D other) {
		return new Vector2D(x + other.x, y + other.y);
	}
}
