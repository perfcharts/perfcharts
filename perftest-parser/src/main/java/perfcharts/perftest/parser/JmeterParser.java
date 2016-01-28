package perfcharts.perftest.parser;

import java.io.*;

/**
 * The parser converts Jmeter test logs to data tables (in CSV format). The raw
 * data is can be XML or CSV format.
 * 
 * @author Rayson Zhu
 *
 */
public class JmeterParser implements DataParser {

	@Override
	public void parse(InputStream in, OutputStream out) throws IOException {
		final int BUFFER_SIZE = 512;
		in.mark(BUFFER_SIZE);
		byte[] buffer = new byte[BUFFER_SIZE];
		in.read(buffer, 0, BUFFER_SIZE);
		String firstLine = new String(buffer, "ascii");
		in.reset();
		if (firstLine.startsWith("<?xml")) {
			new JmeterXMLParser().parse(in, out);
		} else {
			new JmeterCSVParser().parse(in, out);
		}
	}

}
