package perfcharts.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import perfcharts.calc.Chart2DCalculation;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.generator.Chart2DSeriesConfigBuilder;

/**
 * A {@link Chart2DSeriesConfigRule} defines a rule to produce
 * {@link Chart2DSeriesConfig}s. Instances of this class are usually created by
 * {@link perfcharts.configtemplate.ChartConfigTemplate}, and used by {@link Chart2DSeriesConfigBuilder}
 * to create {@link Chart2DSeriesConfig}s.
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2DSeriesConfigRule {
	/**
	 * The regular expression to filter data rows by row label.
	 * 
	 */
	private String labelPattern;
	/**
	 * The format of generated series label. The matched part of row label
	 * captured by {@link #labelPattern} will be used to replace the $1, $2,
	 * $3,... marks in {@link #seriesLabelFormat}.
	 * 
	 */
	@Deprecated
	private String seriesLabelFormat;
	/**
	 * the {@link FieldSelector} for extracting row labels of raw data
	 */
	private FieldSelector labelField;

	private FieldSelector<String> seriesLabelField;

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

	private Chart2DSeriesExclusionRule exclusionRule;

	/**
	 * constructor
	 */
	public Chart2DSeriesConfigRule() {
	}

	/**
	 * constructor
	 * 
	 * @param labelPattern
	 *            The regular expression to filter data rows by row label.
	 * @param seriesLabelFormat
	 *            The format of generated series label. The matched part of row
	 *            label captured by {@link #labelPattern} will be used to
	 *            replace the $1, $2, $3,... marks in {@link #seriesLabelFormat}
	 *            .
	 * 
	 * @param unit
	 *            The unit of y-value. It is mainly used to share axes for
	 *            composite charts.
	 * @param labelField
	 *            the {@link FieldSelector} for extracting row labels of raw
	 *            data
	 * @param xField
	 *            the {@link FieldSelector} for extracting x-value of raw data
	 * @param yField
	 *            the {@link FieldSelector} for extracting y-value of raw data
	 * @param calculation
	 *            specify the calculation to be used for plotted chart
	 */
    @Deprecated
	public Chart2DSeriesConfigRule(String labelPattern,
			String seriesLabelFormat, String unit, FieldSelector labelField,
			FieldSelector xField, FieldSelector yField,
			Chart2DCalculation calculation) {
		this(labelPattern, seriesLabelFormat, unit, labelField, xField, yField,
				calculation, true, false, false);
	}

	/**
	 * constructor
	 * 
	 * @param labelPattern
	 *            The regular expression to filter data rows by row label.
	 * @param seriesLabelFormat
	 *            The format of generated series label. The matched part of row
	 *            label captured by {@link #labelPattern} will be used to
	 *            replace the $1, $2, $3,... marks in {@link #seriesLabelFormat}
	 *            .
	 * 
	 * @param unit
	 *            The unit of y-value. It is mainly used to share axes for
	 *            composite charts.
	 * @param labelField
	 *            the {@link FieldSelector} for extracting row labels of raw
	 *            data
	 * @param xField
	 *            the {@link FieldSelector} for extracting x-value of raw data
	 * @param yField
	 *            the {@link FieldSelector} for extracting y-value of raw data
	 * @param calculation
	 *            specify the calculation to be used for plotted chart
	 * @param showLines
	 *            show line?
	 * @param showBars
	 *            show bar?
	 * @param showUnit
	 *            show unit?
	 */
    @Deprecated
	public Chart2DSeriesConfigRule(String labelPattern,
			String seriesLabelFormat, String unit, FieldSelector labelField,
			FieldSelector xField, FieldSelector yField,
			Chart2DCalculation calculation, boolean showLines,
			boolean showBars, boolean showUnit) {
		this.labelPattern = labelPattern;
		this.seriesLabelFormat = seriesLabelFormat;
		this.labelField = labelField;
		this.xField = xField;
		this.yField = yField;
		this.calculation = calculation;
		this.showBar = showBars;
		this.showLine = showLines;
		this.unit = unit;
		this.showUnit = showUnit;
	}

    /**
     * constructor
     *
     * @param labelPattern
     *            The regular expression to filter data rows by row label.
     * @param seriesLabelField
     *            The format of generated series label. The matched part of row
     *            label captured by {@link #labelPattern} will be used to
     *            replace the $1, $2, $3,... marks in {@link #seriesLabelFormat}
     *            .
     *
     * @param unit
     *            The unit of y-value. It is mainly used to share axes for
     *            composite charts.
     * @param labelField
     *            the {@link FieldSelector} for extracting row labels of raw
     *            data
     * @param xField
     *            the {@link FieldSelector} for extracting x-value of raw data
     * @param yField
     *            the {@link FieldSelector} for extracting y-value of raw data
     * @param calculation
     *            specify the calculation to be used for plotted chart
     */
    public Chart2DSeriesConfigRule(String labelPattern,
                                   FieldSelector<String> seriesLabelField, String unit, FieldSelector labelField,
                                   FieldSelector xField, FieldSelector yField,
                                   Chart2DCalculation calculation) {
        this(labelPattern, seriesLabelField, unit, labelField, xField, yField,
                calculation, true, false, false);
    }

    /**
     * constructor
     *
     * @param labelPattern
     *            The regular expression to filter data rows by row label.
     * @param seriesLabelField
     *            The format of generated series label. The matched part of row
     *            label captured by {@link #labelPattern} will be used to
     *            replace the $1, $2, $3,... marks in {@link #seriesLabelFormat}
     *            .
     *
     * @param unit
     *            The unit of y-value. It is mainly used to share axes for
     *            composite charts.
     * @param labelField
     *            the {@link FieldSelector} for extracting row labels of raw
     *            data
     * @param xField
     *            the {@link FieldSelector} for extracting x-value of raw data
     * @param yField
     *            the {@link FieldSelector} for extracting y-value of raw data
     * @param calculation
     *            specify the calculation to be used for plotted chart
     * @param showLines
     *            show line?
     * @param showBars
     *            show bar?
     * @param showUnit
     *            show unit?
     */
    public Chart2DSeriesConfigRule(String labelPattern,
                                   FieldSelector<String> seriesLabelField, String unit, FieldSelector labelField,
                                   FieldSelector xField, FieldSelector yField,
                                   Chart2DCalculation calculation, boolean showLines,
                                   boolean showBars, boolean showUnit) {
        this.labelPattern = labelPattern;
        this.seriesLabelField = seriesLabelField;
        this.labelField = labelField;
        this.xField = xField;
        this.yField = yField;
        this.calculation = calculation;
        this.showBar = showBars;
        this.showLine = showLines;
        this.unit = unit;
        this.showUnit = showUnit;
    }

	/**
	 * Get the regular expression to filter data rows by row label.
	 * 
	 * @return a regular expression
	 */
	public String getLabelPattern() {
		return labelPattern;
	}

	/**
	 * Set the regular expression to filter data rows by row label.
	 * 
	 * @param labelPattern
	 *            a regular expression
	 */
	public void setLabelPattern(String labelPattern) {
		this.labelPattern = labelPattern;
	}

	/**
	 * Get the format of generated series label. The matched part of row label
	 * captured by {@link #labelPattern} will be used to replace the $1, $2,
	 * $3,... marks in {@link #seriesLabelFormat} .
	 * 
	 * @return the format of generated series label
	 */
	public String getSeriesLabelFormat() {
		return seriesLabelFormat;
	}

	/**
	 * Set the format of generated series label. The matched part of row label
	 * captured by {@link #labelPattern} will be used to replace the $1, $2,
	 * $3,... marks in {@link #seriesLabelFormat} .
	 * 
	 * @param seriesLabelFormat
	 *            the format of generated series label
	 */
	public void setSeriesLabelFormat(String seriesLabelFormat) {
		this.seriesLabelFormat = seriesLabelFormat;
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

	public Chart2DSeriesExclusionRule getExclusionRule() {
		return exclusionRule;
	}

	public void setExclusionRule(Chart2DSeriesExclusionRule exclusionRule) {
		this.exclusionRule = exclusionRule;
	}

	/**
	 * generated series label
	 *
	 */
	public FieldSelector<String> getSeriesLabelField() {
		return seriesLabelField;
	}

	public void setSeriesLabelField(FieldSelector<String> seriesLabelField) {
		this.seriesLabelField = seriesLabelField;
	}
}
