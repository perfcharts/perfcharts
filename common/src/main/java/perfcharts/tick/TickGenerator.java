package perfcharts.tick;

import java.util.List;

public interface TickGenerator {
	public Ticks generate(List<List<Object>> dataRows);
}
