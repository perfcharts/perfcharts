package perfcharts.perftest.parser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimeZone;

import perfcharts.common.AppData;

/**
 * Contains entry point
 * 
 * @author Rayson Zhu
 *
 */
public class PerftestParserLauncher {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err
					.println("Usage:\n\tjava [-DstartTime=START_TIME] [-DendTime=END_TIME] -jar chartgeneration-perftest-parser.jar PARSER_CLASS [TIME_ZONE]");
			System.err
					.println("\tformat of startTime and endTime: 'yyyy-MM-dd hh:mm:ss', like '2014-12-18 13:45:00'");
			return;
		}
		// Accept the time zone. (Because the logs from NMON and CPU load
		// monitoring tool don't contain any information about time zone)
		TimeZone.setDefault(TimeZone.getTimeZone(args.length > 1
				&& !args[1].isEmpty() ? args[1] : "UTC"));
		AppData.getInstance()
				.getLogger()
				.info("The fallback time zone is "
						+ TimeZone.getDefault().getDisplayName());
		String parserClassName = args[0];
		if (!parserClassName.startsWith("/"))
			parserClassName = PerftestParserLauncher.class.getPackage().getName()
					+ "." + parserClassName;
		else
			parserClassName = parserClassName.substring(1);
		InputStream in = System.in;
		OutputStream out = System.out;
		// load specified parser class and create a instance
		Class<?> parserClass = Class.forName(parserClassName);
		DataParser parser = parserClass.asSubclass(DataParser.class)
				.newInstance();
		// run parser
		parser.parse(in, out);
		in.close();
		out.close();
	}
}
