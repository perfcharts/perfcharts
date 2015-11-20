package perfcharts.config;

import perfcharts.generator.GenericTableFactory;
import perfcharts.generator.PerformanceComparisonTableFactoryImpl;

public class PerformanceComparisonTableConfig extends GenericTableConfigBase {

	@Override
	public GenericTableFactory createChartFactory()
			throws Exception {
		return new PerformanceComparisonTableFactoryImpl();
	}

}
