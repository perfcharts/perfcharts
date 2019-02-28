package perfcharts.perftest.parser;

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
    public void parse(InputStream in, OutputStream out) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        Map<Integer, Long> timeTable = new HashMap<Integer, Long>(16000);
        //Map<String, String> metaInfo = new HashMap<String, String>(30);
        // TimeZone utcZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:m:s d-MMM-y", Locale.ENGLISH);
        // timeFormat.setTimeZone(utcZone);

        final Map<String, Integer> diskColumnMap = new HashMap<String, Integer>(4);
        final Map<String, Integer> cpuColumnMap = new HashMap<>();
        final Map<String, Integer> memColumnMap = new HashMap<>();
        final Map<String, Integer> netColumnMap = new HashMap<>();
        final Map<String, Integer> vmColumnMap = new HashMap<>();


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
                Date date = null;
                try {
                    date = timeFormat.parse(rec.get(2) + " "
                            + rec.get(3));
                } catch (ParseException e) {
                    throw new IOException(e);
                }
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
            else if ("CPU_ALL".equals(rec.get(0)) && !rec.get(1).startsWith("T")) {
                for (int i = 2; i < rec.size(); ++i) {
                    cpuColumnMap.put(rec.get(i), i);
                }
            }
            else if ("MEM".equals(rec.get(0)) && !rec.get(1).startsWith("T")) {
                for (int i = 2; i < rec.size(); ++i) {
                    memColumnMap.put(rec.get(i), i);
                }
            }
            else if ("VM".equals(rec.get(0)) && !rec.get(1).startsWith("T")) {
                for (int i = 2; i < rec.size(); ++i) {
                    vmColumnMap.put(rec.get(i), i);
                }
            }
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
                        Float.toString(Float.parseFloat(rec.get(cpuColumnMap.get("User%")))),
                        Float.toString(Float.parseFloat(rec.get(cpuColumnMap.get("Sys%")))),
                        Float.toString(Float.parseFloat(rec.get(cpuColumnMap.get("Wait%")))),
                        String.format("%.1f", 100.0f - Float.parseFloat(rec.get(cpuColumnMap.get("Idle%")))), // total
                        Integer.toString(Integer.parseInt(rec.get(cpuColumnMap.get("CPUs"))))
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
                        Float.toString(Float.parseFloat(rec.get(memColumnMap.get("memtotal")))), // total
                        Float.toString(Float.parseFloat(rec.get(memColumnMap.get("memfree")))), // free
                        Float.toString(Float.parseFloat(rec.get(memColumnMap.get("cached")))), // cached
                        Float.toString(Float.parseFloat(rec.get(memColumnMap.get("buffers")))) // buffers
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
                        Float.toString(Float.parseFloat(rec.get(vmColumnMap.get("pswpin")))), // swap in
                        Float.toString(Float.parseFloat(rec.get(vmColumnMap.get("pswpout")))) // swap out
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
                try {
                    itemParser.parse(csvPrinter, rec, timeTable);
                } catch (ParseException e) {
                    throw new IOException(e);
                }
        }
        csvPrinter.flush();
    }

    private interface NMONItemParser {
        void parse(CSVPrinter writer, CSVRecord rec, Map<Integer, Long> timeTable) throws IOException,
                ParseException;
    }
}
