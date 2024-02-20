package yearreview.app.data.processor.toplist;

import yearreview.app.util.value.Value;

import java.net.URL;

public class TopListElement implements Comparable<TopListElement> {
	private static int count = 0;
	private final int id;
	private final TopListCompatibleItem item;
	private final Value value;

	public TopListElement(TopListCompatibleItem item, Value value) {
		id = count;
		count++;
		this.item = item;
		this.value = value;
	}

	public void addValue(Value value) {
		this.value.addValue(value);
	}

	public Value getValue() {
		return value;
	}

	public TopListCompatibleItem getItem() {
		return item;
	}

	@Override
	public int compareTo(TopListElement other) {
		int valCompare = value.compareTo(other.getValue());
		if(valCompare != 0)
			return valCompare;
		return item.getMainText().compareTo(other.getItem().getMainText());
	}
}