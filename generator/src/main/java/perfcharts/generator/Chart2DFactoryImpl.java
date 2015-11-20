package perfcharts.generator;

import perfcharts.chart.Chart2D;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.ChartConfig;
import perfcharts.formatter.Chart2DFormatter;
import perfcharts.formatter.FlotChartFormatter;

/**
 * A default {@link Chart2DFactory} implementation.
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2DFactoryImpl implements Chart2DFactory {

	@Override
	public Chart2DGenerator createGenerator(ChartConfig<Chart2D> config)
			throws Exception {
		Chart2DConfig cfg = (Chart2DConfig) config;
		return new Chart2DGenerator(this, cfg);
	}

	@Override
	public Chart2DFormatter createFormatter() throws Exception {
		return new FlotChartFormatter();
	}

}
