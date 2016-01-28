package perfcharts.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a series for 2-dimensional charts
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2DSeries {
	/**
	 * series label
	 */
	private String label;
	/**
	 * data points
	 */
	private List<Point2D> points;
	/**
	 * specify whether the line should be shown for this series
	 */
	private boolean showLine = true;
	/**
	 * specify whether the bars should be shown for this series
	 */
	private boolean showBars = false;
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
	 * Initialize an empty series.
	 */
	public Chart2DSeries() {
		points = new ArrayList<Point2D>();
	}

	/**
	 * Initialize a series with specified parameters.
	 * 
	 * @param label
	 *            series label
	 * @param unit
	 *            unit of y-value
	 * @param points
	 *            data points
	 */
	public Chart2DSeries(String label, String unit, List<Point2D> points) {
		this(label, unit, points, true, false, false);
	}

	/**
	 * Initialize a series with specified parameters.
	 * 
	 * @param label
	 *            series label
	 * @param unit
	 *            unit of y-value
	 * @param points
	 *            data points
	 * @param showLines
	 *            show line?
	 * @param showBars
	 *            show bar?
	 * @param showUnit
	 *            show unit?
	 */
	public Chart2DSeries(String label, String unit, List<Point2D> points,
			boolean showLines, boolean showBars, boolean showUnit) {
		this.label = label;
		this.points = points;
		this.unit = unit;
		this.showBars = showBars;
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
	 * Get data points
	 * 
	 * @return data points
	 */
	public List<Point2D> getStops() {
		return points;
	}

	/**
	 * Set data points
	 * 
	 * @param stops
	 *            data points
	 */
	public void setStops(List<Point2D> stops) {
		this.points = stops;
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
	 * determine whether the bars should be shown for this series
	 * 
	 * @return true if yes, otherwise false
	 */
	public boolean isShowBars() {
		return showBars;
	}

	/**
	 * specify whether the bars should be shown for this series
	 * 
	 * @param showBar
	 *            true if yes, otherwise false
	 */
	public void setShowBars(boolean showBar) {
		this.showBars = showBar;
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

	@Override
	public String toString() {
		return this.getClass().getName() + ":" + label + ", points "
				+ points.size();
	}

}
