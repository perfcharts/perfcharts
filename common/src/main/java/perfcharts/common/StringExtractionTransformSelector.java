package perfcharts.common;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtractionTransformSelector implements FieldSelector<String> {
	private Pattern pattern;
	private String replacement;
	private FieldSelector<String> sourceSelector;
	public StringExtractionTransformSelector(FieldSelector<String> sourceSelector, String regex, String replacement) {
		super();
		this.pattern = Pattern.compile(regex);
		this.replacement = replacement;
		this.sourceSelector = sourceSelector;
	}
	public StringExtractionTransformSelector(FieldSelector<String> sourceSelector, Pattern pattern, String replacement) {
		super();
		this.pattern = pattern;
		this.replacement = replacement;
		this.sourceSelector = sourceSelector;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public String getReplacement() {
		return replacement;
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	@Override
	public String select(List<?> row) {
		Matcher m = pattern.matcher(sourceSelector.select(row));
		return m.replaceAll(replacement);
	}
	
}
