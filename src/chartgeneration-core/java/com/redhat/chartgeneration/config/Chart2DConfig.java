package com.redhat.chartgeneration.config;

import java.util.List;

import com.redhat.chartgeneration.chart.Chart2D;
import com.redhat.chartgeneration.generator.Chart2DFactory;
import com.redhat.chartgeneration.generator.Chart2DFactoryImpl;

/**
 * The configuration for a general two dimensional chart
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2DConfig extends BaseChartConfig<Chart2D> {
	/**
	 * the x-axis label
	 */
	private String xLabel;
	/**
	 * the y-axis label
	 */
	private String yLabel;
	/**
	 * rules for generating elements of the chart
	 */
	private List<Chart2DSeriesConfigRule> rules;
	/**
	 * the x-axis mode
	 */
	private AxisMode xaxisMode = AxisMode.NUMBER;

	public Chart2DConfig() {

	}

	/**
	 * 
	 * @param title
	 *            title of the chart
	 * @param subtitle
	 *            subtitle of the chart
	 * @param xLabel
	 *            x-axis label of the chart
	 * @param yLabel
	 *            y-axis label of the chart
	 * @param rules
	 *            for generating elements of the chart
	 */
	public Chart2DConfig(String title, String subtitle, String xLabel,
			String yLabel, List<Chart2DSeriesConfigRule> rules) {
		super(title, subtitle);
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.rules = rules;
	}

	/**
	 * 
	 * @param title
	 *            title of the chart
	 * @param subtitle
	 *            subtitle of the chart
	 * @param xLabel
	 *            x-axis label of the chart
	 * @param yLabel
	 *            y-axis label of the chart
	 * @param rules
	 *            for generating elements of the chart
	 * @param xaxisMode
	 *            the x-axis mode
	 */
	public Chart2DConfig(String title, String subtitle, String xLabel,
			String yLabel, List<Chart2DSeriesConfigRule> rules, AxisMode xaxisMode) {
		super(title, subtitle);
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.rules = rules;
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
	 * get rules for generating elements of the chart
	 * 
	 * @return rules
	 */
	public List<Chart2DSeriesConfigRule> getRules() {
		return rules;
	}

	/**
	 * set rules for generating elements of the chart
	 * 
	 * @param rules
	 *            rules
	 */
	public void setRules(List<Chart2DSeriesConfigRule> rules) {
		this.rules = rules;
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

	@Override
	public Chart2DFactory createChartFactory() throws Exception {
		return new Chart2DFactoryImpl();
	}

}
