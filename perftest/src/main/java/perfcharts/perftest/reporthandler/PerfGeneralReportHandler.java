package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.CommandLine;
import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vfreex on 1/19/16.
 */
public class PerfGeneralReportHandler extends PerfReportHandler {
    private final static Logger LOGGER = Logger.getLogger(PerfGeneralReportHandler.class.getName());
    private final static String reportTemplateHTML = "res/report_templates/perf-baseline/html/report-mono.template.html";

    private Map<String, PerfReportHandlerRule> reportHandlerRules = new HashMap<>();

    public PerfGeneralReportHandler() {
        reportHandlerRules.put("jtl", new PerfReportHandlerRule("(.+).jtl", "Performance", "jmeter", PerfReportHandlerRule.DuplicatedAction.APPEND));
        reportHandlerRules.put("nmon", new PerfReportHandlerRule("([^_]+)(_.*)?\\.nmon", "$1", "nmon", PerfReportHandlerRule.DuplicatedAction.APPEND));
        reportHandlerRules.put("load", new PerfReportHandlerRule("([^_]+)(_.*)?\\.load", "$1", "nmon", PerfReportHandlerRule.DuplicatedAction.APPEND));
    }


    @Override
    protected Map<String, PerfReportHandlerRule> getReportHandlerRules() {
        return reportHandlerRules;
    }

    @Override
    protected String getReportTemplateHTMLFileName() {
        return reportTemplateHTML;
    }

    @Override
    protected ReportConfig createReportConfigs(String category, String reportTitle, String reportSubtitle, CommandLine cmd) {
        if ("jmeter".equals(category)) {
            String optionalExclusionPattern = cmd.getOptionValue("e", null);
            ReportConfig config = new ReportConfig();
            config.setTitle(reportTitle);
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
            return config;
        }
        if (("nmon").equals(category)) {
            ReportConfig config = new ReportConfig();
            config.setTitle(reportTitle);
            config.setSubtitle(reportSubtitle);
            config.setGroupName("System Resource Monitoring");
            //config.setBasePath();
            List<ChartConfigTemplate> configTemplates = new ArrayList<>();
            configTemplates.add(new CPULoadChartTemplate().setSubtitle(reportTitle));
            configTemplates.add(new NMONCPUUtilizationChartTemplate().setSubtitle(reportTitle));
            configTemplates.add(new NMONMemoryUtilizationChartTemplate().setSubtitle(reportTitle));
            configTemplates.add(new VMSwapInOutChartTemplate().setSubtitle(reportTitle));
            configTemplates.add(new NMONNetworkThroughputChartTemplate().setSubtitle(reportTitle));
            configTemplates.add(new NMONDiskIOChartTemplate().setSubtitle(reportTitle));
            configTemplates.add(new NMONDiskBusyChartTemplate().setSubtitle(reportTitle));
            config.setConfigTemplates(configTemplates);
            //config.setLabelField(new IndexFieldSelector<>(0));
            return config;
        }
        return null;
    }
}
