package perfcharts.configtemplate;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

import java.util.ArrayList;
import java.util.List;

public class JmeterThreadChartTemplate extends Chart2DTemplateWithIntervalBase {
    @Override
    public Chart2DConfig generateChartConfig() {
        FieldSelector timestampField = new IndexFieldSelector(1);
        FieldSelector threadsField = new IndexFieldSelector(2);
        List<Chart2DSeriesConfigRule> rules = new ArrayList<Chart2DSeriesConfigRule>();
        rules.add(new Chart2DSeriesConfigRule("^TX-(.+)-[SF]$",
                "Virtual Users", "", getLabelField(), timestampField,
                threadsField, new AverageCalculation(getInterval())));
        Chart2DConfig cfg = createConfig("Concurrent Virtual Users", "Time",
                "Concurrent Virtual Users", rules, AxisMode.TIME);
        cfg.setKey("virtual-users");
        return cfg;
    }
}
