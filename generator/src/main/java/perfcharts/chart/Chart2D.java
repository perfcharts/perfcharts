package perfcharts.chart;

import java.util.List;
import java.util.Map;

import perfcharts.config.AxisMode;
import perfcharts.formatter.Chart2DFormatter;
import perfcharts.tick.Ticks;

/**
 * Represents a 2-dimensional chart
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2D extends Chart {
	/**
	 * the x-axis label
	 */
	private String xLabel;
	/**
	 * the y-axis label
	 */
	private String yLabel;
	/**
	 * the x-axis mode
	 */
	private AxisMode xaxisMode;
	/**
	 * series that the chart contains
	 */
	private List<Chart2DSeries> series;
	/**
	 * the formatter that the chart uses
	 */
	private Chart2DFormatter formatter;
	
	/**
	 * optional, custom x-ticks
	 */
	private Ticks xticks;
	
	private Map<String, Integer> barChartStringIDMap;

	/**
	 * Initialize an empty 2-dimensional chart
	 */
	public Chart2D() {
	}

	/**
	 * Initialize a 2-dimensional chart with specified parameters
	 * 
	 * @param title
	 *            title of the chart
	 * @param subtitle
	 *            subtitle of the chart
	 * @param xLabel
	 *            x-axis label of the chart
	 * @param yLabel
	 *            y-axis label of the chart
	 * @param series
	 *            series that the chart contains
	 * @param xaxisMode
	 *            the x-axis mode
	 */
	public Chart2D(String title, String subtitle, String xLabel, String yLabel,
			List<Chart2DSeries> series, AxisMode xaxisMode) {
		super(title, subtitle);
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.series = series;
		this.xaxisMode = xaxisMode;
	}

	/**
	 * get the label of x-axis
	 * 
	 * @return the label of x-axis
	 */
	public String getXLabel() {
		return xLabel;
	}

	/**
	 * set the label of x-axis
	 * 
	 * @param xLabel
	 *            the label of x-axis
	 */
	public void setXLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	/**
	 * get the label of y-axis
	 * 
	 * @return the label of y-axis
	 */
	public String getYLabel() {
		return yLabel;
	}

	/**
	 * set the label of y-axis
	 * 
	 * @param yLabel
	 *            the label of y-axis
	 */
	public void setYLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	/**
	 * Get series that the chart contains.
	 * 
	 * @return series
	 */
	public List<Chart2DSeries> getLines() {
		return series;
	}

	/**
	 * Set series that the chart contains.
	 * 
	 * @param series
	 *            series
	 */
	public void setLines(List<Chart2DSeries> series) {
		this.series = series;
	}

	/**
	 * get x-axis mode
	 * 
	 * @return x-axis mode
	 */
	public AxisMode getXaxisMode() {
		return xaxisMode;
	}

	/**
	 * set x-axis mode
	 * 
	 * @param xaxisMode
	 *            x-axis mode
	 */
	public void setXaxisMode(AxisMode xaxisMode) {
		this.xaxisMode = xaxisMode;
	}

	/**
	 * format the 2-dimensional chart to string
	 * 
	 * @return a string
	 * @throws Exception
	 */
	@Override
	public String format() throws Exception {
		return formatter.format(this);
	}
	
	/**
	 * Get the formatter that the chart uses.
	 * 
	 * @return a formatter
	 */
	public Chart2DFormatter getFormatter() {
		return formatter;
	}

	/**
	 * Set the formatter that the chart uses.
	 * 
	 * @param formatter
	 *            a formatter
	 */
	public void setFormatter(Chart2DFormatter formatter) {
		this.formatter = formatter;
	}

	public Ticks getXTicks() {
		return xticks;
	}

	public void setXTicks(Ticks xticks) {
		this.xticks = xticks;
	}

	public Map<String, Integer> getBarChartStringIDMap() {
		return barChartStringIDMap;
	}

	public void setBarChartStringIDMap(Map<String, Integer> barChartStringIDMap) {
		this.barChartStringIDMap = barChartStringIDMap;
	}

}
