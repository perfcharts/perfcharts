package perfcharts.chart;

import java.util.List;

import perfcharts.formatter.JmeterSummaryChartFormatter;

/**
 * Represents a Jmeter summary chart
 * 
 * @author Rayson Zhu
 *
 */
@Deprecated
public class JmeterSummaryChart extends Chart {
	/**
	 * the formatter that the chart uses
	 */
	private JmeterSummaryChartFormatter formatter;
	/**
	 * series (rows) that the chart contains
	 */
	private List<List<Object>> series;
	/**
	 * column labels
	 */
	private List<String> columnLabels;

	/**
	 * Initialize an empty Jmeter summary chart
	 */
	public JmeterSummaryChart() {
	}

	/**
	 * Initialize a Jmeter summary chart with specified parameters
	 * 
	 * @param title
	 *            title of the chart
	 * @param subtitle
	 *            subtitle of the chart
	 * @param columnLabels
	 *            column labels
	 * @param series
	 *            series (rows) that the chart contains
	 */
	public JmeterSummaryChart(String title, String subtitle,
			List<String> columnLabels, List<List<Object>> series) {
		super(title, subtitle);
		this.series = series;
		this.columnLabels = columnLabels;
	}

	@Override
	public String format() throws Exception {
		return formatter.format(this);
	}

	/**
	 * Get series (rows) that the chart contains.
	 * 
	 * @return series (rows)
	 */
	public List<List<Object>> getSeries() {
		return series;
	}

	/**
	 * Set series (rows) that the chart contains.
	 * 
	 * @param series
	 *            series (rows)
	 */
	public void setSeries(List<List<Object>> series) {
		this.series = series;
	}

	/**
	 * Get column labels.
	 * 
	 * @return column labels
	 */
	public List<String> getColumnLabels() {
		return columnLabels;
	}

	/**
	 * Set column labels.
	 * 
	 * @param columnLabels
	 *            column labels
	 */
	public void setColumnLabels(List<String> columnLabels) {
		this.columnLabels = columnLabels;
	}

	/**
	 * Get the formatter that the chart uses.
	 * 
	 * @return a formatter
	 */
	public JmeterSummaryChartFormatter getFormatter() {
		return formatter;
	}

	/**
	 * Set the formatter that the chart uses.
	 * 
	 * @param formatter
	 *            a formatter
	 */
	public void setFormatter(JmeterSummaryChartFormatter formatter) {
		this.formatter = formatter;
	}

}
