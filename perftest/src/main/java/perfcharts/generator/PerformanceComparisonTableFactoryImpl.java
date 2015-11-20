package perfcharts.generator;

import perfcharts.chart.GenericTable;
import perfcharts.config.ChartConfig;
import perfcharts.config.PerformanceComparisonTableConfig;

public class PerformanceComparisonTableFactoryImpl extends
		GenericTableFactoryBase {

	@Override
	public PerformanceComparisonTableGenerator createGenerator(
			ChartConfig<GenericTable> config) throws Exception {
		return new PerformanceComparisonTableGenerator(
				(PerformanceComparisonTableConfig) config);
	}

}
