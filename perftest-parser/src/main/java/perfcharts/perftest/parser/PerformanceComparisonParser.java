package perfcharts.perftest.parser;

import perfcharts.common.Utilities;
import perfcharts.perftest.parser.perfsummary.PerfSummaryData;
import perfcharts.perftest.parser.perfsummary.PerfSummaryItem;
import perfcharts.perftest.parser.perfsummary.PerfSummarySupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.logging.Logger;

public class PerformanceComparisonParser implements DataParser {
    private final static Logger LOGGER = Logger
            .getLogger(PerformanceComparisonParser.class.getName());

    @Override
    public void parse(InputStream in, OutputStream out) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        CSVParser csvParser = null;
        CSVPrinter csvPrinter = null;
        try {
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            int sourceBuildID = 0;
            String sourceBuildName = null;
            String sourceBuildPath = null;
            int destBuildID = 0;
            String destBuildName = null;
            String destBuildPath = null;
            for (CSVRecord csvRecord : csvParser) {
                switch (csvRecord.get(0)) {
                    case "SOURCE":
                        sourceBuildID = Integer.parseInt(csvRecord.get(1));
                        sourceBuildName = csvRecord.get(2);
                        sourceBuildPath = csvRecord.get(3);
                        break;
                    case "DEST":
                        destBuildID = Integer.parseInt(csvRecord.get(1));
                        destBuildName = csvRecord.get(2);
                        destBuildPath = csvRecord.get(3);
                        break;
                    default:
                        break;
                }
            }
            if (sourceBuildPath == null || sourceBuildPath.isEmpty()) {
                LOGGER.severe("Source build path is required.");
                Runtime.getRuntime().exit(1);
            }

            File sourceFile = new File(sourceBuildPath);
            JSONObject sourceResult = new JSONObject(new JSONTokener(
                    new FileInputStream(sourceFile)));
            JSONArray sourceCharts = sourceResult.getJSONArray("charts");

            JSONObject sourceSummaryChart = PerfSummarySupport.findPerfSummaryChart2(sourceCharts);
            PerfSummaryData sourceData = null;
            if (sourceSummaryChart != null) {
                sourceData = PerfSummarySupport.parsePerfSummaryTable2(sourceSummaryChart);
            } else {
                sourceSummaryChart = PerfSummarySupport.findPerfSummaryChart1(sourceCharts);
                if (sourceSummaryChart != null)
                    sourceData = PerfSummarySupport.parsePerfSummaryTable1(sourceSummaryChart);
            }
            if (sourceData == null) {
                LOGGER.severe("No valid result found from source build.");
                Runtime.getRuntime().exit(1);
            }
            sourceData.setBuildId(sourceBuildID);
            sourceData.setBuildName(sourceBuildName);

            if (destBuildPath == null || destBuildPath.isEmpty()) {
                LOGGER.severe("Destination build path is required.");
                Runtime.getRuntime().exit(1);
            }

            File destFile = new File(destBuildPath);
            JSONObject destResult = new JSONObject(new JSONTokener(
                    new FileInputStream(destFile)));
            JSONArray destCharts = destResult.getJSONArray("charts");

            JSONObject destSummaryChart = PerfSummarySupport.findPerfSummaryChart2(destCharts);
            PerfSummaryData destData = null;
            if (destSummaryChart != null) {
                destData = PerfSummarySupport.parsePerfSummaryTable2(destSummaryChart);
            } else {
                destSummaryChart = PerfSummarySupport.findPerfSummaryChart1(destCharts);
                if (destSummaryChart != null)
                    destData = PerfSummarySupport.parsePerfSummaryTable1(destSummaryChart);
            }
            if (destData == null) {
                LOGGER.severe("No valid result found from destination build.");
                Runtime.getRuntime().exit(1);
            }
            destData.setBuildId(destBuildID);
            destData.setBuildName(destBuildName);

            printCSVItems(sourceData, destData, csvPrinter);
            csvPrinter.flush();
        } finally {
            if (csvParser != null)
                csvParser.close();
            if (csvPrinter != null)
                csvPrinter.close();
        }

    }

    private void printCSVItems(PerfSummaryData source, PerfSummaryData dest,
                               CSVPrinter csvPrinter) throws IOException {
        for (String transactionName : source.getItems().keySet()) {
            PerfSummaryItem sourceItem = source.getItems().get(transactionName);
            PerfSummaryItem destItem = dest.getItems().get(transactionName);
            if (destItem == null)
                destItem = new PerfSummaryItem();
            csvPrinter.printRecord("TX-" + transactionName,
                    sourceItem.getSamples(),
                    destItem.getSamples(),
                    Utilities.doubleToString(sourceItem.getAverage()),
                    Utilities.doubleToString(destItem.getAverage()),
                    Utilities.doubleToString(sourceItem.get90Line()),
                    Utilities.doubleToString(destItem.get90Line()),
                    Utilities.doubleToString(sourceItem.getError()),
                    Utilities.doubleToString(destItem.getError()),
                    Utilities.doubleToString(sourceItem.getThroughput()),
                    Utilities.doubleToString(destItem.getThroughput()),
                    source.getBuildId(),
                    dest.getBuildId(),
                    source.getBuildName(),
                    dest.getBuildName());
        }
        PerfSummaryItem sourceItem = source.getTotal();
        if (sourceItem != null) {
            PerfSummaryItem destItem = dest.getTotal();
            if (destItem == null)
                destItem = new PerfSummaryItem();
            csvPrinter.printRecord("TOTAL",
                    sourceItem.getSamples(),
                    destItem.getSamples(),
                    Utilities.doubleToString(sourceItem.getAverage()),
                    Utilities.doubleToString(destItem.getAverage()),
                    Utilities.doubleToString(sourceItem.get90Line()),
                    Utilities.doubleToString(destItem.get90Line()),
                    Utilities.doubleToString(sourceItem.getError()),
                    Utilities.doubleToString(destItem.getError()),
                    Utilities.doubleToString(sourceItem.getThroughput()),
                    Utilities.doubleToString(destItem.getThroughput()),
                    source.getBuildId(),
                    dest.getBuildId(),
                    source.getBuildName(),
                    dest.getBuildName());
        }
    }


}

