package perfcharts.config;

import perfcharts.generator.GenericTableFactory;
import perfcharts.generator.PerformanceComparisonTableFactoryImpl;

import java.io.IOException;

public class PerformanceComparisonTableConfig extends GenericTableConfigBase {

	@Override
	public GenericTableFactory createChartFactory()
			throws IOException, InterruptedException {
		return new PerformanceComparisonTableFactoryImpl();
	}

}
