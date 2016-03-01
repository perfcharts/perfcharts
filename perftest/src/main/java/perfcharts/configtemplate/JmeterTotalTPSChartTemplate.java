package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.List;

import perfcharts.calc.CountCalculation;
import perfcharts.common.AddTransformSelector;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;
import perfcharts.config.Chart2DSeriesExclusionRule;

public class JmeterTotalTPSChartTemplate extends
		Chart2DTemplateWithIntervalBase {
	private String exclusionPattern;

	@Override
	public Chart2DConfig generateChartConfig() {
		int interval = getInterval();
		if (interval < 1)
			interval = 1000;
		List<Chart2DSeriesConfigRule> rules;
		FieldSelector timestampField = new IndexFieldSelector(1);
		FieldSelector rtField = new IndexFieldSelector(5);
		FieldSelector xField = new AddTransformSelector(timestampField, rtField);
		rules = new ArrayList<Chart2DSeriesConfigRule>();
		Chart2DSeriesConfigRule rule1 = new Chart2DSeriesConfigRule(
				"^TX-(.+)-S$", "Transations-Success", "", getLabelField(),
				xField, null, new CountCalculation(interval, 1000.0 / interval, false));
		if (exclusionPattern != null && !exclusionPattern.isEmpty())
			rule1.setExclusionRule(new Chart2DSeriesExclusionRule("$1",
					exclusionPattern));
		rules.add(rule1);
		Chart2DSeriesConfigRule rule2 = new Chart2DSeriesConfigRule(
				"^TX-(.+)-F$", "Transations-Failure", "", getLabelField(),
				xField, null, new CountCalculation(interval, 1000.0 / interval, true));
		if (exclusionPattern != null && !exclusionPattern.isEmpty())
			rule2.setExclusionRule(new Chart2DSeriesExclusionRule("$1",
					exclusionPattern));
		rules.add(rule2);
		Chart2DConfig cfg = createConfig("Total TPS over Time", "Time", "TPS", rules,
				AxisMode.TIME);
		cfg.setInterval(interval);
		cfg.setKey("total-tps");
		return cfg;
	}

	public String getExclusionPattern() {
		return exclusionPattern;
	}

	public JmeterTotalTPSChartTemplate setExclusionPattern(String exclusionPattern) {
		this.exclusionPattern = exclusionPattern;
		return this;
	}

}
