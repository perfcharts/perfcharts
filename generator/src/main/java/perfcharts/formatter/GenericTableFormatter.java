package perfcharts.formatter;

import perfcharts.chart.GenericTable;

public interface GenericTableFormatter extends
		ChartFormatter<GenericTable> {
	public String format(GenericTable chart) throws Exception;
}
