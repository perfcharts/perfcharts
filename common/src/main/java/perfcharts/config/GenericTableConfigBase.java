package perfcharts.config;

import perfcharts.chart.GenericTable;
import perfcharts.generator.GenericTableFactory;

import java.io.IOException;

public abstract class GenericTableConfigBase extends ChartConfigBase<GenericTable> {

	@Override
	public abstract GenericTableFactory createChartFactory() throws IOException, InterruptedException;

}
