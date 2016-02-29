package perfcharts.generator;

import org.json.JSONArray;
import org.json.JSONObject;
import perfcharts.chart.Chart;
import perfcharts.chart.Report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Converts a report to JSON string
 *
 * @author Rayson Zhu
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
     * @param report a report
     * @param out    the {@link OutputStream} for writing
     * @throws Exception
     */
    public void write(Report report, OutputStream out) throws IOException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", report.getTitle());
        jsonObject.put("subtitle", report.getSubtitle());
        jsonObject.put("groupName", report.getGroupName());
        JSONArray charts = new JSONArray();
        jsonObject.put("charts", charts);
        for (Chart chart : report.getCharts()) {
            charts.put(chart.format());
        }

        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(jsonObject.toString());
        writer.flush();
    }

}
