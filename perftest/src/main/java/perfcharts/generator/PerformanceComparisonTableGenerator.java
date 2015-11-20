package perfcharts.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import perfcharts.chart.GenericTable;
import perfcharts.chart.TableCell;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.config.PerformanceComparisonTableConfig;
import perfcharts.model.DataTable;

public class PerformanceComparisonTableGenerator implements ChartGenerator {
	private final static Logger LOGGER = Logger
			.getLogger(PerformanceComparisonTableGenerator.class.getName());
	private PerformanceComparisonTableConfig config;

	public PerformanceComparisonTableGenerator(
			PerformanceComparisonTableConfig config) {
		this.config = config;
	}

	private final static Pattern txPattern = Pattern.compile("TX-(.+)");

	@Override
	public GenericTable generate(DataTable dataTable) throws Exception {
		LOGGER.info("Generating Perf Comparison Table '" + config.getTitle()
				+ "' (" + config.getKey() + ")...");
		GenericTable table = new GenericTable(config.createChartFactory()
				.createFormatter());
		table.setTitle(config.getTitle());
		table.setSubtitle(config.getSubtitle());
		table.setKey("perfcharts-table-perfcmp");
		final int columns = 14;
		table.setHeader(new String[] { "Transaction", "#Samples",
				"#Samples diff", "Average", "Average diff", "Average diff%",
				"90% line", "90% line diff", "90% line diff%",
				"Throughput (tx/h)", "Throughput diff", "Throughput diff%",
				"Error%", "Error% diff" });
		FieldSelector labelField = new IndexFieldSelector(0);
		FieldSelector sSampleField = new IndexFieldSelector(1);
		FieldSelector dSampleField = new IndexFieldSelector(2);
		FieldSelector sAverageField = new IndexFieldSelector(3);
		FieldSelector dAverageField = new IndexFieldSelector(4);
		FieldSelector s90LineField = new IndexFieldSelector(5);
		FieldSelector d90LineField = new IndexFieldSelector(6);
		FieldSelector sErrorField = new IndexFieldSelector(7);
		FieldSelector dErrorField = new IndexFieldSelector(8);
		FieldSelector sThroughputField = new IndexFieldSelector(9);
		FieldSelector dThroughputField = new IndexFieldSelector(10);
		TableCell[] totalRow = null;// = new TableCell[columns];
		Map<String, TableCell[]> tx2RowMap = new TreeMap<String, TableCell[]>();
		for (List<Object> dataRow : dataTable.getRows()) {
			String label = labelField.select(dataRow).toString();
			TableCell[] row = null;
			if ("TOTAL".equals(label)) {
				row = totalRow = new TableCell[columns];
				row[0] = new TableCell("TOTAL");
			} else {
				Matcher m = txPattern.matcher(label);
				if (m.matches()) {
					String txName = m.group(1);
					row = new TableCell[columns];
					row[0] = new TableCell(txName);
					tx2RowMap.put(txName, row);
				} else {
					continue;
				}
			}
			long sSample = (long) sSampleField.select(dataRow);
			long dSample = (long) dSampleField.select(dataRow);
			row[1] = new TableCell(sSample);
			row[2] = new TableCell(sSample - dSample);
			double sAverage = (double) sAverageField.select(dataRow);
			double dAverage = (double) dAverageField.select(dataRow);
			double diffAverage = sAverage - dAverage;
			row[3] = new TableCell(sAverage);
			row[4] = new TableCell(diffAverage);
			double diffAveragePercentage = diffAverage * 100.0 / dAverage;
			row[5] = new TableCell(diffAveragePercentage);
			/*if (Double.isInfinite(diffAveragePercentage)|| diffAveragePercentage >= 50.0){
				row[5].setCssClass("perfcharts_warning");
				//row[0].setCssClass("perfcharts_warning");
			} else */if (diffAveragePercentage <= -50.0){
				row[5].setCssClass("perfcharts_fine");
			}
			
			double s90Line = (double) s90LineField.select(dataRow);
			double d90Line = (double) d90LineField.select(dataRow);
			row[6] = new TableCell(s90Line);
			row[7] = new TableCell(s90Line - d90Line);
			row[8] = new TableCell((s90Line - d90Line) * 100.0 / d90Line);
			double sThroughput = (double) sThroughputField.select(dataRow);
			double dThroughput = (double) dThroughputField.select(dataRow);
			row[9] = new TableCell(sThroughput);
			row[10] = new TableCell(sThroughput - dThroughput);
			row[11] = new TableCell((sThroughput - dThroughput) * 100.0
					/ dThroughput);
			double sError = (double) sErrorField.select(dataRow);
			double dError = (double) dErrorField.select(dataRow);
			row[12] = new TableCell(sError);
			if (Double.isInfinite(sError) || Double.isNaN(sError)
					|| sError > 0.0){
				row[12].setCssClass("perfcharts_warning");
				row[0].setCssClass("perfcharts_warning");
			}
			row[13] = new TableCell(sError - dError);
		}
		List<TableCell[]> rows = new ArrayList<TableCell[]>(tx2RowMap.size());
		for (Map.Entry<String, TableCell[]> entry : tx2RowMap.entrySet())
			rows.add(entry.getValue());
		// rows.add(totalRow);
		table.setRows(rows);
		if (totalRow != null) {
			List<TableCell[]> bottomRows = new ArrayList<TableCell[]>(1);
			bottomRows.add(totalRow);
			table.setBottomRows(bottomRows);
		}
		return table;
	}

	public PerformanceComparisonTableConfig getConfig() {
		return config;
	}

	public void setConfig(PerformanceComparisonTableConfig config) {
		this.config = config;
	}

}
