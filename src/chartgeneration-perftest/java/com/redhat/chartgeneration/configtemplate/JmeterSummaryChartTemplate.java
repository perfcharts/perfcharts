package com.redhat.chartgeneration.configtemplate;

import com.redhat.chartgeneration.config.JmeterSummaryChartConfig;

public class JmeterSummaryChartTemplate extends BaseChartTemplate {

	public JmeterSummaryChartConfig generateChartConfig() {
		JmeterSummaryChartConfig config = new JmeterSummaryChartConfig();
		config.setTitle("Summary Chart");
		return config;
	}

	public JmeterSummaryChartTemplate() {
	}
}