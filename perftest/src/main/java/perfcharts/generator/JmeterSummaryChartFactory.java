package perfcharts.generator;

import perfcharts.chart.GenericTable;
import perfcharts.config.ChartConfig;
import perfcharts.formatter.ChartFormatter;
import perfcharts.formatter.GenericTableFormatter;

/**
 * A factory for creating creating {@link ChartGenerator} and {@link ChartFormatter}
 * objects for {@link JmeterSummaryChart}
 * 
 * @author Rayson Zhu
 *
 */
public interface JmeterSummaryChartFactory extends
		GenericTableFactory {
	@Override
	public JmeterSummaryChartGenerator createGenerator(ChartConfig<GenericTable> config) throws Exception;

	@Override
	public GenericTableFormatter createFormatter() throws Exception;
}
