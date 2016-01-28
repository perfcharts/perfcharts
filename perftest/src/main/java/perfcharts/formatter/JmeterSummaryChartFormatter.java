package perfcharts.formatter;

import perfcharts.chart.JmeterSummaryChart;

import java.io.IOException;

/**
 * Provides the support for formatting a {@link JmeterSummaryChart} to JSON
 * string.
 * 
 * @author Rayson Zhu
 *
 */
@Deprecated
public interface JmeterSummaryChartFormatter extends
		ChartFormatter<JmeterSummaryChart> {
	public String format(JmeterSummaryChart chart) throws IOException, InterruptedException;
}
