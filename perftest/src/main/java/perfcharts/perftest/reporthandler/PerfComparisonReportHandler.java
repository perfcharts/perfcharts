package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.CommandLine;
import perfcharts.config.ReportConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by vfreex on 2/26/16.
 */
public class PerfComparisonReportHandler extends PerfReportHandler {
    private final static String reportTemplateHTML = "res/report_templates/perf/report-mono.template.html";
    private List<PerfReportHandlerRule> reportHandlerRules;

    public PerfComparisonReportHandler() {
        reportHandlerRules.add(new PerfReportHandlerRule("([^/\\.]+).perfcmp", "$1", "perfcmp", PerfReportHandlerRule.DuplicatedAction.APPEND));

    }

    @Override
    protected Map<String, PerfReportHandlerRule> getReportHandlerRules() {
        return null;
    }

    @Override
    protected String getReportTemplateHTMLFileName() {
        return reportTemplateHTML;
    }

    @Override
    protected ReportConfig createReportConfigs(String category, String reportTitle, String reportSubtitle, CommandLine cmd) {
        return null;
    }
}
