package perfcharts.perftest.reporthandler;

import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vfreex on 1/19/16.
 */
public class PerfGeneralReportHandler extends PerfReportHandler {
    private final static Logger LOGGER = Logger.getLogger(PerfGeneralReportHandler.class.getName());
    private final static String reportTemplateHTML = "res/report_templates/perf-baseline/html/report-mono.template.html";

    public PerfGeneralReportHandler() {
    }

    @Override
    protected ReportConfig createPerformanceReportConfig() {
        ReportConfig config = new ReportConfig();
        config.setTitle("Performance");
        //config.setBasePath();
        List<ChartConfigTemplate> configTemplates = new ArrayList<>();
        configTemplates.add(new JmeterSimpleSummaryTableTemplate());
        configTemplates.add(new TopTxWithHighestAvgRTBarChartTemplate());
        configTemplates.add(new JmeterAverageRTChartTemplate());
        configTemplates.add(new JmeterRTChartTemplate());
        config.setConfigTemplates(configTemplates);
        //config.setLabelField(new IndexFieldSelector<>(0));
        return config;
    }

    @Override
    protected ReportConfig createResourceMonitoringReportConfig(String title) {
        ReportConfig config = new ReportConfig();
        config.setTitle(title);
        //config.setBasePath();
        List<ChartConfigTemplate> configTemplates = new ArrayList<>();
        configTemplates.add(new CPULoadChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONCPUUtilizationChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONMemoryUtilizationChartTemplate().setSubtitle(title));
        configTemplates.add(new VMSwapInOutChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONNetworkThroughputChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONDiskIOChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONDiskBusyChartTemplate().setSubtitle(title));
        config.setConfigTemplates(configTemplates);
        //config.setLabelField(new IndexFieldSelector<>(0));
        return config;
    }

    @Override
    protected String getReportTemplateFilePath() {
        return reportTemplateHTML;
    }
}
