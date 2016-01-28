package perfcharts.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import perfcharts.calc.Chart2DCalculation;
import perfcharts.chart.Chart2D;
import perfcharts.chart.Chart2DSeries;
import perfcharts.chart.Point2D;
import perfcharts.common.AppData;
import perfcharts.common.FieldSelector;
import perfcharts.config.BarChartStringIDMapper;
import perfcharts.config.Chart2DConfig;
import perfcharts.config.Chart2DSeriesConfig;
import perfcharts.config.Chart2DSeriesConfigRule;
import perfcharts.config.Chart2DXValueComparator;
import perfcharts.config.SeriesOrder;
import perfcharts.model.DataTable;
import perfcharts.tick.TickGenerator;

/**
 * A generator reads data tables and produces {@link Chart2D} according to
 * preset configurations.
 * 
 * @author Rayson Zhu
 *
 */
public class Chart2DGenerator implements ChartGenerator {
	/**
	 * the chart configuration
	 */
	private Chart2DConfig chart2dConfig;
	/**
	 * the default points per chart for automatic interval selection
	 */
	private final static int DEFAULT_POINTS = 300;
	/**
	 * The factory for creating related objects.
	 */
	private Chart2DFactory factory;

	/**
	 * constructor
	 */
	public Chart2DGenerator() {

	}

	/**
	 * constructor
	 * 
	 * @param factory
	 *            The factory for creating related objects
	 * @param chart2dConfig
	 *            the chart configuration
	 */
	public Chart2DGenerator(Chart2DFactory factory, Chart2DConfig chart2dConfig) {
		this.chart2dConfig = chart2dConfig;
		this.factory = factory;
	}

	public Chart2D generate(final DataTable dataTable) throws Exception {
		Logger logger = AppData.getInstance().getLogger();
		logger.info("Generating 2D chart '" + chart2dConfig.getTitle() + "' ("
				+ chart2dConfig.getKey() + ")...");
		final List<List<Object>> dataRows = dataTable.getRows();

		final Chart2DSeriesConfigBuilder seriesConfigBuilder = new Chart2DSeriesConfigBuilder();
		List<Chart2DSeriesConfigRule> rules = chart2dConfig.getRules();
		final List<Chart2DSeriesConfig> seriesConfigs = new ArrayList<Chart2DSeriesConfig>();

		// traverse the rules and collect all generated SeriesConfigRules for
		// this chart
		for (Chart2DSeriesConfigRule rule : rules)
			seriesConfigs.addAll(seriesConfigBuilder.build(rule, dataTable));

		// sort series
		// Collections.sort(seriesConfigs, new Comparator<Chart2DSeriesConfig>()
		// {
		// @Override
		// public int compare(Chart2DSeriesConfig o1, Chart2DSeriesConfig o2) {
		// return 0;
		// }
		// });

		final List<Chart2DSeries> series = new ArrayList<Chart2DSeries>(
				seriesConfigs.size());
		Chart2DXValueComparator comparator = chart2dConfig
				.getXValueComparator();
		for (final Chart2DSeriesConfig seriesConfig : seriesConfigs) {
			LinkedList<List<Object>> involvedRows = new LinkedList<List<Object>>();
			// collect involved rows
			for (List<Object> row : dataRows) {
				if (seriesConfig.getInvolvedRowLabels().contains(
						seriesConfig.getLabelField().select(row)))
					involvedRows.add(row);

			}
			if (involvedRows.isEmpty())
				continue;
			if (comparator != null) {
				comparator.setSeriesConfig(seriesConfig);
				Collections.sort(involvedRows, comparator);
			}
			Chart2DCalculation calc = seriesConfig.getCalculation();

			int interval = chart2dConfig.getInterval();
			// If user didn't specify the interval, produce one.
			if (interval == 0) {
				FieldSelector xField = seriesConfig.getXField();
				long minX = ((Number) xField.select(involvedRows.getFirst()))
						.longValue();
				long maxX = ((Number) xField.select(involvedRows.getLast()))
						.longValue();
				interval = (int) (maxX - minX) / DEFAULT_POINTS;
				if (interval < 1)
					interval = 1;
				calc.setInterval(interval);
				/*
				 * logger.info("use automatic interval value " + interval +
				 * " for series '" + seriesConfig.getLabel() + "' in chart '" +
				 * chart2dConfig.getTitle() + "'");
				 */
			}
			// execute the calculation defined by Chart2DSeriesConfig, and
			// collect generated points
			List<Point2D> lineStops = calc.produce(involvedRows,
					seriesConfig.getXField(), seriesConfig.getYField());
			if (!lineStops.isEmpty()) {
				Chart2DSeries line = new Chart2DSeries(seriesConfig.getLabel(),
						seriesConfig.getUnit(), lineStops,
						seriesConfig.isShowLine(), seriesConfig.isShowBar(),
						seriesConfig.isShowUnit());
				series.add(line);
			}
		}

		SeriesOrder order = chart2dConfig.getSeriesOrder();
		if (order != null && order != SeriesOrder.NONE) {
			switch (order) {
			case SERIES_LABEL:
				Collections.sort(series, new Comparator<Chart2DSeries>() {
					@Override
					public int compare(Chart2DSeries o1, Chart2DSeries o2) {
						return o1.getLabel().compareTo(o2.getLabel());
					}
				});
				break;
			case FIRST_POINT_Y:
				Collections.sort(series, new Comparator<Chart2DSeries>() {
					@Override
					public int compare(Chart2DSeries o1, Chart2DSeries o2) {
						if (o1.getStops().isEmpty() || o2.getStops().isEmpty())
							return 0;
						double y1 = o1.getStops().get(0).getY();
						double y2 = o2.getStops().get(0).getY();
						return Double.compare(y1, y2);
					}
				});
				break;
			case FIRST_POINT_Y_DESC:
				Collections.sort(series, new Comparator<Chart2DSeries>() {
					@Override
					public int compare(Chart2DSeries o1, Chart2DSeries o2) {
						if (o1.getStops().isEmpty() || o2.getStops().isEmpty())
							return 0;
						double y1 = o1.getStops().get(0).getY();
						double y2 = o2.getStops().get(0).getY();
						return Double.compare(y2, y1);
					}
				});
				break;
			default:
				break;
			}
		}
		Chart2D graph = new Chart2D(chart2dConfig.getTitle(),
				chart2dConfig.getSubtitle(), chart2dConfig.getXLabel(),
				chart2dConfig.getYLabel(), series, chart2dConfig.getXaxisMode());
		// set up appropriate formatter
		graph.setFormatter(factory.createFormatter());
		TickGenerator xTickGenerator = chart2dConfig.getXTickGenerator();
		if (xTickGenerator != null) {
			graph.setXTicks(xTickGenerator.generate(dataRows));
		}
		BarChartStringIDMapper stringIDMapper = chart2dConfig
				.getStringIDMapper();
		if (stringIDMapper != null) {
			graph.setBarChartStringIDMap(stringIDMapper.map(graph,
					chart2dConfig));
		}
		return graph;
	}

	/**
	 * Get the configuration of this chart.
	 * 
	 * @return the configuration
	 */
	public Chart2DConfig getChart2DConfig() {
		return chart2dConfig;
	}

	/**
	 * Set the configuration of this chart.
	 * 
	 * @param chart2DConfig
	 *            the configuration
	 */
	public void setChart2DConfig(Chart2DConfig chart2DConfig) {
		this.chart2dConfig = chart2DConfig;
	}

}
