package perfcharts.configtemplate;

import perfcharts.chart.Chart;
import perfcharts.config.ChartConfig;
import perfcharts.config.PerformanceComparisonTableConfig;

public class PerformanceComparisonTableTemplate extends ChartTemplateBase {

	@Override
	public ChartConfig<? extends Chart> generateChartConfig() {
		PerformanceComparisonTableConfig config = new PerformanceComparisonTableConfig();
		config.setTitle("Performance Comparison");
		config.setSubtitle(getSubtitle());
		return config;
	}

}
