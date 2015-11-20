package perfcharts.perftest.parser.perfsummary;

import java.util.Map;

public class PerfSummaryData {
	private Map<String, PerfSummaryItem> items;
	private PerfSummaryItem total;
	private int buildId;
	private String buildName;

	public PerfSummaryData() {
	}

	public PerfSummaryData(Map<String, PerfSummaryItem> items,
			PerfSummaryItem total) {
		this.items = items;
		this.total = total;
	}

	public Map<String, PerfSummaryItem> getItems() {
		return items;
	}

	public void setItems(Map<String, PerfSummaryItem> items) {
		this.items = items;
	}

	public PerfSummaryItem getTotal() {
		return total;
	}

	public void setTotal(PerfSummaryItem total) {
		this.total = total;
	}

	public int getBuildId() {
		return buildId;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
}
