package perfcharts.formatter;

import perfcharts.chart.Chart2D;

/**
 * Format a {@link Chart2D} to string.
 * 
 * @author Rayson Zhu
 *
 */
public interface Chart2DFormatter extends ChartFormatter<Chart2D> {
	public String format(Chart2D chart) throws Exception;
}
