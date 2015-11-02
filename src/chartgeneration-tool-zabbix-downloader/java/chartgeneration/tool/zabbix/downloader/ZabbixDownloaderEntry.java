package chartgeneration.tool.zabbix.downloader;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import chartgeneration.tool.zabbix.downloader.ZabbixDownloader.ZabbixAPIException;

public class ZabbixDownloaderEntry {
	private final static Logger LOGGER = Logger
			.getLogger(ZabbixDownloaderEntry.class.getName());

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"y-M-d h:m:s", Locale.ENGLISH);

	public static void main(String[] args) throws IOException, ParseException,
			ZabbixAPIException {
		if (args.length < 1) {
			System.err.println("Usage:<OUTPUT_DIR>");
			Runtime.getRuntime().exit(1);
			return;
		}
		String outputDir = args[0];

		// load configuration
		final Properties prop = System.getProperties();
		final String apiUrl = prop.getProperty("API_URL");
		if (apiUrl == null || apiUrl.isEmpty())
			throw new InvalidParameterException("API_URL is required.");
		// final String ca = prop.getProperty("CA");
		final String user = prop.getProperty("user");
		if (user == null || user.isEmpty())
			throw new InvalidParameterException("user is required.");

		final String hostStr = prop.getProperty("hosts");
		if (hostStr == null || hostStr.isEmpty()) {
			throw new InvalidParameterException("hosts is required.");
		}
		String[] hosts = hostStr.split(";");

		TimeZone timeZone;
		final String timeZoneStr = prop.getProperty("time_zone");
		if (timeZoneStr == null || timeZoneStr.isEmpty())
			timeZone = TimeZone.getTimeZone(timeZoneStr);
		else
			timeZone = TimeZone.getTimeZone("UTC");
		TimeZone.setDefault(timeZone);
		sdf.setTimeZone(timeZone);
		LOGGER.info("The fallback time zone is " + timeZone.getDisplayName());

		final String startTimeStr = prop.getProperty("start_time");
		Date startTime = null;
		if (startTimeStr != null && !startTimeStr.isEmpty()) {
			startTime = sdf.parse(startTimeStr);
			LOGGER.info("Use start time " + startTime.toString());
		}

		final String endTimeStr = prop.getProperty("end_time");
		Date endTime = null;
		if (endTimeStr != null && !endTimeStr.isEmpty()) {
			endTime = sdf.parse(endTimeStr);
			LOGGER.info("Use end time " + endTime.toString());
		}
		
		final String itemKeysStr = prop.getProperty("item_keys");
		String[] itemKeys = null;
		if (itemKeysStr != null && !itemKeysStr.isEmpty()) {
			LOGGER.info("Use item keys " + itemKeysStr);
			itemKeys = itemKeysStr.split(";");
		}

		// make sure output directory exists
		new File(outputDir).mkdirs();


		System.out.println("Zabbix password for user '" + user + "' on '"
				+ apiUrl + "':");
		String password;
		Console console = System.console();
		if (console != null)
			password = new String(System.console().readPassword());
		else {
			Scanner scanner = new Scanner(System.in);
			password = scanner.nextLine();
			scanner.close();
		}
		if (password == null || password.isEmpty())
			throw new InvalidParameterException("password is required.");

		System.out.println("Downloading data from Zabbix server to '" + outputDir + "'...");
		ZabbixDownloader downloader = new ZabbixDownloader(apiUrl, user,
				password, hosts, startTime, endTime, outputDir);
		downloader.setItemKeys(itemKeys);
		downloader.download();
	}
}
