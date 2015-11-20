package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import perfcharts.calc.PerfCmpBarChartCalculation;

public class PerformanceComparisonChartTemplateOrderedByHits extends
		Chart2DTemplateBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		List<Chart2DSeriesConfigRule> rules;
		FieldSelector<Long> sourceSamplesField = new IndexFieldSelector(1);
		FieldSelector<Long> destSamplesField = new IndexFieldSelector(2);
		FieldSelector<Double> sourceRTField = new IndexFieldSelector<Double>(3);
		FieldSelector<Double> destRTField = new IndexFieldSelector<Double>(4);
		FieldSelector<String> sourceSeriesLabelField = new IndexFieldSelector<String>(13);
        FieldSelector<String> destSeriesLabelField = new IndexFieldSelector<String>(14);
		rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("TX-.+", destSeriesLabelField, "ms",
				getLabelField(), new StringExtractionTransformSelector(
						getLabelField(), "TX-(.+)", "$1"), destRTField,
				new PerfCmpBarChartCalculation(destSamplesField), false, true,
				false));
		rules.add(new Chart2DSeriesConfigRule("TX-.+", sourceSeriesLabelField, "ms",
				getLabelField(), new StringExtractionTransformSelector(
						getLabelField(), "TX-(.+)", "$1"), sourceRTField,
				new PerfCmpBarChartCalculation(sourceSamplesField), false,
				true, false));
		Chart2DConfig cfg = createConfig(
				"Transactions Average Response Time Comparison (Ordered by Hits)",
				"", "Response Time", rules, AxisMode.BAR_STRING);
		cfg.setSeriesOrder(SeriesOrder.NONE);
		cfg.setInterval(1); // disable auto interval
		cfg.setStringIDMapper(new BarChartStringIDMapper() {
			@Override
			public Map<String, Integer> map(Chart2D chart, Chart2DConfig config) {
				if (chart.getLines() == null || chart.getLines().size() < 2)
					return null;
				Map<String, Integer> result = new HashMap<String, Integer>();
				Chart2DSeries comparedSeries = chart.getLines().get(0);
				Chart2DSeries thisSeries = chart.getLines().get(1);
				Collections.sort(thisSeries.getStops(),
						new Comparator<Point2D>() {
							@Override
							public int compare(Point2D o1, Point2D o2) {
//								System.err.println(o2.getWeight() + " "
//										+ o1.getWeight() + " "
//										+ (o2.getWeight() - o1.getWeight()));
								return o2.getWeight() - o1.getWeight();
							}
						});

				for (int i = 0; i < thisSeries.getStops().size();) {
					String key = thisSeries.getStops().get(i).getX().toString();
					result.put(key, ++i);
				}
				return result;
			}
		});
		return cfg;
	}
}