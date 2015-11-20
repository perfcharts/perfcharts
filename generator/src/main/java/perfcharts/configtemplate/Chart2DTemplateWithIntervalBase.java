package perfcharts.configtemplate;

import java.util.List;

import perfcharts.config.AxisMode;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfigRule;

/**
 * Represents a {@link Chart2DTemplateWithIntervalBase} with interval merge
 * support
 * 
 * @author Rayson Zhu
 *
 */
public abstract class Chart2DTemplateWithIntervalBase extends
		Chart2DTemplateBase {
	/**
	 * the interval value. 0 means do not merge.
	 */
	private int interval = 0;

	/**
 * 
 */
	public Chart2DTemplateWithIntervalBase() {

	}

	/**
	 * 
	 * @param interval
	 *            the interval value. 0 means do not merge.
	 */
	public Chart2DTemplateWithIntervalBase(int interval) {
		this.interval = interval;
	}

	/**
	 * Get the interval value.
	 * 
	 * @return the interval value. 0 means do not merge.
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * Set the interval value.
	 * 
	 * @param interval
	 *            the interval value. 0 means do not merge.
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	protected Chart2DConfig createConfig(String defaultTitle,
										 String defaultXLabel, String defaultYLabel,
										 List<Chart2DSeriesConfigRule> rules, AxisMode xaxisMode) {
		Chart2DConfig config = super.createConfig(defaultTitle, defaultXLabel, defaultYLabel, rules,
				xaxisMode);
		config.setInterval(interval);
		return config;
	}
}
