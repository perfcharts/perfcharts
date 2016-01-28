package perfcharts.formatter;

import perfcharts.chart.GenericTable;

import java.io.IOException;

public interface GenericTableFormatter extends
		ChartFormatter<GenericTable> {
	public String format(GenericTable chart) throws IOException, InterruptedException;
}
