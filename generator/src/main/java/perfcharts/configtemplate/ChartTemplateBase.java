package perfcharts.configtemplate;

import perfcharts.common.FieldSelector;

/**
 * Defines the default template of charts.
 * 
 * @author Rayson Zhu
 *
 */
public abstract class ChartTemplateBase implements ChartConfigTemplate {
	/**
	 * the label field of data row
	 */
	private FieldSelector labelField;
	/**
	 * the title of configured chart
	 */
	private String title;
	/**
	 * the subtitle of configured chart
	 */
	private String subtitle;
	
	private String key;

	/**
	 * Get the label field of data row
	 * 
	 * @return a label field
	 */
	public FieldSelector getLabelField() {
		return labelField;
	}

	/**
	 * set the label field of data row
	 * 
	 * @param labelField
	 *            a label field
	 */
	public void setLabelField(FieldSelector labelField) {
		this.labelField = labelField;
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
