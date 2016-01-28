package perfcharts.calc;

import java.util.List;

import perfcharts.chart.Point2D;
import perfcharts.common.FieldSelector;

/**
 * A {@link Chart2DCalculation} defines the procedure for producing
 * 2-dimensional chart series from data rows.
 * 
 * @author Rayson Zhu
 *
 */
public interface Chart2DCalculation {
	/**
	 * Produce 2-dimensional chart series from specified data rows.
	 * 
	 * @param rows
	 *            data rows
	 * @param xField
	 *            the x-field
	 * @param yField
	 *            the y-field
	 * @return a list of points that contains the points of produced series
	 */
	public List<Point2D> produce(List<List<Object>> rows,
								 FieldSelector xField, FieldSelector yField);

	/**
	 * Get the interval for point merging.
	 * 
	 * @return interval
	 */
	public int getInterval();

	/**
	 * Set the interval for point merging.
	 * 
	 * @param interval
	 *            interval
	 */
	public void setInterval(int interval);
}
