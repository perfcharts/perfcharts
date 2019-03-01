package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.CommandLine;
import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vfreex on 2/26/16.
 */
public class PerfTrendReportHandler extends PerfReportHandler {
    private final static String reportTemplateHTML = "res/report-templates/perf/html/report-mono.template.html";
    private static Map<String, PerfReportHandlerRule> reportHandlerRules = new HashMap<>();

    static {
        options.addOption(null, "subtitle", true, "Optional subtitle applied to all charts");
        reportHandlerRules.put("perftrend", new PerfReportHandlerRule("([^/\\.]+).perftrend", "$1", "perftrend", PerfReportHandlerRule.DuplicatedAction.APPEND));
    }

    public PerfTrendReportHandler() {

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
        if ("perftrend".equals(category)) {
            String subtitle = cmd.getOptionValue("subtitle", "");
            ReportConfig config = new ReportConfig();
            config.setTitle(reportTitle);
            List<ChartConfigTemplate> configTemplates = new ArrayList<>();
            configTemplates.add(new JmeterAverageRTTrendChartTemplate().setSubtitle(subtitle));
            configTemplates.add(new JmeterRTTrendChartTemplate().setSubtitle(subtitle));
            config.setConfigTemplates(configTemplates);
            return config;
        }
        return null;
    }
}
