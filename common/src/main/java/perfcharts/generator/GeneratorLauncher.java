package perfcharts.generator;

import perfcharts.chart.Report;
import perfcharts.config.ReportConfig;
import perfcharts.config.ReportConfigLoader;

import java.io.*;

/**
 * Contains the entry point of the generator module.
 *
 * @author Rayson Zhu
 */
public class GeneratorLauncher {
    /**
     * The entry point
     *
     * @param args Usage: &lt;CONFIG_FILE&gt;
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: \n<CONFIG_FILE>");
            return;
        }
        String configFilePath = args[0];

        ReportConfigLoader reportConfigLoader = new ReportConfigLoader();
        ReportConfig reportConfig = reportConfigLoader.load(configFilePath);

        // specify the input stream and output stream
        InputStream in = reportConfig.getInputFile() == null
                || reportConfig.getInputFile().isEmpty() ? System.in
                : new FileInputStream(getFile(reportConfig.getBasePath(), reportConfig.getInputFile()));
        OutputStream out = reportConfig.getOutputFile() == null
                || reportConfig.getOutputFile().isEmpty() ? System.out
                : new FileOutputStream(getFile(reportConfig.getBasePath(), reportConfig.getOutputFile()));
        // generate a report through ReportGenerator
        ReportGenerator generator = new ReportGenerator(reportConfig);
        Report report = generator.generate(in);
        // set the report title
        if (reportConfig.getTitle() != null && !reportConfig.getTitle().isEmpty())
            report.setTitle(reportConfig.getTitle());
        // write to output stream
        ReportWritter reportWritter = new ReportWritter();
        reportWritter.write(report, out);
    }

    private static File getFile(String parent, String relative) {
        if (relative.startsWith("/") || relative.startsWith("file:"))
            return new File(relative);
        else
            return new File(parent, relative);
    }
}
