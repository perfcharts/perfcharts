package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.List;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.AddTransformSelector;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;
import perfcharts.config.Chart2DSeriesExclusionRule;

public class JmeterAverageRTChartTemplate extends
		Chart2DTemplateWithIntervalBase {

	private String exclusionPattern;

	@Override
	public Chart2DConfig generateChartConfig() {
		List<Chart2DSeriesConfigRule> rules;
		FieldSelector timestampField = new IndexFieldSelector(1);
		FieldSelector rtField = new IndexFieldSelector(5);
		FieldSelector xField = new AddTransformSelector(timestampField, rtField);
		rules = new ArrayList<Chart2DSeriesConfigRule>();
		Chart2DSeriesConfigRule rule = new Chart2DSeriesConfigRule(
				"^TX-(.+)-S$", "Average Response Time", "ms", getLabelField(),
				xField, rtField, new AverageCalculation(getInterval()));
		if (exclusionPattern != null && !exclusionPattern.isEmpty())
			rule.setExclusionRule(new Chart2DSeriesExclusionRule("$1",
					exclusionPattern));
		rules.add(rule);
		Chart2DConfig config = createConfig("Average Response Time over Time", "Time",
				"Response Time", rules, AxisMode.TIME);
		config.setKey("weighted-avg-rt");
		return config;
	}

	public String getExclusionPattern() {
		return exclusionPattern;
	}

	public JmeterAverageRTChartTemplate setExclusionPattern(String exclusionPattern) {
		this.exclusionPattern = exclusionPattern;
		return this;
	}
}
