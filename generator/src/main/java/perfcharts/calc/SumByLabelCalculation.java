package perfcharts.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import perfcharts.chart.Point2D;
import perfcharts.common.FieldSelector;

/**
 * This calculation groups data rows by their x-fields, sums up the the average
 * of y-fields having the same row label, and makes the result as y-values.
 * 
 * @author Rayson Zhu
 *
 */
public class SumByLabelCalculation implements Chart2DCalculation {
	/**
	 * the interval for point merging
	 */
	private int interval = 0;
	/**
	 * the times to multiply on y-value of each point
	 */
	private int times = 1;
	/**
	 * the label field selector
	 */
	private FieldSelector labelSelector;

	/**
 * 
 */
	public SumByLabelCalculation() {

	}

	/**
	 * 
	 * @param labelSelector
	 *            the label field selector
	 */
	public SumByLabelCalculation(FieldSelector labelSelector) {
		this.labelSelector = labelSelector;
	}

	/**
	 * 
	 * @param labelSelector
	 *            the label field selector
	 * @param interval
	 *            the interval for point merging
	 */
	public SumByLabelCalculation(FieldSelector labelSelector, int interval) {
		this.labelSelector = labelSelector;
		this.interval = interval;
	}

	/**
	 * constructor
	 * 
	 * @param labelSelector
	 *            the label field selector
	 * @param interval
	 *            the interval for point merging
	 * @param times
	 *            the times to multiply on y-value of each point
	 */
	public SumByLabelCalculation(FieldSelector labelSelector, int interval,
			int times) {
		this.labelSelector = labelSelector;
		this.interval = interval;
		this.times = times;
	}

	public List<Point2D> produce(List<List<Object>> rows, FieldSelector xField,
								 FieldSelector yField) {
		List<Point2D> stops = new LinkedList<Point2D>();
		if (rows.isEmpty())
			return stops;
		Map<String, Integer> labelIndexMap = new HashMap<String, Integer>();
		List<Double> valueList = new ArrayList<Double>();
		List<Integer> countList = new ArrayList<Integer>();
		Comparable<?> firstX = (Comparable<?>)xField.select(rows.get(0));
		Comparable<?> lastX = firstX;
		for (List<Object> row : rows) {
			Comparable<?> x = (Comparable<?>)xField.select(row);
			if (interval > 1) {
				Number _x = (Number)x;
				Number _firstX = (Number)firstX;
				x = _firstX.longValue() + (_x.longValue() - _firstX.longValue())
						/ interval * interval;
			}
			String label = labelSelector.select(row).toString();
			Integer indexWrapper = labelIndexMap.get(label);
			int index = indexWrapper != null ? indexWrapper.intValue() : -1;
			double value = ((Number) yField.select(row)).doubleValue();
			if (index < 0) {
				index = countList.size();
				labelIndexMap.put(label, index);
				countList.add(0);
				valueList.add(0.0);
			}
			if (x.equals(lastX)) {
				valueList
						.set(index, valueList.get(index).doubleValue() + value);
				countList.set(index, countList.get(index).intValue() + 1);
			} else {
				double y = 0.0;
				int count = 0;
				for (int i = 0; i < countList.size(); ++i) {
					int n = countList.get(i);
					if (n > 0) {
						y += valueList.get(i).doubleValue() / n;
						++count;
					}
				}
				if (count > 0)
					stops.add(new Point2D(lastX, y * times, count));

				for (int i = 0; i < countList.size(); i++) {
					countList.set(i, 0);
				}
				for (int i = 0; i < valueList.size(); i++) {
					valueList.set(i, 0.0);
				}
				valueList.set(index, value);
				countList.set(index, 1);
			}
			lastX = x;
		}
		double y = 0.0;
		int count = 0;
		for (int i = 0; i < countList.size(); ++i) {
			int n = countList.get(i);
			if (n > 0) {
				y += valueList.get(i) / n;
				++count;
			}
		}
		if (count > 0)
			stops.add(new Point2D(lastX, y * times, count));
		return stops;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * Get the times to multiply on y-value of each point.
	 * 
	 * @return the times
	 */
	public int getTimes() {
		return times;
	}

	/**
	 * Set the times to multiply on y-value of each point.
	 * 
	 * @param times
	 *            the times
	 */
	public void setTimes(int times) {
		this.times = times;
	}

	/**
	 * Get the label field selector
	 * 
	 * @return the label field selector
	 */
	public FieldSelector getLabelSelector() {
		return labelSelector;
	}

	/**
	 * Set the label field selector
	 * 
	 * @param labelSelector
	 *            the label field selector
	 */
	public void setLabelSelector(FieldSelector labelSelector) {
		this.labelSelector = labelSelector;
	}
}
