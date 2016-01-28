package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import perfcharts.config.ChartConfig;
import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.*;
import perfcharts.generator.ChartFactory;
import perfcharts.handler.ReportTypeHandler;
import perfcharts.perftest.parser.DataParser;
import perfcharts.perftest.parser.DataParserFactory;
import perfcharts.perftest.parser.DataParserFactoryImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vfreex on 1/19/16.
 */
public class PerfBaselineReportHandler implements ReportTypeHandler {
    private final static Logger LOGGER = Logger.getLogger(PerfBaselineReportHandler.class.getName());
    private final static String reportTemplateHTML = "res/report_templates/perf-baseline/html/report-mono.template.html";
    private final static Options options;

    static {
        options = new Options();
        options.addOption("h", false, "show help message.");
    }

    private List<ReportConfig> reportConfigs = new ArrayList<>();

    public PerfBaselineReportHandler() {
    }

    private static ReportConfig createPerformanceReportConfig() {
        ReportConfig config = new ReportConfig();
        config.setTitle("Performance");
        //config.setBasePath();
        List<ChartConfigTemplate> configTemplates = new ArrayList<>();
        configTemplates.add(new JmeterSimpleSummaryTableTemplate());
        configTemplates.add(new TopTxWithHighestAvgRTBarChartTemplate());
        configTemplates.add(new JmeterAverageRTChartTemplate());
        configTemplates.add(new JmeterRTChartTemplate());
        config.setConfigTemplate(configTemplates);
        return config;
    }

    /*
    Args: [options] input_dir
     */
    @Override
    public void handle(List<String> argList) throws IOException, InterruptedException {
        // parse command line
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, argList.toArray(new String[0]));
        } catch (ParseException e) {
            throw new IOException(e);
        }
        if (cmd.hasOption("h")) {
            System.out.println("Usage: <input_directory>");
            //System.out.println(cmd.getOptionValue("h"));
            //System.out.println(cmd.getArgs()[0]);
            return;
        }
        System.out.println("data processing...");


        // paths
        final String DEFAULT_OUTPUT_DIR = "output";
        final String DATA_DIR = "data";
        final String PARSED_DIR = DATA_DIR + File.separator + "parsed";
        final String SUBREPORTS_DIR = DATA_DIR + File.separator + "subreports";

        String input = cmd.getArgs().length > 0 ? cmd.getArgs()[0] : ".";
        File inputDir = new File(input);
        DataParserFactory dataParserFactory = new DataParserFactoryImpl();
        String outputDirPath = input + File.separator + DEFAULT_OUTPUT_DIR;
        File outputDir = new File(outputDirPath);
        outputDir.mkdirs();
        File metaDir = new File(outputDirPath + File.separator + PARSED_DIR);
        if (metaDir.exists())
            FileUtils.cleanDirectory(metaDir);
        else
            metaDir.mkdirs();

        // parse raw input files
        for (String fileName :
                inputDir.list()) {
            File file = new File(inputDir, fileName);
            if (!file.exists() || file.isDirectory())
                continue;
            String ext = "";
            int dotPos = fileName.indexOf('.');
            if (dotPos >= 0)
                ext = fileName.substring(dotPos + 1);
            else
                continue;
            DataParser dataParser = null;
            try {
                dataParser = dataParserFactory.createParser(ext);
            } catch (Exception e) {
                LOGGER.warning("file '" + fileName + " is ignored.");
                continue;
            }
            InputStream fileIn = new BufferedInputStream(new FileInputStream(file));
            File dir = new File(metaDir, ext);
            dir.mkdirs();
            OutputStream fileOut = new FileOutputStream(new File(dir, fileName + ".csv"));
            dataParser.parse(fileIn, fileOut);
        }

        // generate report
        ReportConfig perfReportConfig = createPerformanceReportConfig();
        for (ChartConfigTemplate chartTemplate:
             perfReportConfig.getConfigTemplate()) {
            ChartConfig<?> chartConfig = chartTemplate.generateChartConfig();
            ChartFactory<?> chartFactory = chartConfig.createChartFactory();
        }

    }
}
