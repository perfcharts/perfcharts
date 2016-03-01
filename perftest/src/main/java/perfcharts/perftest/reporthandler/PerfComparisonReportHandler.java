package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.CommandLine;
import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.ChartConfigTemplate;
import perfcharts.configtemplate.PerformanceComparisonChartTemplateOrderedByDifference;
import perfcharts.configtemplate.PerformanceSimpleComparisonTableTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vfreex on 2/26/16.
 */
public class PerfComparisonReportHandler extends PerfReportHandler {
    private final static String reportTemplateHTML = "res/report_templates/perf-baseline/html/report-mono.template.html";
    private static Map<String, PerfReportHandlerRule> reportHandlerRules = new HashMap<>();

    static {
        options.addOption(null, "subtitle", true, "Optional subtitle applied to all charts");
        reportHandlerRules.put("perfcmp", new PerfReportHandlerRule("([^/\\.]+).perfcmp", "$1", "perfcmp", PerfReportHandlerRule.DuplicatedAction.APPEND));
    }

    public PerfComparisonReportHandler() {

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
        if ("perfcmp".equals(category)) {
            String subtitle = cmd.getOptionValue("subtitle", "");
            ReportConfig config = new ReportConfig();
            config.setTitle(reportTitle);
            List<ChartConfigTemplate> configTemplates = new ArrayList<>();
            configTemplates.add(new PerformanceSimpleComparisonTableTemplate().setSubtitle(subtitle));
            configTemplates.add(new PerformanceComparisonChartTemplateOrderedByDifference().setSubtitle(subtitle));
            config.setConfigTemplates(configTemplates);
            return config;
        }
        return null;
    }
}
