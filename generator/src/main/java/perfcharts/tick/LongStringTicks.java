package perfcharts.tick;

import java.util.Map;

public class LongStringTicks implements Ticks {
	private Map<Long, String> map;

	public LongStringTicks(Map<Long, String> map) {
		this.map = map;
	}

	public String format() {
		StringBuilder sb = new StringBuilder("[");
		for (long key : map.keySet()) {
			sb.append("[").append(key).append(",\"")
					.append(map.get(key).replace("\"", "\\\"")).append("\"],");
		}
		if (!map.isEmpty())
			sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
}
