package perfcharts.config;

import perfcharts.chart.Chart;
import perfcharts.generator.ChartFactory;

import java.io.IOException;

/**
 * Represents a chart configuration.
 * A ChartConfig interface defines the {@link #createChartFactory()} method,
 * which creates a factory object for generating corresponding chart.
 * 
 * @author Rayson Zhu
 *
 * @param <T> the type of chart
 */
public interface ChartConfig<T extends Chart> {
	/**
	 * create a factory object for generating this chart
	 * @return
	 *
	 */
	public ChartFactory<T> createChartFactory() throws IOException, InterruptedException;
}
