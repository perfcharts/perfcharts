package perfcharts.config;

import perfcharts.generator.GenericTableFactory;
import perfcharts.generator.PerformanceSimpleComparisonTableFactoryImpl;

/**
 * Created by vfreex on 1/5/16.
 */
public class PerformanceSimpleComparisonTableConfig extends GenericTableConfigBase {
    @Override
    public GenericTableFactory createChartFactory()
            throws Exception {
        return new PerformanceSimpleComparisonTableFactoryImpl();
    }
}
