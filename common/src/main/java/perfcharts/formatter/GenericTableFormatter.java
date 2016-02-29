package perfcharts.formatter;

import org.json.JSONObject;
import perfcharts.chart.GenericTable;

import java.io.IOException;

public interface GenericTableFormatter extends
		ChartFormatter<GenericTable> {
	public JSONObject format(GenericTable chart) throws IOException, InterruptedException;
}
