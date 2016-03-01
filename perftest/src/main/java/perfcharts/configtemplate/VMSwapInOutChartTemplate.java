package perfcharts.configtemplate;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

import java.util.ArrayList;
import java.util.List;

public class VMSwapInOutChartTemplate extends Chart2DTemplateWithIntervalBase {
    @Override
    public Chart2DConfig generateChartConfig() {
        int interval = getInterval();
        FieldSelector timestampField = new IndexFieldSelector(1);
        FieldSelector labelField = getLabelField();
        FieldSelector swapInField = new IndexFieldSelector(2);
        FieldSelector swapOutField = new IndexFieldSelector(3);
        List<Chart2DSeriesConfigRule> rules = new ArrayList<Chart2DSeriesConfigRule>();
        rules.add(new Chart2DSeriesConfigRule("^VM$", "Swap In", "Pages / s",
                labelField, timestampField, swapInField,
                new AverageCalculation(interval)));
        rules.add(new Chart2DSeriesConfigRule("^VM$", "Swap Out", "Pages / s",
                labelField, timestampField, swapOutField,
                new AverageCalculation(interval)));

        FieldSelector singleValueField = swapInField;
        rules.add(new Chart2DSeriesConfigRule("^SWAP_IN$", "Swap In", "Pages / s",
                labelField, timestampField, singleValueField,
                new AverageCalculation(interval)));
        rules.add(new Chart2DSeriesConfigRule("^SWAP_OUT$", "Swap Out", "Pages / s",
                labelField, timestampField, singleValueField,
                new AverageCalculation(interval)));
        Chart2DConfig cfg = createConfig("Page Swap In / Out", "Time", "Pages / s", rules,
                AxisMode.TIME);
        cfg.setKey("nmon-vm-swap");
        return cfg;
    }

}
