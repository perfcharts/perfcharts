package perfcharts.configtemplate;

import perfcharts.config.JmeterSummaryChartConfig;

public class JmeterSummaryChartTemplate extends ChartTemplateBase {

	public JmeterSummaryChartConfig generateChartConfig() {
		JmeterSummaryChartConfig config = new JmeterSummaryChartConfig();
		config.setTitle("Summary");
		config.setKey("perf-summary-table");
		return config;
	}

	public JmeterSummaryChartTemplate() {
	}
}
