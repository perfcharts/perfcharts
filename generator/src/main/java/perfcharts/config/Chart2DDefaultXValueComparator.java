package perfcharts.config;

import java.util.List;

public class Chart2DDefaultXValueComparator implements Chart2DXValueComparator {
	private Chart2DSeriesConfig seriesConfig;

	public Chart2DDefaultXValueComparator() {
	}

	@Override
	public int compare(List<Object> o1, List<Object> o2) {
		@SuppressWarnings("unchecked")
		Comparable<Object> a = (Comparable<Object>) seriesConfig.getXField()
				.select(o1);
		@SuppressWarnings("unchecked")
		Comparable<Object> b = (Comparable<Object>) seriesConfig.getXField()
				.select(o2);
		return a.compareTo(b);
	}

	public Chart2DSeriesConfig getSeriesConfig() {
		return seriesConfig;
	}

	public void setSeriesConfig(Chart2DSeriesConfig seriesConfig) {
		this.seriesConfig = seriesConfig;
	}

}
