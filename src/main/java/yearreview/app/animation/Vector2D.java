package yearreview.app.animation;

/**
 * A class representing an immutable 2D-Vector of {@link Float Floats}.
 * Similar to {@link java.awt.geom.Point2D} but allows for direct access of the x and y values.
 *
 * @author ColdStone37
 */
public class Vector2D {
	/**
	 * A constant Vector facing upwards: (0, -1)
	 */
	public final static Vector2D UP = new Vector2D(0f, -1f);
	/**
	 * A constant Vector facing right: (1, 0)
	 */
	public final static Vector2D RIGHT = new Vector2D(1f, 0f);
	/**
	 * A constant Vector facing downwards: (0, 1)
	 */
	public final static Vector2D DOWN = new Vector2D(0f, 1f);
	/**
	 * A constant Vector facing left: (-1, 0)
	 */
	public final static Vector2D LEFT = new Vector2D(-1f, 0f);
	/**
	 * A constant zero Vector: (0, 0)
	 */
	public final static Vector2D ZERO = new Vector2D(0f, 0f);

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
	 * @param x x-value of the Vector
	 * @param y y-value of the Vector
	 */
	public Vector2D(float x, float y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Mixes the values of two Vectors.
	 * Result: (v1 * mix + v2 * (1-mix))
	 * @param mix percentage of the mix
	 * @param v1 first Vector
	 * @param v2 second Vector
	 * @return mixed Vector
	 */
	public static Vector2D mix(float mix, Vector2D v1, Vector2D v2) {
		float mixInv = 1.0f - mix;
		return new Vector2D(v1.x * mix + v2.x * mixInv, v1.y * mix + v2.y * mixInv);
	}

	/**
	 * Formatted output of a Vector (e.g.: "(4.3, -1.2)")
	 * @return formatted Vector
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Gets a normalized version of this Vector.
	 * If this Vectors x and y values are zero a Vector facing to the right is returned.
	 * @return Vector of length 1 facing the same direction
	 */
	public Vector2D getNormalized() {
		if(x == 0 && y == 0)
			return new Vector2D(1.0f, 0.0f);
		float length = (float)Math.sqrt(x * x + y * y);
		return new Vector2D(x / length, y / length);
	}

	/**
	 * Gets a Vector facing the same direction as this Vector with a given length.
	 * Works by first normalizing the Vector and then scaling it.
	 * @param l length of the new Vector
	 * @return Vector with length l
	 */
	public Vector2D setLength(float l) {
		return getNormalized().getScaled(l);
	}

	/**
	 * Gets this Vector scaled by a passed value.
	 * @param scale value to scale the Vector by
	 * @return scaled Vector
	 */
	public Vector2D getScaled(float scale) {
		if(scale == 1f)
			return this;
		return new Vector2D(x * scale, y * scale);
	}

	/**
	 * Gets the length of this Vector using the Pythagoras-Theorem: sqrt(x * x + y * y)
	 * @return length of the Vector
	 */
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Gets an inverted version of this Vector: (-x, -y)
	 * @return inverted Vector
	 */
	public Vector2D getInverted() {
		return new Vector2D(-x, -y);
	}

	/**
	 * Gets a new Vector which is the result of adding this Vector to a passed Vector.
	 * @param other Vector to add
	 * @return sum of the Vectors
	 */
	public Vector2D plus(Vector2D other) {
		return new Vector2D(x + other.x, y + other.y);
	}

	/**
	 * Gets a new Vector which is the result of subtracting this Vector with a passed Vector.
	 * @param other Vector to subtract
	 * @return difference between the Vectors
	 */
	public Vector2D minus(Vector2D other) {
		return new Vector2D(x - other.x, y - other.y);
	}
}
