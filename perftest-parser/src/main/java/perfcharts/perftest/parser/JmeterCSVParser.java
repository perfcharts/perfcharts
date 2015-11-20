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

    @Override
    public void parse(InputStream in, OutputStream out) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        Date startTime = Settings.getInstance().getStartTime();
        Date endTime = Settings.getInstance().getEndTime();
        long startTimeVal = startTime == null ? -1 : startTime.getTime();
        long endTimeVal = endTime == null ? -1 : endTime.getTime();
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        try {
            for (CSVRecord rec : parser) {
                long timestamp = Long.parseLong(rec.get(0));
                if (timestamp == 0) {
                    LOGGER.warning("Skip invalid data row: " + rec.toString());
                    continue;
                }
                int rt = Integer.parseInt(rec.get(1));
                if (startTimeVal > 0 && timestamp + rt < startTimeVal
                        || endTimeVal > 0 && timestamp + rt > endTimeVal)
                    continue;
                String label = rec.get(2);
                int skip = 0;
                if (rec.get(4).startsWith("\""))
                    ++skip;
                if (rec.get(skip + 5).startsWith("\""))
                    ++skip;
                int latency = Integer.parseInt(rec.get(skip + 9));
                int threads = 0;
                int bytes = Integer.parseInt(rec.get(skip + 8));
                boolean error = !Boolean.parseBoolean(rec.get(skip + 7));
                csvPrinter.printRecord("TX-" + label
                        + (error ? "-F" : "-S"), timestamp, threads, error ? '1'
                        : '0', latency, rt, bytes);

            }
        } catch (Exception ex) {
            System.err.println("WARNING: invalid raw data skipped: " + ex.toString());
        } finally {
            writer.flush();
        }

    }

}
