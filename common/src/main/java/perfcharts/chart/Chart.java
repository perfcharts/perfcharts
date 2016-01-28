package perfcharts.chart;

import java.io.IOException;

/**
 * Represents a generated chart
 * 
 * @author Rayson Zhu
 *
 */
public abstract class Chart {
	/**
	 * the title of configured chart
	 */
	private String title;
	/**
	 * the subtitle of configured chart
	 */
	private String subtitle;
	
	/**
	 * An identifier
	 */
	private String key;

	/**
	 * Initialize an empty chart
	 */
	public Chart() {

	}

	/**
	 * Initialize a chart with specified title and subtitle
	 * 
	 * @param title
	 *            a title
	 * @param subtitle
	 *            a subtitle
	 */
	public Chart(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}

	/**
	 * format the chart to string
	 * 
	 * @return a string
	 * @throws Exception
	 */
	public abstract String format() throws IOException, InterruptedException;

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
