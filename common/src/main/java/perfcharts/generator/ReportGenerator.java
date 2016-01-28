package perfcharts.generator;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import perfcharts.chart.Chart;
import perfcharts.chart.Report;
import perfcharts.config.ChartConfig;
import perfcharts.model.DataTable;

/**
 * This object generates a report that contains many charts by a collection of
 * {@link ChartConfig}s.
 * 
 * @author Rayson Zhu
 *
 */
public class ReportGenerator {
	/**
	 * a collection of {@link ChartConfig}s.
	 */
	private List<ChartConfig<Chart>> chartConfigs;

	/**
	 * constructor
	 */
	public ReportGenerator() {

	}

	/**
	 * constructor
	 * 
	 * @param chartConfigs
	 *            a collection of {@link ChartConfig}s.
	 */
	public ReportGenerator(List<ChartConfig<Chart>> chartConfigs) {
		this.chartConfigs = chartConfigs;
	}

	/**
	 * Generates a report that contains many charts from specified input stream.
	 * 
	 * @param in
	 *            a input stream
	 * @return a report
	 * @throws Exception
	 */
	public Report generate(InputStream in) throws Exception {
		// load data table
		final DataTableLoader loader = new DataTableLoader();
		final DataTable dataTable = loader.load(in);
		in.close();

		final int workingThreadsCount = Math.min(chartConfigs.size(), (int)(Runtime
				.getRuntime().availableProcessors() * 1.6));
		final Thread[] workingThreads = new Thread[workingThreadsCount];
		final AtomicInteger remainedTasks = new AtomicInteger(chartConfigs.size());
		final Chart[] charts = new Chart[chartConfigs.size()];
		for (int t = 0; t < workingThreads.length; t++) {
			workingThreads[t] = new Thread(new Runnable() {
				@Override
				public void run() {
					int taskId;
					while ((taskId = remainedTasks.decrementAndGet()) >= 0) {
						ChartConfig<Chart> cfg = chartConfigs.get(taskId);
						try {
						ChartFactory<Chart> factory = cfg.createChartFactory();
						ChartGenerator generator = factory.createGenerator(cfg);
						charts[taskId] = generator.generate(dataTable);
						} catch (Exception e){
							e.printStackTrace();
							System.exit(1);
						}
					}
				}
			});
		}
		for (int t = 0; t < workingThreads.length; t++) {
			workingThreads[t].start();
		}
		for (int t = 0; t < workingThreads.length; t++) {
			workingThreads[t].join();
		}

//		for (ChartConfig<Chart> cfg : chartConfigs) {
//			// for each ChartConfig, create associate factory and generate
//			// corresponding chart
//			ChartFactory<Chart> factory = cfg.createChartFactory();
//			Generator generator = factory.createGenerator(cfg);
//			charts.add(generator.generate(dataTable));
//		}
		return new Report(null, Arrays.asList(charts));
	}

	/**
	 * Get a collection of {@link ChartConfig}s.
	 * 
	 * @return a collection of {@link ChartConfig}s.
	 */
	public List<ChartConfig<Chart>> getChartConfigs() {
		return chartConfigs;
	}

	/**
	 * Set a collection of {@link ChartConfig}s.
	 * 
	 * @param lineGraphConfigs
	 *            a collection of {@link ChartConfig}s.
	 */
	public void setChartConfigs(List<ChartConfig<Chart>> lineGraphConfigs) {
		this.chartConfigs = lineGraphConfigs;
	}
}
