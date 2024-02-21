package yearreview.app.data.processor.toplist;

import java.time.Instant;
import java.util.Collection;

/**
 * An Interface for Classes functioning as an adapter to the {@link TopListGenerator}
 * by transforming the data of a {@link yearreview.app.data.sources.DataSource}
 * into a Collection of {@link TopListElement TopListElements}.
 *
 * @author ColdStone37
 */
public interface TopListAdapter {

	/**
	 * Gets the collection of {@link TopListElement TopListElements} that should be processed by the {@link TopListGenerator}.
	 *
	 * @param t time until which all values should be added up to
	 * @return Collection of {@link TopListElement TopListElements}
	 */
	public Collection<TopListElement> getElements(Instant t);
}
