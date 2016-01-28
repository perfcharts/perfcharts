package perfcharts.calc;

import java.util.LinkedList;
import java.util.List;

import perfcharts.chart.Point2D;
import perfcharts.common.FieldSelector;

/**
 * This calculation groups data rows by their x-fields, calculates the sum of
 * their y-fields, and makes the sum as result y-values.
 * 
 * @author Rayson Zhu
 *
 */
public class SumCalculation implements Chart2DCalculation {
	/**
	 * the interval for point merging
	 */
	private int interval = 0;
	/**
	 * the times to multiply on y-value of each point
	 */
	private int times = 1;

	/**
	 * constructor
	 */
	public SumCalculation() {

	}

	/**
	 * constructor
	 * 
	 * @param interval
	 *            the interval for point merging
	 */
	public SumCalculation(int interval) {
		this.interval = interval;
	}

	/**
	 * constructor
	 * 
	 * @param interval
	 *            the interval for point merging
	 * @param times
	 *            the times to multiply on y-value of each point
	 */
	public SumCalculation(int interval, int times) {
		this.interval = interval;
		this.times = times;
	}

	public List<Point2D> produce(List<List<Object>> rows,
								 FieldSelector xField, FieldSelector yField) {
		List<Point2D> stops = new LinkedList<Point2D>();
		if (rows.isEmpty())
			return stops;
		Comparable<?> firstX = (Comparable<?>)xField.select(rows.get(0));
		Comparable<?> lastX = firstX;
		double y = 0.0;
		int count = 0;
		for (List<Object> row : rows) {
			Comparable<?> x = (Comparable<?>)xField.select(row);
			if (interval > 1) {
				Number _x = (Number)x;
				Number _firstX = (Number)firstX;
				x = _firstX.longValue() + (_x.longValue() - _firstX.longValue())
						/ interval * interval;
			}
			if (lastX.equals(x)) {
				y += ((Number) yField.select(row)).doubleValue();
				++count;
			} else {
				if (count > 0)
					stops.add(new Point2D(lastX, y * times, count));
				y = ((Number) yField.select(row)).doubleValue();
				count = 1;
			}
			lastX = x;
		}
		if (count > 0)
			stops.add(new Point2D(lastX, y * times, count));
		return stops;
	}

	/**
	 * Get the times to multiply on y-value of each point
	 * 
	 * @return the times
	 */
	public int getTimes() {
		return times;
	}

	/**
	 * Set the times to multiply on y-value of each point
	 * 
	 * @param times
	 *            the times
	 */
	public void setTimes(int times) {
		this.times = times;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
