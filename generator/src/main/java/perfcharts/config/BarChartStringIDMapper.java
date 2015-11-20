package perfcharts.config;

import java.util.Map;

import perfcharts.chart.Chart2D;

public interface BarChartStringIDMapper {
	Map<String, Integer> map(Chart2D chart, Chart2DConfig config);
}
