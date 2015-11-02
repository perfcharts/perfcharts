package chartgeneration.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import chartgeneration.common.AppData;

/**
 * A parser converts CPU load logs to data tables (in CSV format). The raw data
 * is plain text file, a record per line. The format of record is "
 * {@code yyyy-MM-dd hh:mm:ss, 1min 5min 15min}" (without quotes), like "
 * {@code 2014-07-07 17:12:43, 0.73 0.99 1.27}".
 * 
 * @author Rayson Zhu
 *
 */
public class CPULoadParser implements DataParser {
	private SimpleDateFormat timeFormat = new SimpleDateFormat("y-M-d H:m:s", Locale.ENGLISH);

	public void parse(InputStream in, OutputStream out) throws IOException,
			ParseException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		String lineStr;
		Date startTime = Settings.getInstance().getStartTime();
		Date endTime = Settings.getInstance().getEndTime();
		Boolean isTimestamp = null;
		while ((lineStr = reader.readLine()) != null) {
			String[] extractedLine = lineStr.split(",");
			long time = 0;
			if (extractedLine.length < 2)
				continue;
			try {
				if (isTimestamp == null)
					isTimestamp = Pattern.matches("\\d+", extractedLine[0]);

				Date date = isTimestamp ? new Date(
						Long.parseLong(extractedLine[0]) * 1000) : timeFormat
						.parse(extractedLine[0]);
				if (startTime != null && date.before(startTime)
						|| endTime != null && date.after(endTime))
					continue;
				time = date.getTime();
			} catch (Exception ex) {
				AppData.getInstance()
						.getLogger()
						.warning(
								"CPULoadParser.parse(): Ignore non-data line '"
										+ lineStr + "'");
				continue;
			}
			writer.write("CPULOAD,");
			writer.write(Long.toString(time));
			String[] cpuLoadStr = extractedLine[1].trim().split(" ");
			writer.write(String.format(",%.2f", Float.parseFloat(cpuLoadStr[0])));
			writer.write(String.format(",%.2f", Float.parseFloat(cpuLoadStr[1])));
			writer.write(String.format(",%.2f", Float.parseFloat(cpuLoadStr[2])));
			writer.write(",");
			writer.write(extractedLine.length > 2 ? Integer.toString(Integer
					.parseInt(extractedLine[2])) : "0");
			writer.write("\n");
		}
		writer.flush();
	}
}
