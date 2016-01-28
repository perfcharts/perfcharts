package perfcharts.config;

public class Chart2DSeriesExclusionRule {
	private String source;
	private String pattern;
	public Chart2DSeriesExclusionRule(String source, String pattern) {
		this.source = source;
		this.pattern = pattern;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
