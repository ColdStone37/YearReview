package yearreview.app.grid;

import yearreview.app.config.GlobalSettings;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Parses the configuration of the grid and creates the segments with according sizes.
 *
 * @author ColdStone37
 */
public class GridManager {

	AffineTransform gridTransform;

	private final float xScale, yScale;

	GridManager() {
		gridTransform = new AffineTransform();
		xScale = (float) GlobalSettings.getRenderWidth() / (float) GlobalSettings.getGridWidth();
		yScale = (float) GlobalSettings.getRenderHeight() / (float) GlobalSettings.getGridHeight();
		gridTransform.scale(xScale, yScale);
	}

	public Point2D transformToGrid(int gridX, int gridY) {
		Point2D.Float pt = new Point2D.Float(gridX, gridY);
		return gridTransform.transform(pt, pt);
	}

	public float scaleXValue(float xVal) {
		return xVal * xScale;
	}

	public float scaleYValue(float yVal) {
		return yVal * yScale;
	}
}