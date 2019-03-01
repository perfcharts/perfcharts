package perfcharts.launcher;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import perfcharts.handler.ReportTypeHandler;
import perfcharts.perftest.reporthandler.PerfBaselineReportHandler;
import perfcharts.perftest.reporthandler.PerfComparisonReportHandler;
import perfcharts.perftest.reporthandler.PerfGeneralReportHandler;
import perfcharts.perftest.reporthandler.PerfTrendReportHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vfreex on 1/19/16.
 */
public class NewGeneratorLauncher {
    private final static Logger LOGGER = Logger.getLogger(NewGeneratorLauncher.class.toString());
    private final static Map<String, ReportTypeHandler> reportTypeHandlers = new HashMap<>();

    static {
        reportTypeHandlers.put("perf-baseline", new PerfBaselineReportHandler());
        reportTypeHandlers.put("perf-general", new PerfGeneralReportHandler());
        reportTypeHandlers.put("perf-compare", new PerfComparisonReportHandler());
        reportTypeHandlers.put("perf-trend", new PerfTrendReportHandler());
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            new HelpFormatter().printHelp("perfcharts gen <report-type> [<options>] <input-dir>",
                    "A tool to generate performance testing and resource monitoring reports",
                    new Options(),
                    "report-type: perf-baseline | perf-general | perf-compare | perf-trend\n\n" +
                    "Please report issues at https://github.com/vfreex/perfcharts/issues",
                    false);
            return;
        }
        String action = args[0];
        if (!"gen".equals(action)) {
            System.err.println("[ERROR] unknown action.");
            return;
        }
        String reportType = args[1];
        ReportTypeHandler handler = reportTypeHandlers.getOrDefault(reportType, null);
        if (handler == null) {
            LOGGER.severe("report type '" + reportType + "' not found");
        }
        List<String> argList = new LinkedList<>();
        for (String arg : args) {
            argList.add(arg);
        }
        argList.remove(0);
        argList.remove(0);
        handler.handle(argList);
    }
}
