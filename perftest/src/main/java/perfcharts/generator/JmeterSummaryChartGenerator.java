package perfcharts.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import perfcharts.chart.GenericTable;
import perfcharts.chart.TableCell;
import perfcharts.common.AddTransformSelector;
import perfcharts.common.FieldSelector;
import perfcharts.common.IndexFieldSelector;
import perfcharts.common.Utilities;
import perfcharts.model.DataTable;
import perfcharts.config.JmeterSummaryChartConfig;

/**
 * A generator reads data tables and produces {@link JmeterSummaryChart}
 * according to preset configurations.
 * 
 * @author Rayson Zhu
 *
 */
public class JmeterSummaryChartGenerator implements ChartGenerator {
	private final static Logger LOGGER = Logger
			.getLogger(JmeterSummaryChartGenerator.class.getName());
	/**
	 * The factory for creating related objects.
	 */
	private JmeterSummaryChartFactory factory;
	/**
	 * the chart configuration
	 */
	private JmeterSummaryChartConfig config;

	/**
	 * constructor
	 */
	public JmeterSummaryChartGenerator() {
	}

	/**
	 * constructor
	 * 
	 * @param factory
	 *            The factory for creating related objects
	 * @param config
	 *            the chart configuration
	 */
	public JmeterSummaryChartGenerator(JmeterSummaryChartFactory factory,
			JmeterSummaryChartConfig config) {
		this.config = config;
		this.factory = factory;
	}

	@Override
	public GenericTable generate(DataTable log) throws Exception {
		LOGGER.info("Generating Perf Summary Table '" + config.getTitle()
				+ "' (" + config.getKey() + ")...");
		FieldSelector labelField = new IndexFieldSelector(0);
		final FieldSelector timestampField = new IndexFieldSelector(1);
		final FieldSelector rtField = new IndexFieldSelector(5);
		final FieldSelector xField = new AddTransformSelector(timestampField,
				rtField);
		final FieldSelector errorField = new IndexFieldSelector(3);
		final FieldSelector bytesField = new IndexFieldSelector(6);
		final FieldSelector latencyField = new IndexFieldSelector(4);
		// map table row label to related data rows
		final Map<String, List<List<Object>>> involvedRows = new HashMap<String, List<List<Object>>>();
		Pattern txPattern = Pattern.compile("^TX-(.+)-[SF]$");
		// filter involved rows
		for (List<Object> row : log.getRows()) {
			Matcher m = txPattern.matcher(labelField.select(row).toString());
			if (m.matches()) {
				String key = m.replaceAll("$1");
				List<List<Object>> list = involvedRows.get(key);
				if (list == null)
					involvedRows
							.put(key, list = new LinkedList<List<Object>>());
				list.add(row);
			}
		}
		// generate table column labels
		String[] header = new String[] { "Transation", "#Samples", "Average",
				"Min", "90% Line", "Max", "Latency", "Std. Dev.", "Error%",
				"Throughput (tx/h)", "KiB/sec", "Avg. Bytes" };
		Map<String, Object> columnKeys = new HashMap<String, Object>();
		for (int i = 0; i < header.length; i++) {
			columnKeys.put(header[i], i);
		}
		List<TableCell[]> tableRows = new ArrayList<TableCell[]>();

		// variables for the TOTAL row
		long minTimestampTotal = Long.MAX_VALUE;
		long maxTimestampTotal = Long.MIN_VALUE;
		long sumRTTotal = 0;
		long numRTsuccessTotal = 0;
		long numRTfailureTotal = 0;
		long minRTTotal = Integer.MAX_VALUE;
		long maxRTTotal = Integer.MIN_VALUE;
		double sumRTSquaredTotal = 0;
		long bytesSumTotal = 0;
		long sumLatencyTotal = 0;
		List<Long> RTsTotal = new LinkedList<Long>();

		// for each series (table row), do calculations
		for (Map.Entry<String, List<List<Object>>> series : involvedRows
				.entrySet()) {
			List<List<Object>> rows = series.getValue();
			if (rows.isEmpty())
				continue;

			long minTimestamp = Long.MAX_VALUE;
			long maxTimestamp = Long.MIN_VALUE;
			long sumRT = 0;
			long numRTsuccess = 0;
			long numRTfailure = 0;
			long minRT = Integer.MAX_VALUE;
			long maxRT = Integer.MIN_VALUE;
			double sumRTSquared = 0;
			long bytesSum = 0;
			long sumLatency = 0;

			List<Long> RTs = new LinkedList<Long>();

			for (List<Object> row : rows) {
				long rt = ((Number) rtField.select(row)).longValue();
				long ts = ((Number) xField.select(row)).longValue();
				if (ts < minTimestamp)
					minTimestamp = ts;
				if (ts + rt > maxTimestamp)
					maxTimestamp = ts + rt;
				if (((Number) errorField.select(row)).intValue() == 0) {
					RTs.add(rt);
					numRTsuccess++;
					sumRT += rt;
					sumLatency += ((Number)latencyField.select(row)).longValue();
					if (rt < minRT)
						minRT = rt;
					if (rt > maxRT)
						maxRT = rt;
					sumRTSquared += rt * rt;
					bytesSum += ((Number) bytesField.select(row)).longValue();
				} else {
					numRTfailure++;
				}
			}

			long duration = maxTimestamp - minTimestamp + 1;

			TableCell[] tableRow = new TableCell[header.length];
			tableRow[0] = new TableCell(series.getKey());
			long samples = numRTsuccess + numRTfailure;
			tableRow[1] = new TableCell(samples);

			if (!RTs.isEmpty()) {
				double avgRT = 1.0 * sumRT / numRTsuccess;
				tableRow[2] = new TableCell(avgRT);
				if (Double.isInfinite(avgRT) || Double.isNaN(avgRT)
						|| avgRT >= 5000) {
					tableRow[2].setCssClass("perfcharts_warning");
					tableRow[0].setCssClass("perfcharts_warning");
				}
				tableRow[3] = new TableCell(minRT);
				tableRow[4] = new TableCell(Utilities.fastSelect(RTs,
						(int) Math.round((RTs.size() - 1) * 0.9)).doubleValue());
				tableRow[5] = new TableCell(maxRT);
				double avgLatency = 1.0 * sumLatency / numRTsuccess;
				tableRow[6] = new TableCell(avgLatency);
				// std. dev. = sqrt(average of (x^2) - (average of x)^2)
				tableRow[7] = new TableCell(Math.sqrt(sumRTSquared
						/ numRTsuccess - avgRT * avgRT));

				// tableRow[7] = new TableCell(100.0 * numRTfailure / samples);
				double throughput = 1.0 * numRTsuccess / duration;
				// tableRow[8] = new TableCell(formatThroughput(throughput),
				// "string",
				// throughput);
				tableRow[9] = new TableCell(throughput * 1000 * 60 * 60);
				// tableRow[8].setCssClass("perfcharts-tx-summary-throughput");
				tableRow[10] = new TableCell(bytesSum / 1.024 / duration);
				tableRow[11] = new TableCell(1.0 * bytesSum / numRTsuccess);
			} else {
				tableRow[2] = new TableCell(Double.NaN);
				tableRow[3] = new TableCell(Double.NaN);
				tableRow[4] = new TableCell(Double.NaN);
				tableRow[5] = new TableCell(Double.NaN);
				tableRow[6] = new TableCell(Double.NaN);
				tableRow[7] = new TableCell(Double.NaN);
				tableRow[8] = new TableCell(Double.NaN);
				tableRow[9] = new TableCell(Double.NaN);
				tableRow[10] = new TableCell(Double.NaN);
				tableRow[11] = new TableCell(Double.NaN);
			}
			double errorPerc = samples > 0 ? 100.0 * numRTfailure / samples
					: Double.NaN;
			tableRow[8] = new TableCell(errorPerc);
			if (Double.isInfinite(errorPerc) || Double.isNaN(errorPerc)
					|| errorPerc > 0.0) {
				tableRow[8].setCssClass("perfcharts_warning");
				tableRow[0].setCssClass("perfcharts_warning");
			}
			tableRows.add(tableRow);

			if (minTimestampTotal > minTimestamp)
				minTimestampTotal = minTimestamp;
			if (maxTimestampTotal < maxTimestamp)
				maxTimestampTotal = maxTimestamp;
			if (minRTTotal > minRT)
				minRTTotal = minRT;
			if (maxRTTotal < maxRT)
				maxRTTotal = maxRT;

			numRTsuccessTotal += numRTsuccess;
			numRTfailureTotal += numRTfailure;
			sumRTTotal += sumRT;
			sumRTSquaredTotal += sumRTSquared;
			bytesSumTotal += bytesSum;
			sumLatencyTotal += sumLatency;
			RTsTotal.addAll(RTs);
		}

		// sort rows by transaction name
		Collections.sort(tableRows, new Comparator<TableCell[]>() {
			@Override
			public int compare(TableCell[] o1, TableCell[] o2) {
				if (o1 == null || o1.length == 0 || o2 == null
						|| o2.length == 0)
					return 0;
				return o1[0].getValue().toString()
						.compareTo(o2[0].getValue().toString());
			}
		});

		// generate the TOTAL row
		TableCell[] totalRow = new TableCell[header.length];
		totalRow[0] = new TableCell("TOTAL");
		long samplesTotal = numRTsuccessTotal + numRTfailureTotal;
		totalRow[1] = new TableCell(samplesTotal);
		double avgRTTotal = 1.0 * sumRTTotal / numRTsuccessTotal;
		totalRow[2] = new TableCell(avgRTTotal);
		totalRow[3] = new TableCell(minRTTotal);
		totalRow[4] = new TableCell(RTsTotal.isEmpty() ? Double.NaN : Utilities
				.fastSelect(RTsTotal,
						(int) Math.round((RTsTotal.size() - 1) * 0.9))
				.doubleValue());
		totalRow[5] = new TableCell(maxRTTotal);
		double avgLatencyTotal = 1.0 * sumLatencyTotal / numRTsuccessTotal;
		totalRow[6] = new TableCell(avgLatencyTotal);
		totalRow[7] = (new TableCell(Math.sqrt(sumRTSquaredTotal
				/ numRTsuccessTotal - avgRTTotal * avgRTTotal)));
		totalRow[8] = new TableCell(100.0 * numRTfailureTotal / samplesTotal);
		long durationTotal = maxTimestampTotal - minTimestampTotal + 1;
		double throughput = 1.0 * numRTsuccessTotal / durationTotal;
		// totalRow[8] = new TableCell(formatThroughput(throughput), "string",
		// throughput);
		totalRow[9] = new TableCell(throughput * 1000 * 60 * 60);
		totalRow[10] = new TableCell(bytesSumTotal / 1.024 / durationTotal);
		totalRow[11] = new TableCell(1.0 * bytesSumTotal / numRTsuccessTotal);
		// tableRows.add(totalRow);
		List<TableCell[]> bottomRows = new ArrayList<TableCell[]>(1);
		bottomRows.add(totalRow);

		// complete the chart
		GenericTable chart = new GenericTable(factory.createFormatter());
		chart.setTitle(config.getTitle());
		chart.setSubtitle(config.getSubtitle());
		chart.setKey(config.getKey());
		chart.setHeader(header);
		chart.setColumnKeys(columnKeys);
		chart.setRows(tableRows);
		chart.setBottomRows(bottomRows);
		return chart;
	}

	// /**
	// * format the throughput data
	// *
	// * @param throughput
	// * throughput
	// * @return a string
	// */
	// private String formatThroughput(double throughput) {
	// if (throughput >= 1.0)
	// return String.format("%.2f/ms", throughput);
	// throughput *= 1000;
	// if (throughput >= 1.0)
	// return String.format("%.2f/s", throughput);
	// throughput *= 60;
	// if (throughput >= 1.0)
	// return String.format("%.2f/min", throughput);
	// throughput *= 60;
	// return String.format("%.2f/h", throughput);
	// }

	/**
	 * Get the configuration of this chart.
	 * 
	 * @return the configuration
	 */
	public JmeterSummaryChartConfig getConfig() {
		return config;
	}

	/**
	 * Set the configuration of this chart.
	 * 
	 * @param config
	 *            the configuration
	 */
	public void setConfig(JmeterSummaryChartConfig config) {
		this.config = config;
	}

}
