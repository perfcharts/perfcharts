package perfcharts.chart;

import perfcharts.formatter.ChartFormatter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GenericTable extends Chart {
    private ChartFormatter<GenericTable> formatter;
    private String[] header;
    private String[] headerTooltip;
    private Map<String, Object> columnKeys;
    private Collection<TableCell[]> topRows;
    private Collection<TableCell[]> rows;
    private Collection<TableCell[]> bottomRows;
    private String[] footer;
    private String[] columnWidths;
    /**
     * An array of instructions for per-column sorting and direction in the format: [[columnIndex, sortDirection], ... ]
     * where columnIndex is a zero-based index for your columns left-to-right and sortDirection is 0 for Ascending and 1
     * for Descending. A valid argument that sorts ascending first by column 1 and then column 2 looks like:
     * [[0,0],[1,0]]
     */
    private int[][] sortList;

    public GenericTable(
            ChartFormatter<GenericTable> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String format() throws IOException, InterruptedException {
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

    public String[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(String[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public int[][] getSortList() {
        return sortList;
    }

    public void setSortList(int[][] sortList) {
        this.sortList = sortList;
    }
}
