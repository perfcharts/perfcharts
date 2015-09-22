package chartgeneration.configtemplate;

import java.util.ArrayList;
import java.util.List;

import chartgeneration.calc.CountCalculation;
import chartgeneration.common.AddTransformSelector;
import chartgeneration.common.FieldSelector;
import chartgeneration.common.IndexFieldSelector;
import chartgeneration.config.AxisMode;
import chartgeneration.config.Chart2DConfig;
import chartgeneration.config.Chart2DSeriesConfigRule;
import chartgeneration.config.Chart2DSeriesExclusionRule;
import chartgeneration.configtemplate.Chart2DTemplateWithIntervalBase;

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
		return cfg;
	}

	public String getExclusionPattern() {
		return exclusionPattern;
	}

	public void setExclusionPattern(String exclusionPattern) {
		this.exclusionPattern = exclusionPattern;
	}

}
