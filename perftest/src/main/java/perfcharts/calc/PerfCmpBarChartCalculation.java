package perfcharts.calc;

import java.util.LinkedList;
import java.util.List;

import perfcharts.chart.Point2D;
import perfcharts.common.FieldSelector;

public class PerfCmpBarChartCalculation implements Chart2DCalculation {
	private FieldSelector<Long> weightField;

	public PerfCmpBarChartCalculation(FieldSelector<Long> weightField) {
		this.weightField = weightField;
	}

	@Override
	public List<Point2D> produce(List<List<Object>> rows, FieldSelector xField,
			FieldSelector yField) {
		List<Point2D> stops = new LinkedList<Point2D>();
		if (rows.isEmpty())
			return stops;
		Comparable<?> firstX = (Comparable<?>) xField.select(rows.get(0));
		Comparable<?> lastX = firstX;
		double y = 0.0;
		int count = 0;
		long lastWeight = 0;
		for (List<Object> row : rows) {
			Comparable<?> x = (Comparable<?>) xField.select(row);
			if (lastX.equals(x)) {
				y += ((Number) yField.select(row)).doubleValue();
				++count;

			} else {
				if (count > 0)
					stops.add(new Point2D(lastX, y / count, (int)lastWeight));
				y = ((Number) yField.select(row)).doubleValue();
				count = 1;
			}
			lastWeight = weightField != null ? weightField.select(row)
					: count;
			lastX = x;
		}
		if (count > 0)
			stops.add(new Point2D(lastX, y / count, (int)lastWeight));
		return stops;
	}

	@Override
	public int getInterval() {
		return 1;
	}

	@Override
	public void setInterval(int interval) {
	}

	public FieldSelector<Long> getWeightField() {
		return weightField;
	}

	public void setWeightField(FieldSelector<Long> weightField) {
		this.weightField = weightField;
	}

}
