package perfcharts.formatter;

import perfcharts.chart.JmeterSummaryChart;

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
	public String format(JmeterSummaryChart chart) throws Exception;
}
