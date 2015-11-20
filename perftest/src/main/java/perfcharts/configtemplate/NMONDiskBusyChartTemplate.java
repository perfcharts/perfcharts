package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.List;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

public class NMONDiskBusyChartTemplate extends Chart2DTemplateWithIntervalBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		int interval = getInterval();
		FieldSelector timestampField = new IndexFieldSelector(1);
		FieldSelector labelField = getLabelField();
		FieldSelector diskBusyField = new IndexFieldSelector(2);
		List<Chart2DSeriesConfigRule> rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("^DISKBUSY-(.+)$", "Disk-$1", "%", labelField,
				timestampField, diskBusyField, new AverageCalculation(interval)));
//		rules.add(new Chart2DSeriesConfigRule("^DISKBUSY-(.+)$", "Disk-Total", "%", labelField,
//				timestampField, diskBusyField, new SumByLabelCalculation(labelField,
//						interval)));
		return createConfig("Disk Busy% Over Time", "Time", "Disk Busy", rules, AxisMode.TIME);
	}

}
