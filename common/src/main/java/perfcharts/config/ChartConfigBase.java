package perfcharts.config;

import perfcharts.chart.Chart;

/**
 * BaseChartConfig is the base class for chart configuration.
 * 
 * @author Rayson Zhu
 *
 * @param <T>
 *            the type of chart
 */
public abstract class ChartConfigBase<T extends Chart> implements
		ChartConfig<T> {
	/**
	 * the title of configured chart
	 */
	private String title;
	/**
	 * the subtitle of configured chart
	 */
	private String subtitle;

	private String key;

	public ChartConfigBase() {

	}

	public ChartConfigBase(String title, String subtitle) {
		this(title, subtitle, null);
	}

	public ChartConfigBase(String title, String subtitle, String key) {
		this.title = title;
		this.subtitle = subtitle;
		this.key = key;
	}

	/**
	 * get the subtitle of configured chart
	 * 
	 * @return a subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * set the subtitle of configured hart
	 * 
	 * @param subtitle
	 *            a subtitle
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * get the title of configured chart
	 * 
	 * @return a title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * set the title of configured chart
	 * 
	 * @param title
	 *            a title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
