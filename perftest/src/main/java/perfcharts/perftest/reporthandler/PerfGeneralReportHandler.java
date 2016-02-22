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
    private final static String reportTemplateHTML = "res/report_templates/perf/report-mono.template.html";

    public PerfGeneralReportHandler() {
    }

    @Override
    protected ReportConfig createPerformanceReportConfig(String optionalExclusionPattern) {
        ReportConfig config = new ReportConfig();
        config.setTitle("Performance");
        //config.setBasePath();
        List<ChartConfigTemplate> configTemplates = new ArrayList<>();
        configTemplates.add(new JmeterSummaryChartTemplate());
        configTemplates.add(new JmeterThreadChartTemplate());
        configTemplates.add(new TopTxWithHighestAvgRTBarChartTemplate());
        configTemplates.add(new TopTxWithMostHitsBarChartTemplate());
        configTemplates.add(new JmeterAverageRTChartTemplate().setExclusionPattern(optionalExclusionPattern));
        configTemplates.add(new JmeterRTChartTemplate());
        configTemplates.add(new JmeterTotalTPSChartTemplate().setExclusionPattern(optionalExclusionPattern));
        configTemplates.add(new JmeterTPSChartTemplate());
        configTemplates.add(new JmeterHitsChartTemplate());
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
