package chartgeneration.configtemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chartgeneration.calc.EmptyCalculation;
import chartgeneration.chart.Chart2D;
import chartgeneration.common.FieldSelector;
import chartgeneration.common.IndexFieldSelector;
import chartgeneration.config.*;
import chartgeneration.tick.LongStringTickGenerator;
public class JmeterRTTrendChartTemplate extends Chart2DTemplateBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		List<Chart2DSeriesConfigRule> rules;
		FieldSelector xField = new IndexFieldSelector(1);
		FieldSelector rtField = new IndexFieldSelector(2);
		FieldSelector buildIDField = rtField;
		rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("^TX-(.+)$", "$1", "ms",
				getLabelField(), xField, rtField, new EmptyCalculation(), true,
				false, false));
		Chart2DConfig cfg = createConfig("Response Time Trend", "Build", "Response Time", rules,
				AxisMode.INTEGER);
		cfg.setXTickGenerator(new LongStringTickGenerator("^XTICK$", getLabelField(), xField, buildIDField));
		cfg.setSeriesOrder(SeriesOrder.SERIES_LABEL);
        return cfg;
	}

}
