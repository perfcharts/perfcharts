package perfcharts.perftest.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Date;
import java.util.logging.Logger;

/**
 * The parser converts Jmeter test logs to data tables (in CSV format). The raw
 * data must be XML format.
 *
 * @author Rayson Zhu
 */
public class JmeterXMLParser implements DataParser {
    private static Logger LOGGER = Logger.getLogger(JmeterCSVParser.class
            .getName());

    public void parse(InputStream in, OutputStream out)
            throws IOException {
        XMLStreamReader reader = null;
        try {
            reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        Date startTime = Settings.getInstance().getStartTime();
        Date endTime = Settings.getInstance().getEndTime();
        long startTimeVal = startTime == null ? -1 : startTime.getTime();
        long endTimeVal = endTime == null ? -1 : endTime.getTime();
        int level = 0;

        String label = null;
        long timestamp = 0;
        int rt = 0;
        int latency = 0;
        int threads = 0;
        long bytes = 0;
        boolean error = false;
        boolean isEmptyTag = true;
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamReader.START_ELEMENT:
                        ++level;
                        if (level == 2) {
                            boolean isSample = "sample".equals(reader.getLocalName());
                            boolean isHttpSample = !isSample
                                    && "httpSample".equals(reader.getLocalName());
                            if (isSample || isHttpSample) {
                                isEmptyTag = true;
                                label = reader.getAttributeValue(null, "lb");
                                timestamp = Long.parseLong(reader.getAttributeValue(
                                        null, "ts"));
                                if (timestamp == 0) {
                                    LOGGER.warning("Skip invalid data element, because its timestamp is 0.");
                                    continue;
                                }
                                rt = Integer.parseInt(reader.getAttributeValue(null,
                                        "t"));
                                latency = Integer.parseInt(reader.getAttributeValue(
                                        null, "lt"));
                                String na = reader.getAttributeValue(null, "na");
                                threads = Integer.parseInt(na != null ? na : reader
                                        .getAttributeValue(null, "ng"));
                                bytes = Long.parseLong(reader.getAttributeValue(null,
                                        "by"));
                                error = !Boolean.parseBoolean(reader.getAttributeValue(
                                        null, "s"));
                                if ((startTimeVal <= 0 || timestamp + rt >= startTimeVal)
                                        && (endTimeVal <= 0 || timestamp + rt <= endTimeVal)) {
                                    csvPrinter.printRecord("TX-" + label
                                                    + (error ? "-F" : "-S"), timestamp,
                                            threads, error ? '1' : '0', latency, rt,
                                            bytes);
                                }
                            }
                        } else if (level >= 3) {
                            boolean isSample = "sample".equals(reader.getLocalName());
                            boolean isHttpSample = !isSample
                                    && "httpSample".equals(reader.getLocalName());
                            if (isHttpSample) {
                                isEmptyTag = false;
                                // label = reader.getAttributeValue(null, "lb");
                                long timestamp_ = Long.parseLong(reader
                                        .getAttributeValue(null, "ts"));
                                if (startTimeVal > 0 && timestamp_ < startTimeVal
                                        || endTimeVal > 0 && timestamp_ > endTimeVal)
                                    continue;
                                long rt_ = Integer.parseInt(reader.getAttributeValue(
                                        null, "t"));
                                csvPrinter.printRecord("HIT", timestamp_, rt_);
                            }
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        if (level == 2 && isEmptyTag) {
                            // System.err.println("!!!EMPTY");
                            boolean isSample = "sample".equals(reader.getLocalName());
                            boolean isHttpSample = !isSample
                                    && "httpSample".equals(reader.getLocalName());
                            if (isHttpSample
                                    && (startTimeVal <= 0 || timestamp + rt >= startTimeVal)
                                    && (endTimeVal <= 0 || timestamp + rt <= endTimeVal)) {
                                csvPrinter.printRecord("HIT", timestamp, rt);
                            }
                        }
                        --level;
                        break;
                }
            }
        } catch (Exception ex) {
            System.err.println("WARNING: invalid raw data skipped: " + ex.toString());
        } finally {
            csvPrinter.flush();
        }
    }
}
