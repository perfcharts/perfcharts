package perfcharts.config;

import java.util.Comparator;
import java.util.List;

public interface Chart2DXValueComparator extends Comparator<List<Object>> {
	public int compare(List<Object> o1, List<Object> o2);
	public Chart2DSeriesConfig getSeriesConfig();
	public void setSeriesConfig(Chart2DSeriesConfig seriesConfig);
}
