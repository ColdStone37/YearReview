package yearreview.app.util.value;

public abstract class Value implements Comparable<Value> {
	public abstract String toString();

	public abstract Value plus(Value other);

	protected abstract Object getValue();
}
