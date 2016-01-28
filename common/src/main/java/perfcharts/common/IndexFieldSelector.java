package perfcharts.common;

import java.util.List;

/**
 * An IndexFieldSelector is used to extract a value from specified data row by a
 * given column index
 * 
 * @author Rayson Zhu
 *
 */
public class IndexFieldSelector<T> implements FieldSelector<T> {
	/**
	 * the column index for extracting values
	 */
	private int index;

	/**
	 * Construct the IndexFieldSelector
	 */
	public IndexFieldSelector() {

	}

	/**
	 * Construct the IndexFieldSelector by a column index
	 * 
	 * @param index
	 *            a column index
	 */
	public IndexFieldSelector(int index) {
		this.index = index;
	}

	/**
	 * Extract the value from specified data row by the given column index.
	 * 
	 * 
	 * @param row
	 *            a data row
	 * @return a value
	 */
	@SuppressWarnings("unchecked")
	public T select(List<?> row) {
		return (T)row.get(index);
	}

	/**
	 * Get the column index for extracting the values of rows
	 * 
	 * @return a column index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Set the column index for extracting the values of rows
	 * 
	 * @param index
	 *            a column index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

}
