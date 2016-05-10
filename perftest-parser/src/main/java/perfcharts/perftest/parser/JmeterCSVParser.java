package perfcharts.perftest.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.Date;
import java.util.logging.Logger;

/**
 * The parser converts Jmeter test logs to data tables (in CSV format). The raw
 * data must be CSV format.
 *
 * @author Rayson Zhu
 */
public class JmeterCSVParser implements DataParser {

    private static Logger LOGGER = Logger.getLogger(JmeterCSVParser.class
            .getName());


    private static Integer getInt(CSVRecord rec, String name) {
        if (!rec.isSet(name))
            return null;
        String s = rec.get(name);
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("NaN") || s.equalsIgnoreCase("null"))
            return null;
        return Integer.parseInt(s);
    }

    private static double getDouble(CSVRecord rec, String name) {
        if (!rec.isSet(name))
            return Double.NaN;
        String s = rec.get(name);
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("NaN") || s.equalsIgnoreCase("null"))
            return Double.NaN;
        return Double.parseDouble(rec.get(name));
    }

    private static String str(Integer n) {
        return n == null ? "NaN" : n.toString();
    }

    private static String str(Double n) {
        return n == null || n.isNaN() ? "NaN" : n.toString();
    }

    private static void printRecord(CSVPrinter csvPrinter, String label, long timestamp, Integer threads, boolean error, double latency, double rt, double bytes) throws IOException {
        csvPrinter.printRecord("TX-" + label
                + (error ? "-F" : "-S"), timestamp, str(threads), error ? '1'
                : '0', str(latency), str(rt), str(bytes));
    }

    @Override
    public void parse(InputStream in, OutputStream out) throws IOException {
        // column names: timeStamp,elapsed,label,responseCode,responseMessage,threadName,dataType,success,bytes,grpThreads,allThreads,Latency
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        Date startTime = Settings.getInstance().getStartTime();
        Date endTime = Settings.getInstance().getEndTime();
        long startTimeVal = startTime == null ? -1 : startTime.getTime();
        long endTimeVal = endTime == null ? -1 : endTime.getTime();
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
        try {
            for (CSVRecord rec : parser) {
                long timestamp = Long.parseLong(rec.get("timeStamp")); // must be set
                if (timestamp == 0) {
                    LOGGER.warning("Skip invalid data row: " + rec.toString());
                    continue;
                }
                double rt = getDouble(rec, "elapsed"); // must be set
                if (startTimeVal > 0 && timestamp + rt < startTimeVal
                        || endTimeVal > 0 && timestamp + rt > endTimeVal)
                    continue;
                String label = rec.get("label");
                if (label == null || label.isEmpty()) // must be set
                    throw new IllegalArgumentException("label has an invalid value: " + label);
                double latency = getDouble(rec, "Latency");
                Integer threads = getInt(rec, "allThreads");
                double bytes = getDouble(rec, "bytes");
                boolean error = !Boolean.parseBoolean(rec.get("success"));
//                csvPrinter.printRecord("TX-" + label
//                        + (error ? "-F" : "-S"), timestamp, threads, error ? '1'
//                        : '0', latency, rt, bytes);
                printRecord(csvPrinter, label, timestamp, threads, error, latency, rt, bytes);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("WARNING: invalid raw data skipped: " + ex.toString());
        } finally {
            writer.flush();
        }
    }
}
