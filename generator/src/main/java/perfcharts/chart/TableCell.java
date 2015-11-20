package perfcharts.chart;

public class TableCell {
	private Object value;
	private String valueType;
	private Object rawValue;
	private String cssClass;
	private Object tag;

	public TableCell() {
		super();
	}

	public TableCell(Object value) {
		super();
		this.value = value;
	}

	public TableCell(Object value, String valueType) {
		super();
		this.value = value;
		this.valueType = valueType;
	}

	public TableCell(Object value, String valueType, Object rawValue) {
		super();
		this.value = value;
		this.valueType = valueType;
		this.rawValue = rawValue;
	}

	public TableCell(Object value, String valueType, Object rawValue,
			String cssClass) {
		super();
		this.value = value;
		this.valueType = valueType;
		this.rawValue = rawValue;
		this.cssClass = cssClass;
	}

	public TableCell(Object value, String valueType, Object rawValue,
			String cssClass, Object tag) {
		super();
		this.value = value;
		this.valueType = valueType;
		this.rawValue = rawValue;
		this.cssClass = cssClass;
		this.tag = tag;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Object getRawValue() {
		return rawValue;
	}

	public void setRawValue(Object rawValue) {
		this.rawValue = rawValue;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

}
