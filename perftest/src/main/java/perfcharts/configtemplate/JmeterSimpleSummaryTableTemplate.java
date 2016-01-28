package perfcharts.configtemplate;

import perfcharts.config.JmeterSimpleSummaryChartConfig;

/**
 * Created by vfreex on 12/22/15.
 */
public class JmeterSimpleSummaryTableTemplate extends ChartTemplateBase {
    public JmeterSimpleSummaryTableTemplate() {
    }
    public JmeterSimpleSummaryChartConfig generateChartConfig() {
        JmeterSimpleSummaryChartConfig config = new JmeterSimpleSummaryChartConfig();
        config.setTitle("Performance Summary");
        config.setKey("perf-simple-summary");
        return config;
    }
}
