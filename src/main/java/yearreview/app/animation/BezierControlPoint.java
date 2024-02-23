package yearreview.app.animation;

public class BezierControlPoint {
	public final Vector2D pos;
	public final Vector2D dir;

	public BezierControlPoint(Vector2D pos, Vector2D dir) {
		this.pos = pos;
		if(dir.x < 0.0f)
			dir = dir.getInverted();
		this.dir = dir;
	}

	public BezierControlPoint(float x, float y, float dx, float dy){
		this(new Vector2D(x, y), new Vector2D(dx, dy));
	}
}
