package perfcharts.configtemplate;

import perfcharts.chart.Chart;
import perfcharts.common.FieldSelector;
import perfcharts.config.ChartConfig;

/**
 * A {@link ChartConfigTemplate} is an factory object to create {@link ChartConfig}.
 * It can be used to define a template of specific chart.
 * 
 * @author Rayson Zhu
 *
 */
public interface ChartConfigTemplate {
	/**
	 * get the label field of data row
	 * 
	 * @return the label field
	 */
	public FieldSelector getLabelField();

	/**
	 * set the label field of data row
	 * 
	 * @param labelField
	 *            the label field
	 */
	public void setLabelField(FieldSelector labelField);

	public String getKey();

	public void setKey(String key);
	
	/**
	 * Create a {@link ChartConfig} object to generate this kind of chart.
	 * 
	 * @return a {@link ChartConfig}
	 */
	public ChartConfig<? extends Chart> generateChartConfig();
}
