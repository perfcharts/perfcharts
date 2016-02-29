package perfcharts.perftest.reporthandler;

import java.util.regex.Pattern;

/**
 * Created by vfreex on 2/26/16.
 */
public class PerfReportHandlerRule {
    private final Pattern inputFileNamePattern;
    private final String outputReportNamePattern;
    private final String category;
    private final DuplicatedAction duplicatedFileAction;

    public PerfReportHandlerRule(String inputFileNamePattern, String outputReportNamePattern, String category, DuplicatedAction duplicatedFileAction) {
        this.category = category;
        this.inputFileNamePattern = Pattern.compile(inputFileNamePattern);
        this.outputReportNamePattern = outputReportNamePattern;
        this.duplicatedFileAction = duplicatedFileAction;
    }

    public Pattern getInputFileNamePattern() {
        return inputFileNamePattern;
    }

    public String getOutputReportNamePattern() {
        return outputReportNamePattern;
    }

    public DuplicatedAction getDuplicatedFileAction() {
        return duplicatedFileAction;
    }

    public String getCategory() {
        return category;
    }

    public enum DuplicatedAction {
        IGNORE, OVERRIDE, APPEND
    }
}
