package perfcharts.generator;

import perfcharts.common.FieldSelector;
import perfcharts.config.Chart2DSeriesConfig;
import perfcharts.config.Chart2DSeriesConfigRule;
import perfcharts.config.Chart2DSeriesExclusionRule;
import perfcharts.config.ReportConfig;
import perfcharts.model.DataTable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link Chart2DSeriesConfigBuilder} creates a list of
 * {@link Chart2DSeriesConfig}s by given {@link Chart2DSeriesConfigRule}.
 *
 * @author Rayson Zhu
 */
public class Chart2DSeriesConfigBuilder {
    /**
     * create a list of {@link Chart2DSeriesConfig}s by given
     * {@link Chart2DSeriesConfigRule}.
     *
     * @param rule      a rule
     * @param dataTable a data table
     * @return a list of {@link Chart2DSeriesConfig}s
     */
    public List<Chart2DSeriesConfig> build(final Chart2DSeriesConfigRule rule,
                                           DataTable dataTable) {
        FieldSelector labelField = rule.getLabelField();
        String seriesLabelFormat = rule.getSeriesLabelFormat();
        FieldSelector<String> seriesLabelField = rule.getSeriesLabelField();
        List<List<Object>> rows = dataTable.getRows();
        Pattern pattern = Pattern.compile(rule.getLabelPattern());
        // define the map from series label to its involved row labels.
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        // traverse all data rows, add labels of rows that matches the patterns
        // specified by rules to @map.
        Chart2DSeriesExclusionRule exclusionRule = rule.getExclusionRule();
        Pattern exclusionPattern = exclusionRule != null ? Pattern
                .compile(exclusionRule.getPattern()) : null;
        for (List<Object> row : rows) {
            String rowLabel = labelField.select(row).toString();
            Matcher matcher = pattern.matcher(rowLabel);
            if (matcher.matches()) {
                if (exclusionRule != null) {
                    String source = matcher.replaceAll(exclusionRule
                            .getSource());
                    if (exclusionPattern.matcher(source).matches())
                        continue; // exclude this row;
                }

                String seriesLabel = seriesLabelField != null ? seriesLabelField.select(row) : matcher.replaceAll(seriesLabelFormat);
                Set<String> labels = map.get(seriesLabel);
                if (labels == null)
                    map.put(seriesLabel, labels = new HashSet<String>());
                labels.add(rowLabel);
            }
        }
        // generate seriesConfigs for each series
        final List<Chart2DSeriesConfig> seriesConfigs = new ArrayList<Chart2DSeriesConfig>(
                map.size());
        for (String seriesLabel : map.keySet()) {
            Set<String> involvedRowLabels = map.get(seriesLabel);
            seriesConfigs.add(new Chart2DSeriesConfig(seriesLabel, rule
                    .getUnit(), rule.getLabelField(), rule.getXField(), rule
                    .getYField(), rule.getCalculation(), involvedRowLabels,
                    rule.isShowLine(), rule.isShowBar(), rule.isShowUnit()));
        }
        return seriesConfigs;
    }
}
