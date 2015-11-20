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

public class TopTxWithMostHitsBarChartTemplate extends Chart2DTemplateBase {

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
				" Top 10 Hits Transctions",
				"", "Response Time", rules, AxisMode.BAR_STRING);
		cfg.setSeriesOrder(SeriesOrder.NONE);
		cfg.setStringIDMapper(new BarChartStringIDMapper() {
			@Override
			public Map<String, Integer> map(Chart2D chart, Chart2DConfig config) {
				if (chart.getLines() == null || chart.getLines().isEmpty())
					return null;
				Chart2DSeries series = chart.getLines().get(0);
				Map<String, Integer> result = new HashMap<String, Integer>();
				Collections.sort(series.getStops(), new Comparator<Point2D>() {
					@Override
					public int compare(Point2D o1, Point2D o2) {
						return o2.getWeight() - o1.getWeight();
					}
				});
				for (int i = 0; i < series.getStops().size();) {
					result.put(series.getStops().get(i).getX().toString(), ++i);
				}
				return result;
			}
		});
		cfg.setInterval(1); // disable auto interval
		return cfg;
	}
}
