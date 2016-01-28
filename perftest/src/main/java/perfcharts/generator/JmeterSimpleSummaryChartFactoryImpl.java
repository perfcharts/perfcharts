package perfcharts.generator;

import perfcharts.chart.GenericTable;
import perfcharts.config.ChartConfig;
import perfcharts.config.JmeterSimpleSummaryChartConfig;
import perfcharts.formatter.GenericTableFormatter;
import perfcharts.formatter.GenericTableFormatterImpl;

/**
 * A default {@link JmeterSummaryChartFactory} implementation.
 * 
 * @author Rayson Zhu
 *
 */
public class JmeterSimpleSummaryChartFactoryImpl implements JmeterSummaryChartFactory {

	@Override
	public JmeterSimpleSummaryChartGenerator createGenerator(
			ChartConfig<GenericTable> config) throws Exception {
		return new JmeterSimpleSummaryChartGenerator(this,
				(JmeterSimpleSummaryChartConfig) config);
	}

	@Override
	public GenericTableFormatter createFormatter() throws Exception {
		return new GenericTableFormatterImpl();
	}

}
