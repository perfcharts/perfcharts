package perfcharts.configtemplate;

import java.util.List;

import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

/**
 * Defines the default template of 2-dimensional charts.
 * 
 * @author Rayson Zhu
 *
 */
public abstract class Chart2DTemplateBase extends ChartTemplateBase {
	/**
	 * the x-axis label
	 */
	private String xLabel;
	/**
	 * the y-axis label
	 */
	private String yLabel;

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
	 * A help function to create a {@link Chart2DConfig}.
	 * 
	 * @param defaultTitle
	 * @param defaultXLabel
	 * @param defaultYLabel
	 * @param rules
	 * @param xaxisMode
	 * @return
	 */
	protected Chart2DConfig createConfig(String defaultTitle,
			String defaultXLabel, String defaultYLabel,
			List<Chart2DSeriesConfigRule> rules, AxisMode xaxisMode) {
		return createConfig(defaultTitle, null, defaultXLabel, defaultYLabel,
				rules, xaxisMode);
	}

	/**
	 * A help function to create a {@link Chart2DConfig}.
	 * 
	 * @param defaultTitle
	 * @param defaultSubtitle
	 * @param defaultXLabel
	 * @param defaultYLabel
	 * @param rules
	 * @param xaxisMode
	 * @return
	 */
	protected Chart2DConfig createConfig(String defaultTitle,
			String defaultSubtitle, String defaultXLabel, String defaultYLabel,
			List<Chart2DSeriesConfigRule> rules, AxisMode xaxisMode) {
		String title = getTitle();
		String subtitle = getSubtitle();
		String key = getKey();
		Chart2DConfig config = new Chart2DConfig(title == null ? defaultTitle : title,
				subtitle == null ? defaultSubtitle : subtitle,
				xLabel == null ? defaultXLabel : xLabel,
				yLabel == null ? defaultYLabel : yLabel, rules, xaxisMode, 0);
		config.setKey(key);
		return config;
	}

	public abstract Chart2DConfig generateChartConfig();
}
