package perfcharts.formatter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import perfcharts.chart.Chart2D;
import perfcharts.chart.Chart2DSeries;
import perfcharts.chart.Point2D;
import perfcharts.config.AxisMode;

/**
 * Provides the support for formatting a {@link Chart2D} to JSON string that
 * specially optimized for JQuery Flot plotting library.
 * 
 * @see {@linkplain http://www.flotcharts.org/}
 * @author Rayson Zhu
 *
 */
public class FlotChartFormatter implements Chart2DFormatter {
	public String format(Chart2D graph) throws IOException, InterruptedException {
		JSONObject chartJson = new JSONObject();
		chartJson.put("key", graph.getKey());
		chartJson.put("title", graph.getTitle());
		chartJson.put("subtitle", graph.getSubtitle());
		chartJson.put("xLabel", graph.getXLabel());
		chartJson.put("yLabel", graph.getYLabel());
		chartJson.put("xaxisMode", graph.getXaxisMode().toString());
		if (graph.getXTicks() != null)
			chartJson.put("xaxisTicks", new JSONArray(new JSONTokener(graph
					.getXTicks().format())));
		List<Chart2DSeries> series = graph.getLines();
		if (series != null) {
			Map<String, Integer> xAxisString2IntegerMap = graph
					.getBarChartStringIDMap();
			// Map<Integer, String> xAxisInteger2StringMap = null;
			// if (graph.getXaxisMode() == AxisMode.BAR_STRING) {
			// xAxisString2IntegerMap = new HashMap<String, Integer>();
			// //xAxisInteger2StringMap = new HashMap<Integer, String>();
			// }
			JSONArray seriesArrJson = new JSONArray();
			for (Chart2DSeries s : series) {
				JSONObject seriesJson = new JSONObject();
				seriesJson.put("label", s.getLabel());
				if (s.getUnit() != null) {
					JSONObject unitJson = new JSONObject();
					unitJson.put("show", s.isShowUnit());
					unitJson.put("value", s.getUnit());
					seriesJson.put("_unit", unitJson);
				}
				if (s.isShowBars())
					seriesJson.put("bars", new JSONObject().put("show", true));
				if (!s.isShowLine())
					seriesJson
							.put("lines", new JSONObject().put("show", false));
				JSONArray dataJson = new JSONArray();
				for (Point2D stop : s.getStops()) {
					Object x = stop.getX();
					if (Double.isNaN(stop.getY())
							|| Double.isInfinite(stop.getY())) {
						System.err.println(x + "," + stop.getY());
						continue;
					}
					if (graph.getXaxisMode() == AxisMode.BAR_STRING
							&& xAxisString2IntegerMap != null) {
						Integer _x = xAxisString2IntegerMap.get(x.toString());
						if (_x != null) {
							x = _x;
						} else {
							// x = -1;
							continue;
						}
					}
					dataJson.put(new JSONArray().put(x).put(stop.getY()));
				}
				seriesJson.put("data", dataJson);
				seriesArrJson.put(seriesJson);
			}
			chartJson.put("series", seriesArrJson);
			if (xAxisString2IntegerMap != null) {
				JSONObject stringMapJson = new JSONObject();
				for (String key : xAxisString2IntegerMap.keySet()) {
					Integer val = xAxisString2IntegerMap.get(key);
					stringMapJson.put(val.toString(), key);
				}
				chartJson.put("stringMap", stringMapJson);
				chartJson.put("stringMapSize", stringMapJson.length());
			}
		}
		return chartJson.toString();
	}
}
