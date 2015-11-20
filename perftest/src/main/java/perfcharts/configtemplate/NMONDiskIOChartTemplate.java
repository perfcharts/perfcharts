package perfcharts.configtemplate;

import java.util.ArrayList;
import java.util.List;

import perfcharts.calc.AverageCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

public class NMONDiskIOChartTemplate extends Chart2DTemplateWithIntervalBase {

	@Override
	public Chart2DConfig generateChartConfig() {
		int interval = getInterval();
		FieldSelector timestampField = new IndexFieldSelector(1);
		FieldSelector labelField = getLabelField();
		FieldSelector diskIOField = new IndexFieldSelector(2);
		List<Chart2DSeriesConfigRule> rules = new ArrayList<Chart2DSeriesConfigRule>();
		rules.add(new Chart2DSeriesConfigRule("^DISKREAD-(.+)$", "Disk-$1-Read", "KiB/s",
				labelField, timestampField, diskIOField,
				new AverageCalculation(interval)));
//		rules.add(new Chart2DSeriesConfigRule("^DISKREAD-(.+)$", "Disk-Total-Read", "KiB/s",
//				labelField, timestampField, diskIOField, new SumByLabelCalculation(
//						labelField, interval)));
		rules.add(new Chart2DSeriesConfigRule("^DISKWRITE-(.+)$", "Disk-$1-Write", "KiB/s",
				labelField, timestampField, diskIOField,
				new AverageCalculation(interval)));
//		rules.add(new Chart2DSeriesConfigRule("^DISKWRITE-(.+)$", "Disk-Total-Write", "KiB/s",
//				labelField, timestampField, diskIOField, new SumByLabelCalculation(
//						labelField, interval)));
		return createConfig("Disk IO", "Time", "Disk IO", rules, AxisMode.TIME);
	}

}
