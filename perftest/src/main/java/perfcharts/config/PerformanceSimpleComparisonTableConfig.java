package perfcharts.config;

import perfcharts.generator.GenericTableFactory;
import perfcharts.generator.PerformanceSimpleComparisonTableFactoryImpl;

import java.io.IOException;

/**
 * Created by vfreex on 1/5/16.
 */
public class PerformanceSimpleComparisonTableConfig extends GenericTableConfigBase {
    @Override
    public GenericTableFactory createChartFactory()
            throws IOException, InterruptedException {
        return new PerformanceSimpleComparisonTableFactoryImpl();
    }
}
