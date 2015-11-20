package perfcharts.calc;

import java.util.LinkedList;
import java.util.List;

import perfcharts.chart.Point2D;
import perfcharts.common.FieldSelector;

/**
 * This calculation groups data rows by their x-fields, counts the number of
 * them, and makes the count as result y-values.
 * 
 * @author Rayson Zhu
 *
 */
public class CountCalculation implements Chart2DCalculation {
	/**
	 * the interval for point merging
	 */
	private int interval = 0;
	/**
	 * the times to multiply on y-value of each point
	 */
	private double times = 1;
	/**
	 * whether set y-value to zero when there is no data in that interval
	 */
	private boolean setZeroIfIntervalNoData = false;

	/**
	 * constructor
	 */
	public CountCalculation() {

	}

	/**
	 * constructor
	 * 
	 * @param interval
	 *            the interval for point merging
	 */
	public CountCalculation(int interval) {
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
	public CountCalculation(int interval, double times) {
		this.interval = interval;
		this.times = times;
	}

	/**
	 * constructor
	 * 
	 * @param interval
	 *            the interval for point merging
	 * @param times
	 *            the times to multiply on y-value of each point
	 * @param setZeroIfIntervalNoData
	 *            whether set y-value to zero when there is no data in that
	 *            interval
	 */
	public CountCalculation(int interval, double times,
			boolean setZeroIfIntervalNoData) {
		this.interval = interval;
		this.times = times;
		this.setZeroIfIntervalNoData = setZeroIfIntervalNoData;
	}

	public List<Point2D> produce(List<List<Object>> rows, FieldSelector xField,
			FieldSelector yField) {
		List<Point2D> stops = new LinkedList<Point2D>();
		if (rows.isEmpty())
			return stops;
		Comparable<?> firstX = (Comparable<?>) xField.select(rows.get(0));
		Comparable<?> lastX = firstX;
		int count = 0;
		for (List<Object> row : rows) {
			Comparable<?> x = (Comparable<?>) xField.select(row);
			if (interval > 1) {
				Number _x = (Number) x;
				Number _firstX = (Number) firstX;
				x = _firstX.longValue()
						+ (_x.longValue() - _firstX.longValue()) / interval
						* interval;
			}
			if (lastX.equals(x)) {
				++count;
			} else {
				if (count > 0) {
					stops.add(new Point2D(lastX, count * times * 1000.0
							/ interval, count));
					if (setZeroIfIntervalNoData) {
						Number _x = (Number)x;
						Number _lastX = (Number)lastX;
						if (_lastX.doubleValue() + interval < _x.doubleValue()) {
							stops.add(new Point2D(_lastX.doubleValue()
									+ interval, 0, 0));
							if (_lastX.doubleValue() + interval < _x
									.doubleValue() - interval)
								stops.add(new Point2D(_x.doubleValue()
										- interval, 0, 0));
						}
					}
				}
				count = 1;
			}
			lastX = x;
		}
		if (count > 0)
			stops.add(new Point2D(lastX, count * times * 1000.0 / interval,
					count));
		return stops;
	}

	/**
	 * Get the times to multiply on y-value of each point.
	 * 
	 * @return the times
	 */
	public double getTimes() {
		return times;
	}

	/**
	 * Set the times to multiply on y-value of each point.
	 * 
	 * @param times
	 *            the times
	 */
	public void setTimes(double times) {
		this.times = times;
	}

	/**
	 * Determine whether set y-value to zero when there is no data in that
	 * interval.
	 * 
	 * @return true if yes, otherwise false
	 */
	public boolean isSetZeroIfIntervalNoData() {
		return setZeroIfIntervalNoData;
	}

	/**
	 * Specify whether set y-value to zero when there is no data in that
	 * interval.
	 * 
	 * @param setZeroIfIntervalNoData
	 *            true if yes, otherwise false
	 */
	public void setSetZeroIfIntervalNoData(boolean setZeroIfIntervalNoData) {
		this.setZeroIfIntervalNoData = setZeroIfIntervalNoData;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
