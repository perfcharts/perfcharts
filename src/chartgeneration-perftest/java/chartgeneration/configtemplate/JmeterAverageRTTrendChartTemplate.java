package chartgeneration.configtemplate;

import java.util.ArrayList;
import java.util.List;

import chartgeneration.calc.AverageCalculation;
import chartgeneration.common.FieldSelector;
import chartgeneration.common.IndexFieldSelector;
import chartgeneration.config.AxisMode;
import chartgeneration.config.Chart2DConfig;
import chartgeneration.config.Chart2DSeriesConfigRule;
import chartgeneration.tick.LongStringTickGenerator;

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
