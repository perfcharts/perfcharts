package chartgeneration.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The parser converts NMON monitoring logs to data tables (in CSV format).
 *
 * @author Rayson Zhu
 */
public class NMONParser implements DataParser {
    public void parse(InputStream in, OutputStream out) throws IOException,
            ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        Map<Integer, Long> timeTable = new HashMap<Integer, Long>(16000);
        //Map<String, String> metaInfo = new HashMap<String, String>(30);
        // TimeZone utcZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:m:s d-MMM-y", Locale.ENGLISH);
        // timeFormat.setTimeZone(utcZone);

        final Map<String, Integer> diskColumnMap = new HashMap<String, Integer>(4);

        Date startTime = Settings.getInstance().getStartTime();
        Date endTime = Settings.getInstance().getEndTime();
        List<CSVRecord> records = new ArrayList<CSVRecord>();
        for (Iterator<CSVRecord> it = csvParser.iterator(); ; ) {
            try {
                if (!it.hasNext())
                    break;
                records.add(it.next());
            } catch (Exception ex) {
                System.err.println("WARNING: invalid data row skipped (" + ex.toString() + ") at line " + csvParser.getCurrentLineNumber());
            }
        }
        //List<CSVRecord> records = csvParser.getRecords();
        for (CSVRecord rec : records) {
            if (rec.size() == 0)
                continue;
            if (rec.get(0).equals("ZZZZ") && rec.size() >= 4) {
                int tsLabelValue = Integer.parseInt(rec.get(1)
                        .substring(1));
                Date date = timeFormat.parse(rec.get(2) + " "
                        + rec.get(3));
                if (startTime != null && date.before(startTime)
                        || endTime != null && date.after(endTime))
                    continue;
                timeTable.put(tsLabelValue, date.getTime());
            } else if (rec.get(0).startsWith("DISK")
                    && rec.size() > 2
                    && !rec.get(1).startsWith("T")) {
                for (int i = 2; i < rec.size(); ++i) {
                    //if (diskPattern.matcher(extractedLine[i]).matches()) {
                    // diskColumnMap.putIfAbsent(extractedLine[i], i);
                    diskColumnMap.put(rec.get(i), i);
                    //}
                }
            }/* else if (extractedLine[0].equals("AAA")
                    && extractedLine.length >= 3) {
				metaInfo.put(extractedLine[1], extractedLine[2]);
			}*/
        }
        in.close();

        Map<String, NMONItemParser> keyParserMap = new HashMap<>();

        keyParserMap.put("CPU_ALL", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                writer.printRecord("CPU",
                        ts.toString(),
                        Float.toString(Float.parseFloat(rec.get(2))),
                        Float.toString(Float.parseFloat(rec.get(3))),
                        Float.toString(Float.parseFloat(rec.get(4))),
                        String.format("%.1f", 100.0f - Float.parseFloat(rec.get(5))),
                        Integer.toString(Integer.parseInt(rec.get(7)))
                );
            }
        });

        keyParserMap.put("MEM", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                writer.printRecord("MEM",
                        ts.toString(),
                        Float.toString(Float.parseFloat(rec.get(2))),
                        Float.toString(Float.parseFloat(rec.get(6))),
                        Float.toString(Float.parseFloat(rec.get(11))),
                        Float.toString(Float.parseFloat(rec.get(14)))
                );
            }
        });

        keyParserMap.put("VM", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                writer.printRecord("VM",
                        ts.toString(),
                        Float.toString(Float.parseFloat(rec.get(10))),
                        Float.toString(Float.parseFloat(rec.get(11)))
                );
            }
        });

        keyParserMap.put("NET", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                int numberOfNetworkAdapters = (rec.size() - 2) >> 1;
                float read = 0;
                float write = 0;
                for (int i = 0; i < numberOfNetworkAdapters; ++i) {
                    read += Float.parseFloat(rec.get(2 + i));
                    write += Float.parseFloat(rec.get(2 + numberOfNetworkAdapters + i));
                }
                writer.printRecord("NET",
                        ts.toString(),
                        Float.toString(read),
                        Float.toString(write)
                );
            }
        });

        keyParserMap.put("DISKBUSY", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                final String tsStr = ts.toString();
                for (String disk : diskColumnMap.keySet()) {
                    Integer index = diskColumnMap.get(disk);
                    if (index == null)
                        continue;
                    writer.printRecord("DISKBUSY-" + disk,
                            tsStr,
                            Float.toString(Float.parseFloat(rec.get(index)))
                    );
                }
            }
        });

        keyParserMap.put("DISKREAD", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                final String tsStr = ts.toString();
                for (String disk : diskColumnMap.keySet()) {
                    Integer index = diskColumnMap.get(disk);
                    if (index == null)
                        continue;
                    writer.printRecord("DISKREAD-" + disk,
                            tsStr,
                            Float.toString(Float.parseFloat(rec.get(index)))
                    );
                }
            }
        });

        keyParserMap.put("DISKWRITE", new NMONItemParser() {
            @Override
            public void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException {
                Long ts = timeTable.get(Integer.parseInt(rec.get(1).substring(1)));
                if (ts == null)
                    return;
                final String tsStr = ts.toString();
                for (String disk : diskColumnMap.keySet()) {
                    Integer index = diskColumnMap.get(disk);
                    if (index == null)
                        continue;
                    writer.printRecord("DISKWRITE-" + disk,
                            tsStr,
                            Float.toString(Float.parseFloat(rec.get(index)))
                    );
                }
            }
        });

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        for (CSVRecord rec : records) {
            if (rec.size() < 2 || !rec.get(1).startsWith("T"))
                continue;
            NMONItemParser itemParser = keyParserMap.get(rec.get(0));
            if (itemParser != null)
                itemParser.parse(csvPrinter, rec, timeTable);
        }
        csvPrinter.flush();
    }

    private interface NMONItemParser {
        void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException,
                ParseException;
    }
}
