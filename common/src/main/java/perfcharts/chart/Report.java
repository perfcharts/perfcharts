package perfcharts.chart;

import java.util.Collection;

/**
 * A report is a collection of charts.
 *
 * @author Rayson Zhu
 */
public class Report {
    /**
     * the report title
     */
    private String title;
    private String subtitle;
    private String groupName;
    /**
     * charts that the report contains
     */
    private Collection<Chart> charts;

    /**
     * Initialize an empty report.
     */
    public Report() {

    }

    /**
     * Initialize a report with specified parameters.
     *
     * @param title  the report title
     * @param charts charts that the report contains
     */
    public Report(String title, Collection<Chart> charts) {
        this.title = title;
        this.charts = charts;
    }


    public Report(String title, String subtitle, String groupName, Collection<Chart> charts) {
        this.title = title;
        this.subtitle = subtitle;
        this.groupName = groupName;
        this.charts = charts;
    }

    /**
     * Get charts that the report contains.
     *
     * @return charts
     */
    public Collection<Chart> getCharts() {
        return charts;
    }

    /**
     * Set charts that the report contains.
     *
     * @param charts charts
    public Report(String title, String subtitle, String groupName, Collection&lt;Chart&gt; charts) {
        this.title = title;
        this.subtitle = subtitle;
        this.groupName = groupName;
        this.charts = charts;
    }
     */
    public void setCharts(Collection<Chart> charts) {
        this.charts = charts;
    }

    /**
     * Get the report title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the report title.
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
