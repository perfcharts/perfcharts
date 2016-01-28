package perfcharts.formatter;

import perfcharts.chart.Chart;

import java.io.IOException;

/**
 * A {@link ChartFormatter} can format a generated {@link Chart} to string.
 * 
 * @author Rayson Zhu
 *
 * @param <T> the type of {@link Chart}
 */
public interface ChartFormatter<T extends Chart> {
	/**
	 * format a specified {@link Chart} to string
	 * @param chart a {@link Chart}
	 * @return formatted string
	 * @throws Exception
	 */
	public String format(T chart) throws IOException, InterruptedException;
}
