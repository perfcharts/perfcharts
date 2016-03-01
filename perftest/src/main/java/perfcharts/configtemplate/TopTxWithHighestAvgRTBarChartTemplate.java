package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import perfcharts.calc.AverageCalculation;
import perfcharts.chart.Chart2D;
import perfcharts.chart.Chart2DSeries;
import perfcharts.chart.Point2D;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.common.StringExtractionTransformSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.BarChartStringIDMapper;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;
import perfcharts.config.SeriesOrder;

public class TopTxWithHighestAvgRTBarChartTemplate extends Chart2DTemplateBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		List<Chart2DSeriesConfigRule> rules;
		FieldSelector<Double> rtField = new IndexFieldSelector<Double>(5);
		rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("TX-(.+)-S", "Tx", "ms",
				getLabelField(), new StringExtractionTransformSelector(
						getLabelField(), "TX-(.+)-S", "$1"), rtField,
				new AverageCalculation(), false, true, false));
		Chart2DConfig cfg = createConfig(
				" Top 10 Slow Transactions ",
				"", "Response Time", rules, AxisMode.BAR_STRING);
		cfg.setSeriesOrder(SeriesOrder.NONE);
		cfg.setStringIDMapper(new BarChartStringIDMapper() {
			@Override
			public Map<String, Integer> map(Chart2D chart, Chart2DConfig config) {
				if (chart.getLines() == null || chart.getLines().isEmpty())
					return null;
				Chart2DSeries series = chart.getLines().get(0);
				Map<String, Integer> result = new HashMap<String, Integer>();
				List<Point2D> copyOfStops = new ArrayList<Point2D>(series
						.getStops().size());

				for (Point2D point : series.getStops()) {
					copyOfStops.add(new Point2D(point.getX(), point.getY(), point.getWeight()));
				}
				Collections.sort(copyOfStops, new Comparator<Point2D>() {
					@Override
					public int compare(Point2D o1, Point2D o2) {
						double d = o1.getY() - o2.getY();
						if (d < 0)
							return 1;
						if (d > 0)
							return -1;
						return 0;
					}
				});
				for (int i = 0; i < copyOfStops.size();) {
					result.put(copyOfStops.get(i).getX().toString(), ++i);
				}
				series.setStops(copyOfStops);
				return result;
			}
		});
		cfg.setInterval(1); // disable auto interval
		cfg.setKey("top-slow-tx");
		return cfg;
	}
}
