package perfcharts.generator;

import perfcharts.chart.Chart;
import perfcharts.chart.Report;
import perfcharts.config.ChartConfig;
import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.ChartConfigTemplate;
import perfcharts.model.DataTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This object generates a report that contains many charts by a collection of
 * {@link ChartConfig}s.
 *
 * @author Rayson Zhu
 */
public class ReportGenerator {
    /**
     * associated report config
     */
    private ReportConfig reportConfig;

    /**
     * constructor
     *
     * @param reportConfig report config
     */
    public ReportGenerator(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    /**
     * Generates a report that contains many charts from specified input stream.
     *
     * @param in a input stream
     * @return a report
     * @throws Exception
     */
    public Report generate(InputStream in) throws IOException, InterruptedException {
        // load data table
        final DataTableLoader loader = new DataTableLoader();
        final DataTable dataTable = loader.load(in);
        in.close();

        // create chartconfigs
        final List<ChartConfig<Chart>> chartConfigs = new ArrayList<ChartConfig<Chart>>();

        for (ChartConfigTemplate template : reportConfig.getConfigTemplates()) {
            @SuppressWarnings("unchecked")
            ChartConfig<Chart> chartConfig = (ChartConfig<Chart>) template.generateChartConfig();
            chartConfigs.add(chartConfig);
        }

        final int workingThreadsCount = Math.min(chartConfigs.size(), (int) (Runtime
                .getRuntime().availableProcessors() * 1.6));
        final Thread[] workingThreads = new Thread[workingThreadsCount];
        final AtomicInteger remainedTasks = new AtomicInteger(chartConfigs.size());
        final Chart[] charts = new Chart[chartConfigs.size()];
        for (int t = 0; t < workingThreads.length; t++) {
            workingThreads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int taskId;
                    while ((taskId = remainedTasks.decrementAndGet()) >= 0) {
                        ChartConfig<Chart> cfg = chartConfigs.get(taskId);
                        try {
                            ChartFactory<Chart> factory = cfg.createChartFactory();
                            ChartGenerator generator = factory.createGenerator(cfg);
                            charts[taskId] = generator.generate(dataTable);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                }
            });
        }
        for (int t = 0; t < workingThreads.length; t++) {
            workingThreads[t].start();
        }
        for (int t = 0; t < workingThreads.length; t++) {
            workingThreads[t].join();
        }
        return new Report(reportConfig.getTitle(), Arrays.asList(charts));
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

}
