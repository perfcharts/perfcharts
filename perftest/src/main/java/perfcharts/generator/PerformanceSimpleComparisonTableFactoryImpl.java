package perfcharts.generator;

import perfcharts.chart.GenericTable;
import perfcharts.config.ChartConfig;
import perfcharts.config.PerformanceSimpleComparisonTableConfig;

/**
 * Created by vfreex on 1/5/16.
 */
public class PerformanceSimpleComparisonTableFactoryImpl extends GenericTableFactoryBase {
    @Override
    public PerformanceSimpleComparisonTableGenerator createGenerator(
            ChartConfig<GenericTable> config) throws Exception {
        return new PerformanceSimpleComparisonTableGenerator((PerformanceSimpleComparisonTableConfig) config);
    }
}
