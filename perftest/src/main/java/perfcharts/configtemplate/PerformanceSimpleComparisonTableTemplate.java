package perfcharts.configtemplate;

import perfcharts.chart.Chart;
import perfcharts.config.ChartConfig;
import perfcharts.config.PerformanceSimpleComparisonTableConfig;

/**
 * Created by vfreex on 1/5/16.
 */
public class PerformanceSimpleComparisonTableTemplate extends ChartTemplateBase {
    @Override
    public ChartConfig<? extends Chart> generateChartConfig() {
        PerformanceSimpleComparisonTableConfig config = new PerformanceSimpleComparisonTableConfig();
        config.setTitle("Performance Comparison");
        config.setSubtitle(getSubtitle());
        return config;
    }

}