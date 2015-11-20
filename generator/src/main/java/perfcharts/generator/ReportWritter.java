package perfcharts.generator;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import perfcharts.chart.Chart;
import perfcharts.chart.Report;

/**
 * Converts a report to JSON string
 * 
 * @author Rayson Zhu
 *
 */
public class ReportWritter {
	/**
 * 
 */
	public ReportWritter() {
	}

	/**
	 * format a report to JSON string
	 * 
	 * @param report
	 *            a report
	 * @param out
	 *            the {@link OutputStream} for writing
	 * @throws Exception
	 */
	public void write(Report report, OutputStream out) throws Exception {
		StringBuilder sb = new StringBuilder("{");
		int validChartCount = 0;
		sb.append("\"charts\":[");
		for (Chart chart : report.getCharts()) {
			String s = chart.format();
			if (s != null) {
				sb.append(s);
				sb.append(",");
				++validChartCount;
			}
		}
		if (validChartCount > 0)
			sb.deleteCharAt(sb.length() - 1);
		sb.append("]}");

		OutputStreamWriter writer = new OutputStreamWriter(out);
		writer.write(sb.toString());
		writer.flush();
	}

}
