package perfcharts.perftest.parser.zabbix;
import java.util.Date;


public class ZabbixHistory {
	private int itemID;
	private Date timestop;
	private int valueType;
	private String value;

	public ZabbixHistory(int itemID, Date timestop, int valueType, String value) {
		this.itemID = itemID;
		this.timestop = timestop;
		this.valueType = valueType;
		this.value = value;
	}

	public int getItemID() {
		return itemID;
	}

	public void setIID(int itemID) {
		this.itemID = itemID;
	}

	public Date getTimestop() {
		return timestop;
	}

	public void setTimestop(Date timestop) {
		this.timestop = timestop;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
