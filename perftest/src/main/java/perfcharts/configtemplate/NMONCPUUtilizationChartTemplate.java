package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.List;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

public class NMONCPUUtilizationChartTemplate extends
		Chart2DTemplateWithIntervalBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		FieldSelector timestampField = new IndexFieldSelector(1);
		FieldSelector userField = new IndexFieldSelector(2);
		FieldSelector systemField = new IndexFieldSelector(3);
		FieldSelector waitField = new IndexFieldSelector(4);
		FieldSelector totalField = new IndexFieldSelector(5);
		FieldSelector cpusField = new IndexFieldSelector(6);
		FieldSelector singleItemField = userField;
		List<Chart2DSeriesConfigRule> rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("^CPU$", "User%", "%",
				getLabelField(), timestampField, userField,
				new AverageCalculation(getInterval())));
		rules.add(new Chart2DSeriesConfigRule("^CPUUTIL_USER$", "User%", "%",
				getLabelField(), timestampField, singleItemField,
				new AverageCalculation(getInterval())));
		
		rules.add(new Chart2DSeriesConfigRule("^CPU$", "System%", "%",
				getLabelField(), timestampField, systemField,
				new AverageCalculation(getInterval())));
		rules.add(new Chart2DSeriesConfigRule("^CPUUTIL_SYS$", "System%", "%",
				getLabelField(), timestampField, singleItemField,
				new AverageCalculation(getInterval())));
		
		
		rules.add(new Chart2DSeriesConfigRule("^CPU$", "Wait%", "%",
				getLabelField(), timestampField, waitField,
				new AverageCalculation(getInterval())));
		rules.add(new Chart2DSeriesConfigRule("^CPUUTIL_WAIT$", "Wait%", "%",
				getLabelField(), timestampField, singleItemField,
				new AverageCalculation(getInterval())));
		
		rules.add(new Chart2DSeriesConfigRule("^CPU$", "Used%", "%",
				getLabelField(), timestampField, totalField,
				new AverageCalculation(getInterval())));
		rules.add(new Chart2DSeriesConfigRule("^CPUUTIL_USED$", "Used%", "%",
				getLabelField(), timestampField, singleItemField,
				new AverageCalculation(getInterval())));
		
		
		rules.add(new Chart2DSeriesConfigRule("^CPU$", "CPUs", "cores",
				getLabelField(), timestampField, cpusField,
				new AverageCalculation(getInterval())));
		rules.add(new Chart2DSeriesConfigRule("^CPUNUM$", "CPUs", "cores",
				getLabelField(), timestampField, singleItemField,
				new AverageCalculation(getInterval())));
		
		
		return createConfig("CPU Utilization over Time", "Time",
				"CPU Utilization", rules, AxisMode.TIME);
	}

}
