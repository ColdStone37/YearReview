package yearreview.app.grid.widgets.toplist;

import yearreview.app.animation.AnimationCurve;
import yearreview.app.config.GlobalSettings;
import yearreview.app.data.DataManager;
import yearreview.app.data.processor.toplist.TopListElement;
import yearreview.app.data.processor.toplist.TopListGenerator;
import yearreview.app.data.processor.toplist.TopListItem;
import yearreview.app.grid.widgets.Widget;
import yearreview.app.util.value.DurationValue;
import yearreview.app.util.value.ValueType;
import yearreview.app.util.xml.XmlNode;

import java.awt.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.time.Instant;

public class TopListWidget extends Widget {
	private final int listLength;
	private final TopListGenerator generator;
	private final ValueType valueType;

	private final HashMap<TopListElement, TopListElementWidget> widgetMap;

	private TopListElementWidget test;

	/**
	 * Constructor for a GridSegment that initializes the position of the segment and the Shape for the {@link Graphics2D#clip}-function.
	 *
	 * @param x x-position of the widget
	 * @param y y-position of the widget
	 * @param w width of the widget
	 * @param h height of the widget
	 */
	public TopListWidget(float x, float y, float w, float h, XmlNode config, DataManager dataManager) {
		super(x, y, w, h);
		XmlNode generatorConfig = config.getChildByName("Generator");
		generator = (TopListGenerator) dataManager.getProcessorByTag(generatorConfig.getAttributeByName("name"));
		listLength = Integer.parseInt(generatorConfig.getChildContent("Length"));
		valueType = ValueType.getTypeByName(generatorConfig.getChildContent("Value"));
		widgetMap = new HashMap<>();
		test = new TopListElementWidget(10, h, w-20, h-20, new TopListElement(new TopListItem("Test"), new DurationValue(Duration.ZERO)));
		test.animateTo(10, 10, AnimationCurve.EASE_OUT);
	}

	/**
	 * Renders the widget in its local space (meaning that the coordinate system goes from (0, 0) to (w, h)).
	 *
	 * @param g    graphic to render to
	 * @param time time to render the widget at
	 */
	@Override
	protected void renderLocalSpace(Graphics2D g, Instant time) {
		// Background
		g.setColor(GlobalSettings.getAccentColor1());
		g.fillRect(0, 0, (int) w, (int) h);

		List<TopListElement> topList = generator.getTopList(time, listLength, valueType);

		test.renderGlobalSpace(g, time);
	}
}
