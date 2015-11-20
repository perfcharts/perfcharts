package perfcharts.perftest.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import perfcharts.perftest.parser.zabbix.ZabbixHistory;
import perfcharts.perftest.parser.zabbix.ZabbixItem;

public class CustomZabbixParser implements DataParser {
	private final static Logger LOGGER = Logger
			.getLogger(CustomZabbixParser.class.getName());

	@Override
	public void parse(InputStream in, OutputStream out) throws Exception {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				in));
		final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
		LOGGER.info("Reading...");
		Map<Integer, ZabbixItem> itemID2Item = new HashMap<Integer, ZabbixItem>();
		Map<Integer, List<ZabbixHistory>> itemID2History = new HashMap<Integer, List<ZabbixHistory>>();
		for (CSVRecord csvRecord : csvParser) {
			String rowLabel = csvRecord.get(0);
			if ("HISTORY".equals(rowLabel)) {
				int itemID = Integer.parseInt(csvRecord.get(1));
				Date timestop = new Date(Long.parseLong(csvRecord.get(2)));
				int valueType = Integer.parseInt(csvRecord.get(3));
				String value = csvRecord.get(4);
				ZabbixHistory history = new ZabbixHistory(itemID, timestop,
						valueType, value);
				List<ZabbixHistory> historyList = itemID2History.get(itemID);
				if (historyList == null)
					itemID2History.put(itemID,
							historyList = new LinkedList<ZabbixHistory>());
				historyList.add(history);
			} else if ("ITEM".equals(rowLabel)) {
				int itemID = Integer.parseInt(csvRecord.get(1));
				String itemKey = csvRecord.get(2);
				int itemValueType = Integer.parseInt(csvRecord.get(3));
				String itemName = csvRecord.get(4);
				int hostID = Integer.parseInt(csvRecord.get(5));
				ZabbixItem item = new ZabbixItem(itemID, itemKey,
						itemValueType, itemName, hostID);
				itemID2Item.put(itemID, item);
			}
		}
		csvParser.close();

		final BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(out));
		final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
		LOGGER.info("Parsing...");
		for (int itemID : itemID2Item.keySet()) {
			ZabbixItem item = itemID2Item.get(itemID);
			LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
			writeHistory("ZABBIX-" + item.getKey(),
					itemID2History.get(item.getItemID()), csvPrinter);
		}
		csvPrinter.flush();
		csvPrinter.close();
	}

	private static void writeHistory(String label,
			List<ZabbixHistory> historyList, CSVPrinter csvPrinter)
			throws IOException {
		if (historyList == null)
			return;
		for (ZabbixHistory zabbixHistory : historyList) {
			csvPrinter.printRecord(label,
					zabbixHistory.getTimestop().getTime(),
					zabbixHistory.getValue());
		}
	}

}
