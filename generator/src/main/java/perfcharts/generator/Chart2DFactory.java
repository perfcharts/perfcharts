package perfcharts.generator;

import perfcharts.chart.Chart2D;
import perfcharts.config.ChartConfig;
import perfcharts.formatter.Chart2DFormatter;
import perfcharts.formatter.ChartFormatter;

/**
 * A factory for creating creating {@link ChartGenerator} and {@link ChartFormatter}
 * objects for {@link Chart2D}
 * 
 * @author Rayson Zhu
 *
 */
public interface Chart2DFactory extends ChartFactory<Chart2D> {
	@Override
	public Chart2DGenerator createGenerator(ChartConfig<Chart2D> config)
			throws Exception;

	@Override
	public Chart2DFormatter createFormatter() throws Exception;
}
