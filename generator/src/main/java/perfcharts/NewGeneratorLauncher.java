package perfcharts;

import perfcharts.handler.ReportTypeHandler;
import perfcharts.perftest.reporthandler.PerfBaselineReportHandler;
import perfcharts.perftest.reporthandler.PerfGeneralReportHandler;

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
        reportTypeHandlers.put("perf", new PerfGeneralReportHandler());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.err.println("Usage: report_type [args...]");
            return;
        }
        String reportType = args[0];
        ReportTypeHandler handler = reportTypeHandlers.getOrDefault(reportType, null);
        if (handler == null) {
            LOGGER.severe("report type '" + reportType + "' not found");
        }
        List<String> argList = new LinkedList<>();
        for (String arg : args) {
            argList.add(arg);
        }
        argList.remove(0);
        handler.handle(argList);
    }
}
