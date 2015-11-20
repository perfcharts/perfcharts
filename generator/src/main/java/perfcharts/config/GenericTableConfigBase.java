package perfcharts.config;

import perfcharts.chart.GenericTable;
import perfcharts.generator.GenericTableFactory;

public abstract class GenericTableConfigBase extends ChartConfigBase<GenericTable> {

	@Override
	public abstract GenericTableFactory createChartFactory() throws Exception;

}
