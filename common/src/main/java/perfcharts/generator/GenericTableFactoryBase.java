package perfcharts.generator;

import perfcharts.chart.GenericTable;
import perfcharts.config.ChartConfig;
import perfcharts.formatter.ChartFormatter;
import perfcharts.formatter.GenericTableFormatterImpl;

public abstract class GenericTableFactoryBase implements GenericTableFactory {

	@Override
	public abstract ChartGenerator createGenerator(ChartConfig<GenericTable> config)
			throws Exception;

	@Override
	public ChartFormatter<GenericTable> createFormatter() throws Exception {
		return new GenericTableFormatterImpl();
	}


}
