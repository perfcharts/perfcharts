package perfcharts.generator;

import perfcharts.chart.GenericTable;
import perfcharts.config.ChartConfig;
import perfcharts.formatter.GenericTableFormatter;
import perfcharts.formatter.GenericTableFormatterImpl;
import perfcharts.config.JmeterSummaryChartConfig;

/**
 * A default {@link JmeterSummaryChartFactory} implementation.
 * 
 * @author Rayson Zhu
 *
 */
public class JmeterSummaryChartFactoryImpl implements JmeterSummaryChartFactory {

	@Override
	public JmeterSummaryChartGenerator createGenerator(
			ChartConfig<GenericTable> config) throws Exception {
		return new JmeterSummaryChartGenerator(this,
				(JmeterSummaryChartConfig) config);
	}

	@Override
	public GenericTableFormatter createFormatter() throws Exception {
		return new GenericTableFormatterImpl();
	}

}
