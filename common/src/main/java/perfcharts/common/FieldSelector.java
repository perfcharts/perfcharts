package perfcharts.common;

import java.util.List;

/**
 * A FieldSelector is the object for extracting a value from a specified data row
 * 
 * @author Rayson Zhu
 *
 */
public interface FieldSelector<T> {
	/**
	 * extract the value from specified data row
	 * 
	 * @param row
	 *            a data row
	 * @return a value
	 */
	public T select(List<?> row);
}
