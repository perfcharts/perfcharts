package perfcharts.tick;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import perfcharts.common.FieldSelector;

public class LongStringTickGenerator implements TickGenerator {
	private String labelPattern;
	private FieldSelector labelField;
	private FieldSelector valueField;
	private FieldSelector tickField;

	public LongStringTickGenerator(String labelPattern,
			FieldSelector labelField, FieldSelector valueField,
			FieldSelector tickField) {
		this.labelPattern = labelPattern;
		this.labelField = labelField;
		this.valueField = valueField;
		this.tickField = tickField;
	}

	public String getLabelPattern() {
		return labelPattern;
	}

	public void setLabelPattern(String labelPattern) {
		this.labelPattern = labelPattern;
	}

	public FieldSelector getLabelField() {
		return labelField;
	}

	public void setLabelField(FieldSelector labelField) {
		this.labelField = labelField;
	}

	public FieldSelector getValueField() {
		return valueField;
	}

	public void setValueField(FieldSelector valueField) {
		this.valueField = valueField;
	}

	public FieldSelector getTickField() {
		return tickField;
	}

	public void setTickField(FieldSelector tickField) {
		this.tickField = tickField;
	}

	@Override
	public LongStringTicks generate(List<List<Object>> dataRows) {
		Map<Long, String> valueTickMap = new TreeMap<Long, String>();
		Pattern pattern = Pattern.compile(labelPattern);
		for (List<Object> row : dataRows) {
			String label = labelField.select(row).toString();
			if (!pattern.matcher(label).matches())
				continue;
			long value = ((Number) valueField.select(row)).longValue();
			String tick = tickField.select(row).toString();
			valueTickMap.put(value, tick);
		}
		return new LongStringTicks(valueTickMap);
	}

}
