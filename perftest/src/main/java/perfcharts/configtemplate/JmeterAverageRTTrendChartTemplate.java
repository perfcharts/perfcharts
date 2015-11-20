package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.List;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;
import perfcharts.tick.LongStringTickGenerator;

public class JmeterAverageRTTrendChartTemplate extends Chart2DTemplateBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		List<Chart2DSeriesConfigRule> rules;
		FieldSelector xField = new IndexFieldSelector(1);
		FieldSelector rtField = new IndexFieldSelector(2);
		FieldSelector buildIDField = rtField;
		rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("^TOTAL$",
				"Average Response Time", "ms", getLabelField(), xField,
				rtField, new AverageCalculation(), true, false, false));
		Chart2DConfig cfg = createConfig("Average Response Time Trend",
				"Build", "Response Time", rules, AxisMode.INTEGER);
		cfg.setXTickGenerator(new LongStringTickGenerator("^XTICK$",
				getLabelField(), xField, buildIDField));
		return cfg;
	}

}
