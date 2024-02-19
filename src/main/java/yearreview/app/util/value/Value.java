package yearreview.app.util.value;

public abstract class Value implements Comparable<Value> {
	public abstract String toString();

	public abstract void addValue(Value other);

	protected abstract Object getValue();
}
