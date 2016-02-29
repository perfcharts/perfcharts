package perfcharts.formatter;

import org.json.JSONObject;
import perfcharts.chart.Chart2D;

import java.io.IOException;

/**
 * Format a {@link Chart2D} to string.
 * 
 * @author Rayson Zhu
 *
 */
public interface Chart2DFormatter extends ChartFormatter<Chart2D> {
	public JSONObject format(Chart2D chart) throws IOException, InterruptedException;
}
