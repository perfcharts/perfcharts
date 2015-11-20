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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import perfcharts.perftest.parser.zabbix.ZabbixHistory;
import perfcharts.perftest.parser.zabbix.ZabbixItem;

public class ZabbixParser implements DataParser {
	private final static Logger LOGGER = Logger.getLogger(ZabbixParser.class
			.getName());

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
			String itemKey = item.getKey();
			if (itemKey.startsWith("system.cpu.num")) {
				parseCPUNumber(item, itemID2History, csvPrinter);
			} else if (itemKey.startsWith("system.cpu.load")) {
				parseCPULoad(item, itemID2History, csvPrinter);
			} else if (itemKey.startsWith("system.cpu.util")) {
				parseCPUUtilization(item, itemID2History, csvPrinter);
			} else if (itemKey.startsWith("vm.memory.size")) {
				parseMemoryUtilization(item, itemID2History, csvPrinter);
			} else if (itemKey.startsWith("net.if.in")) {
				parseNetworkIO(item, itemID2History, csvPrinter, true);
			} else if (itemKey.startsWith("net.if.out")) {
				parseNetworkIO(item, itemID2History, csvPrinter, false);
			} else if (itemKey.startsWith("vfs.dev.read")) {
				parseDiskIO(item, itemID2History, csvPrinter, true);
			} else if (itemKey.startsWith("vfs.dev.write")) {
				parseDiskIO(item, itemID2History, csvPrinter, false);
			} else if (itemKey.startsWith("system.swap.in")) {
				parseSwapInOut(item, itemID2History, csvPrinter, true);
			} else if (itemKey.startsWith("system.swap.out")) {
				parseSwapInOut(item, itemID2History, csvPrinter, false);
			}

		}
		csvPrinter.flush();
		csvPrinter.close();
	}

	private static void parseCPUNumber(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter) throws IOException {
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		writeHistory("CPUNUM", itemID2History.get(item.getItemID()), csvPrinter);
	}

	private final static Pattern optionsPattern = Pattern.compile("\\[(.*)\\]");

	private static void parseCPULoad(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter) throws IOException {
		Matcher m = optionsPattern.matcher(item.getKey());
		String label = "CPULOAD_1MIN";
		if (m.find()) {
			String option = m.group();
			// if (option.contains("percpu")) {
			// LOGGER.warning("Zabbix item key \""
			// + item.getKey()
			// +
			// "\" is ignored bacause its arguments are incompatible with this program.");
			// return;
			// }
			if (option.contains("avg5")) {
				label = "CPULOAD_5MIN";
			} else if (option.contains("avg15")) {
				label = "CPULOAD_15MIN";
			}
		}
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		writeHistory(label, itemID2History.get(item.getItemID()), csvPrinter);
	}

	private static void parseCPUUtilization(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter) throws IOException {
		List<ZabbixHistory> historyList = itemID2History.get(item.getItemID());
		Matcher m = optionsPattern.matcher(item.getKey());
		String label = "CPUUTIL_USER";
		if (m.find()) {
			String option = m.group();
			if (option.contains("user")) {
				// label = "CPUUTIL_USER";
			} else if (option.contains("system")) {
				label = "CPUUTIL_SYS";
			} else if (option.contains("iowait")) {
				label = "CPUUTIL_WAIT";
			} else if (option.contains("idle")) {
				label = "CPUUTIL_USED";
				if (historyList != null) {
					for (ZabbixHistory zabbixHistory : historyList) {
						double idle = Double.parseDouble(zabbixHistory
								.getValue());
						if (Double.isNaN(idle))
							continue;
						csvPrinter.printRecord(label, zabbixHistory
								.getTimestop().getTime(), 100.0 - idle);
					}
				}
				return;
			} else {
				LOGGER.info("Zabbix item key \""
						+ item.getKey()
						+ "\" is ignored bacause its arguments are incompatible with this program.");
				return;
			}
		}
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		writeHistory(label, historyList, csvPrinter);
	}

	private static void parseMemoryUtilization(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter) throws IOException {
		Matcher m = optionsPattern.matcher(item.getKey());
		String label = "MEM_TOTAL";
		if (m.find()) {
			String option = m.group();
			if (option.contains("total")) {
				// label = "MEM_TOTAL";
			} else if (option.contains("free")) {
				label = "MEM_FREE";
			} else if (option.contains("cached")) {
				label = "MEM_CACHED";
			} else if (option.contains("buffers")) {
				label = "MEM_BUFFERS";
			} else if (option.contains("available")) {
				label = "MEM_AVAILABLE";
			} else {
				LOGGER.info("Zabbix item key \""
						+ item.getKey()
						+ "\" is ignored bacause its arguments are incompatible with this program.");
				return;
			}
		}
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		List<ZabbixHistory> historyList = itemID2History.get(item.getItemID());
		if (historyList == null)
			return;
		for (ZabbixHistory zabbixHistory : historyList) {
			csvPrinter.printRecord(label,
					zabbixHistory.getTimestop().getTime(),
					Double.parseDouble(zabbixHistory.getValue()) / 1.0e6);
		}
	}

	private static void parseNetworkIO(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter, boolean isIn) throws IOException {
		Matcher m = optionsPattern.matcher(item.getKey());
		String label = isIn ? "NET_IF_IN-" : "NET_IF_OUT-";
		if (!m.find()) {
			LOGGER.info("Zabbix item key \"" + item.getKey()
					+ "\" is ignored bacause its arguments are invalid.");
			return;
		}
		String options = m.group(1);
		if (options.isEmpty()) {
			LOGGER.info("Zabbix item key \""
					+ item.getKey()
					+ "\" is ignored bacause its arguments are incompatible with this program.");
			return;
		}
		String[] optionsArr = options.split(";");
		String nif = optionsArr[0];
		if (nif.isEmpty()) {
			LOGGER.info("Zabbix item key \""
					+ item.getKey()
					+ "\" is ignored bacause its arguments are incompatible with this program.");
			return;
		}
		String mode = null;
		if (optionsArr.length > 1)
			mode = optionsArr[1];
		if (mode != null && !mode.isEmpty() && !"bytes".equals(mode)) {
			LOGGER.info("Zabbix item key \""
					+ item.getKey()
					+ "\" is ignored bacause its arguments are incompatible with this program.");
			return;
		}
		label += nif;
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		List<ZabbixHistory> historyList = itemID2History.get(item.getItemID());
		if (historyList == null)
			return;
		for (ZabbixHistory zabbixHistory : historyList) {
			csvPrinter.printRecord(label,
					zabbixHistory.getTimestop().getTime(),
					Double.parseDouble(zabbixHistory.getValue()) / 1024.0);
		}
	}

	private static void parseDiskIO(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter, boolean isIn) throws IOException {
		// [device,<type>,<mode>]
		List<ZabbixHistory> historyList = itemID2History.get(item.getItemID());
		Matcher m = optionsPattern.matcher(item.getKey());
		String label = isIn ? "DISKREAD-" : "DISKWRITE-";
		if (m.find()) {
			String[] options = m.group(1).split(";");
			String device = options[0].trim();
			if (device == null || device.isEmpty()){
				LOGGER.info("Zabbix item key \"" + item.getKey()
						+ "\" is ignored bacause its arguments are invalid.");
				return;
			}
			if (options.length > 1) {
			}
			label += device;
		} else {
			LOGGER.info("Zabbix item key \"" + item.getKey()
					+ "\" is ignored bacause its arguments are invalid.");
			return;
		}
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		if (historyList == null)
			return;
		for (ZabbixHistory zabbixHistory : historyList) {
			csvPrinter.printRecord(label,
					zabbixHistory.getTimestop().getTime(),
					Double.parseDouble(zabbixHistory.getValue()) / 2.0);
		}
	}

	private static void parseSwapInOut(ZabbixItem item,
			Map<Integer, List<ZabbixHistory>> itemID2History,
			CSVPrinter csvPrinter, boolean isIn) throws IOException {
		// system.swap.in|out[<device>,<type>]
		Matcher m = optionsPattern.matcher(item.getKey());
		String label = isIn ? "SWAP_IN" : "SWAP_OUT";
		if (!m.find()) {
			LOGGER.info("Zabbix item key \"" + item.getKey()
					+ "\" is ignored bacause its arguments are invalid.");
			return;
		}
		String options = m.group(1);
		if (options.isEmpty()) {
			LOGGER.info("Zabbix item key \""
					+ item.getKey()
					+ "\" is ignored bacause its arguments are incompatible with this program.");
			return;
		}
		String[] optionsArr = options.split(";");
		// String device = optionsArr[0];
		// if (device.isEmpty()) {
		// LOGGER.info("Zabbix item key \""
		// + item.getKey()
		// +
		// "\" is ignored bacause its arguments are incompatible with this program.");
		// return;
		// }
		String type = null;
		if (optionsArr.length > 1)
			type = optionsArr[1];
		if (type != null && !type.isEmpty() && !"pages".equals(type)) {
			LOGGER.info("Zabbix item key \""
					+ item.getKey()
					+ "\" is ignored bacause its arguments are incompatible with this program.");
			return;
		}
		// label += device;
		LOGGER.info("Parsing Zabbix item key \"" + item.getKey() + "...");
		List<ZabbixHistory> historyList = itemID2History.get(item.getItemID());
		if (historyList == null)
			return;
		for (ZabbixHistory zabbixHistory : historyList) {
			csvPrinter.printRecord(label,
					zabbixHistory.getTimestop().getTime(),
					Double.parseDouble(zabbixHistory.getValue()) / 1024.0);
		}
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



