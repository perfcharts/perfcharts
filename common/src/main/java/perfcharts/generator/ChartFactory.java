package perfcharts.generator;

import perfcharts.chart.Chart;
import perfcharts.config.ChartConfig;
import perfcharts.formatter.ChartFormatter;

/**
 * A factory interface for creating {@link ChartGenerator} and {@link ChartFormatter}
 * objects for specific type of {@link Chart}
 * 
 * @param T
 *            the type of {@link Chart}
 * @author Rayson Zhu
 *
 */
public interface ChartFactory<T extends Chart> {
	/**
	 * Create a generator for this kind of chart.
	 * 
	 * @param config
	 *            a configuration object
	 * @return a generator
	 *
	 */
	public ChartGenerator createGenerator(ChartConfig<T> config) throws Exception;

	/**
	 * Create a formatter for this kind of chart.
	 * 
	 * @return a formatter
	 *
	 */
	public ChartFormatter<T> createFormatter() throws Exception;
}
