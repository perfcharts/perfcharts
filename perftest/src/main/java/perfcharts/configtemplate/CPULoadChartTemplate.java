package perfcharts.configtemplate;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

import java.util.ArrayList;
import java.util.List;

public class CPULoadChartTemplate extends Chart2DTemplateWithIntervalBase {
	@Override
	public Chart2DConfig generateChartConfig() {
		int interval = getInterval();
		FieldSelector timestampField = new IndexFieldSelector(1);
		FieldSelector labelField = getLabelField();
		FieldSelector cpuload1min = new IndexFieldSelector(2);
		FieldSelector cpuload5min = new IndexFieldSelector(3);
		FieldSelector cpuload15min = new IndexFieldSelector(4);
		FieldSelector coresField = new IndexFieldSelector(5);
		FieldSelector singleValueField = cpuload1min;
		List<Chart2DSeriesConfigRule> rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD$", "CPU load-1 min", "", labelField,
				timestampField, cpuload1min, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD_1MIN$", "CPU load-1 min", "", labelField,
				timestampField, singleValueField, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD$", "CPU load-5 min", "", labelField,
				timestampField, cpuload5min, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD_5MIN$", "CPU load-5 min", "", labelField,
				timestampField, singleValueField, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD$", "CPU load-15 min", "", labelField,
				timestampField, cpuload15min, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD_15MIN$", "CPU load-15 min", "", labelField,
				timestampField, singleValueField, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPULOAD$", "CPUs", "", labelField,
				timestampField, coresField, new AverageCalculation(interval)));
		rules.add(new Chart2DSeriesConfigRule("^CPUNUM$", "CPUs", "", labelField,
				timestampField, singleValueField, new AverageCalculation(interval)));
		Chart2DConfig cfg = createConfig("CPU Load over Time", "Time", "CPU load", rules,
				AxisMode.TIME);
		cfg.setKey("cpu-load");
		return cfg;
	}

}
