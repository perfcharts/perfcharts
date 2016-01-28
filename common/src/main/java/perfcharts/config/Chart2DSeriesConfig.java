package perfcharts.config;

import java.util.Set;

import perfcharts.calc.Chart2DCalculation;
import perfcharts.common.FieldSelector;

/**
 * Represents the configuration for a chart series.
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2DSeriesConfig {
	/**
	 * the series label
	 */
	private String label;
	/**
	 * the {@link FieldSelector} for extracting row labels of raw data
	 */
	private FieldSelector labelField;
	/**
	 * the {@link FieldSelector} for extracting x-value of raw data
	 */
	private FieldSelector xField;
	/**
	 * the {@link FieldSelector} for extracting y-value of raw data
	 */
	private FieldSelector yField;
	/**
	 * specify the calculation to be used for plotted chart
	 */
	private Chart2DCalculation calculation;
	/**
	 * involvedRowLabels contains labels of data rows that the series involves.
	 */
	private Set<String> involvedRowLabels;
	/**
	 * specify whether the line should be shown for this series
	 */
	private boolean showLine = true;
	/**
	 * specify whether the bar should be shown for this series
	 */
	private boolean showBar = false;
	/**
	 * The unit of y-value. It is mainly used to share axes for composite
	 * charts.
	 */
	private String unit;
	/**
	 * Specify whether the unit should be shown for this series.
	 */
	private boolean showUnit;

	/**
	 * constructor
	 */
	public Chart2DSeriesConfig() {
	}

	/**
	 * constructor
	 * 
	 * @param label
	 *            series label
	 * @param unit
	 *            unit of y-value
	 * @param labelField
	 *            the field of row label
	 * @param xField
	 *            the field of x-value
	 * @param yField
	 *            the field of y-value
	 * @param calculation
	 *            specify the calculation algorithm for plotting
	 * @param involvedRowLabels
	 *            specify the labels of which rows should be involved in this
	 *            series
	 * @param showLines
	 *            show line?
	 * @param showBars
	 *            show bar?
	 * @param showUnit
	 *            show unit?
	 */
	public Chart2DSeriesConfig(String label, String unit,
			FieldSelector labelField, FieldSelector xField,
			FieldSelector yField, Chart2DCalculation calculation,
			Set<String> involvedRowLabels, boolean showLines, boolean showBars,
			boolean showUnit) {
		this.label = label;
		this.unit = unit;
		this.labelField = labelField;
		this.xField = xField;
		this.yField = yField;
		this.calculation = calculation;
		this.involvedRowLabels = involvedRowLabels;
		this.showBar = showBars;
		this.showLine = showLines;
		this.showUnit = showUnit;
	}

	/**
	 * get the series label
	 * 
	 * @return the series label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * set the series label
	 * 
	 * @param label
	 *            the series label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * get the field of x-value
	 * 
	 * @return the field of x-value
	 */
	public FieldSelector getXField() {
		return xField;
	}

	/**
	 * set the field of x-value
	 * 
	 * @param xField
	 *            the field of x-value
	 */
	public void setXField(FieldSelector xField) {
		this.xField = xField;
	}

	/**
	 * get the field of y-value
	 * 
	 * @return the field of y-value
	 */
	public FieldSelector getYField() {
		return yField;
	}

	/**
	 * set the field of y-value
	 * 
	 * @param yField
	 *            the field of y-value
	 */
	public void setYField(FieldSelector yField) {
		this.yField = yField;
	}

	/**
	 * get the calculation algorithm for plotting
	 * 
	 * @return the calculation algorithm
	 */
	public Chart2DCalculation getCalculation() {
		return calculation;
	}

	/**
	 * set the calculation algorithm for plotting
	 * 
	 * @param calculation
	 *            the calculation algorithm
	 */
	public void setCalculation(Chart2DCalculation calculation) {
		this.calculation = calculation;
	}

	/**
	 * get the label field
	 * 
	 * @return the label field
	 */
	public FieldSelector getLabelField() {
		return labelField;
	}

	/**
	 * set the label field
	 * 
	 * @param labelField
	 *            the label field
	 */
	public void setLabelField(FieldSelector labelField) {
		this.labelField = labelField;
	}

	/**
	 * Get the set of involved row labels. The set contains the labels of which
	 * rows should be involved in this series.
	 * 
	 * @return involved row labels
	 */
	public Set<String> getInvolvedRowLabels() {
		return involvedRowLabels;
	}

	/**
	 * Set the set of involved row labels. The set contains the labels of which
	 * rows should be involved in this series.
	 * 
	 * @param involvedRowLabels
	 *            involved row labels
	 */
	public void setInvolvedRowLabels(Set<String> involvedRowLabels) {
		this.involvedRowLabels = involvedRowLabels;
	}

	/**
	 * determine whether the line should be shown for this series
	 * 
	 * @return true if yes, otherwise false
	 */
	public boolean isShowLine() {
		return showLine;
	}

	/**
	 * specify whether the line should be shown for this series
	 * 
	 * @param showLine
	 *            true if yes, otherwise false
	 */
	public void setShowLine(boolean showLine) {
		this.showLine = showLine;
	}

	/**
	 * determine whether the bar should be shown for this series
	 * 
	 * @return true if yes, otherwise false
	 */
	public boolean isShowBar() {
		return showBar;
	}

	/**
	 * specify whether the bar should be shown for this series
	 * 
	 * @param showBar
	 *            true if yes, otherwise false
	 */
	public void setShowBar(boolean showBar) {
		this.showBar = showBar;
	}

	/**
	 * Get the unit of y-data. It is mainly used to share axes for composite
	 * charts.
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Set the unit of y-data. It is mainly used to share axes for composite
	 * charts.
	 * 
	 * @param unit
	 *            the unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * determine whether the unit of y-data should be shown for this series
	 * 
	 * @return true if yes, otherwise false
	 */
	public boolean isShowUnit() {
		return showUnit;
	}

	/**
	 * specify whether the unit of y-data should be shown for this series
	 * 
	 * @param showUnit
	 *            true if yes, otherwise false
	 */
	public void setShowUnit(boolean showUnit) {
		this.showUnit = showUnit;
	}
}
