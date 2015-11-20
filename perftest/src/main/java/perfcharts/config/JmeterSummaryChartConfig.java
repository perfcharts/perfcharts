package perfcharts.config;

import perfcharts.generator.JmeterSummaryChartFactory;
import perfcharts.generator.JmeterSummaryChartFactoryImpl;

/**
 * The configuration for Jmeter summary chart
 * 
 * @author Rayson Zhu
 *
 */
public class JmeterSummaryChartConfig extends
		GenericTableConfigBase {
	@Override
	public JmeterSummaryChartFactory createChartFactory() throws Exception {
		return new JmeterSummaryChartFactoryImpl();
	}

}
