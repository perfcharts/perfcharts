package perfcharts.perftest.parser.zabbix;

public class ZabbixItem {
	private int itemID;
	private String key;
	private int valueType;
	private String name;
	private int hostID;

	public ZabbixItem(int itemID, String key, int valueType, String name,
			int hostID) {
		this.itemID = itemID;
		this.key = key;
		this.valueType = valueType;
		this.name = name;
		this.hostID = hostID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHostID() {
		return hostID;
	}

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

}