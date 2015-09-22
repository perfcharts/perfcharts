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
import chartgeneration.config.SeriesOrder;
import chartgeneration.configtemplate.Chart2DTemplateWithIntervalBase;

public class JmeterTPSChartTemplate extends Chart2DTemplateWithIntervalBase {

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
		rules.add(new Chart2DSeriesConfigRule("^TX-(.+)-S$", "$1-Success", "", getLabelField(),
				xField, null, new CountCalculation(interval, 1000.0 / interval, true), true, false, false));
		rules.add(new Chart2DSeriesConfigRule("^TX-(.+)-F$", "$1-Failure", "", getLabelField(),
				xField, null, new CountCalculation(interval, 1000.0 / interval, true), true, false, false));
		Chart2DConfig cfg =  createConfig("TPS over Time", "Time", "TPS", rules, AxisMode.TIME);
		cfg.setSeriesOrder(SeriesOrder.SERIES_LABEL);
		cfg.setInterval(interval);
		return cfg;
	}

}
