package perfcharts.chart;

import java.util.Collection;
import java.util.Map;

import perfcharts.formatter.ChartFormatter;

public class GenericTable extends Chart {
	private ChartFormatter<GenericTable> formatter;
	private String[] header;
	private String[] headerTooltip;
	private Map<String, Object> columnKeys;
	private Collection<TableCell[]> topRows;
	private Collection<TableCell[]> rows;
	private Collection<TableCell[]> bottomRows;
	private String[] footer;
	
	public GenericTable(
			ChartFormatter<GenericTable> formatter) {
		this.formatter = formatter;
	}

	@Override
	public String format() throws Exception {
		return formatter.format(this);
	}

	public ChartFormatter<GenericTable> getFormatter() {
		return formatter;
	}

	public void setFormatter(
			ChartFormatter<GenericTable> formatter) {
		this.formatter = formatter;
	}

	public String[] getHeader() {
		return header;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public Collection<TableCell[]> getRows() {
		return rows;
	}

	public void setRows(Collection<TableCell[]> rows) {
		this.rows = rows;
	}

	public String[] getFooter() {
		return footer;
	}

	public void setFooter(String[] footer) {
		this.footer = footer;
	}

	public Map<String, Object> getColumnKeys() {
		return columnKeys;
	}

	public void setColumnKeys(Map<String, Object> columnKeys) {
		this.columnKeys = columnKeys;
	}

	public Collection<TableCell[]> getTopRows() {
		return topRows;
	}

	public void setTopRows(Collection<TableCell[]> topRows) {
		this.topRows = topRows;
	}

	public Collection<TableCell[]> getBottomRows() {
		return bottomRows;
	}

	public void setBottomRows(Collection<TableCell[]> bottomRows) {
		this.bottomRows = bottomRows;
	}

	public String[] getHeaderTooltip() {
		return headerTooltip;
	}

	public void setHeaderTooltip(String[] headerTooltip) {
		this.headerTooltip = headerTooltip;
	}
}
