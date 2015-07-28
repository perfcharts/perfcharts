package chartgeneration.parser;

import chartgeneration.parser.perfsummary.PerfSummaryData;
import chartgeneration.parser.perfsummary.PerfSummaryItem;
import chartgeneration.parser.perfsummary.PerfSummarySupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.Iterator;
import java.util.logging.Logger;

public class PerfTrendParser implements DataParser {
    private final static Logger LOGGER = Logger.getLogger(PerfTrendParser.class
            .getName());

    @Override
    public void parse(InputStream in, OutputStream out) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        Iterator<CSVRecord> it = parser.iterator();
        //List<String>
        for (int xNumber = 0; ; ++xNumber) {
            try {
                if (!it.hasNext())
                    break;
                CSVRecord rec = it.next();
                if (rec.size() < 2) {
                    LOGGER.warning("Ignore line '" + parser.getCurrentLineNumber() + "'.");
                    continue;
                }
                csvPrinter.printRecord("XTICK", xNumber, rec.get(0));
                try {
                    JSONTokener tokener = new JSONTokener(
                            new FileInputStream(rec.get(1)));
                    JSONObject report = new JSONObject(tokener);
                    JSONArray charts = report.getJSONArray("charts");

                    JSONObject chart = PerfSummarySupport
                            .findPerfSummaryChart2(charts);
                    PerfSummaryData data = null;
                    if (chart != null) {
                        data = PerfSummarySupport
                                .parsePerfSummaryTable2(chart);
                    } else {
                        chart = PerfSummarySupport
                                .findPerfSummaryChart1(charts);
                        if (chart != null)
                            data = PerfSummarySupport
                                    .parsePerfSummaryTable1(chart);
                    }
                    if (data == null) {
                        LOGGER.severe("No valid summary chart found from target build.");
                        Runtime.getRuntime().exit(1);
                    }
                    if (data.getItems() != null) {
                        for (String txName : data.getItems().keySet()) {
                            PerfSummaryItem item = data.getItems().get(
                                    txName);
                            double avg = item.getAverage();
                            if (Double.isNaN(avg) || Double.isInfinite(avg)) {
                                LOGGER.warning("Skip invaild Response Time value (NaN) for transaction \""
                                        + txName
                                        + "\" (build: "
                                        + rec.get(0) + ").");
                                continue;
                            }
                            csvPrinter.printRecord("TX-" + txName, xNumber,
                                    avg);
                        }
                    }
                    if (data.getTotal() != null) {
                        double avgTotal = data.getTotal().getAverage();
                        if (Double.isNaN(avgTotal)
                                || Double.isInfinite(avgTotal)) {
                            LOGGER.warning("Skip invaild Response Time value (NaN) for TOTAL (build: "
                                    + rec.get(0) + ").");
                            continue;
                        }
                        csvPrinter.printRecord("TOTAL", xNumber, avgTotal);
                    }
                } catch (JSONException ex) {
                    LOGGER.warning("parser error, skip build \"" + rec.get(0)
                            + "\":\n" + ex.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        csvPrinter.flush();
        csvPrinter.close();
        parser.close();
    }
}



