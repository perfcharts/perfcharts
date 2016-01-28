package perfcharts.config;

import perfcharts.generator.JmeterSimpleSummaryChartFactoryImpl;
import perfcharts.generator.JmeterSummaryChartFactory;
import perfcharts.generator.JmeterSummaryChartFactoryImpl;

import java.io.IOException;

/**
 * The configuration for Jmeter summary chart
 * 
 * @author Rayson Zhu
 *
 */
public class JmeterSimpleSummaryChartConfig extends
		JmeterSummaryChartConfig {
	@Override
	public JmeterSummaryChartFactory createChartFactory() throws IOException, InterruptedException {
		return new JmeterSimpleSummaryChartFactoryImpl();
	}

}
