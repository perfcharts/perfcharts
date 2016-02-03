package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import perfcharts.chart.Report;
import perfcharts.common.PerfchartsContext;
import perfcharts.config.ReportConfig;
import perfcharts.configtemplate.*;
import perfcharts.generator.ReportGenerator;
import perfcharts.generator.ReportWritter;
import perfcharts.handler.ReportTypeHandler;
import perfcharts.perftest.parser.DataParser;
import perfcharts.perftest.parser.DataParserFactory;
import perfcharts.perftest.parser.DataParserFactoryImpl;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        config.setConfigTemplates(configTemplates);
        //config.setLabelField(new IndexFieldSelector<>(0));
        return config;
    }

    private static ReportConfig createResourceMonitoringReportConfig(String title) {
        ReportConfig config = new ReportConfig();
        config.setTitle(title);
        //config.setBasePath();
        List<ChartConfigTemplate> configTemplates = new ArrayList<>();
        configTemplates.add(new CPULoadChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONCPUUtilizationChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONMemoryUtilizationChartTemplate().setSubtitle(title));
        configTemplates.add(new VMSwapInOutChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONNetworkThroughputChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONDiskIOChartTemplate().setSubtitle(title));
        configTemplates.add(new NMONDiskBusyChartTemplate().setSubtitle(title));
        config.setConfigTemplates(configTemplates);
        //config.setLabelField(new IndexFieldSelector<>(0));
        return config;
    }

    //private static class

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

        File dataDir = new File(outputDir, DATA_DIR);
        if (dataDir.exists())
            FileUtils.cleanDirectory(dataDir);
        else
            dataDir.mkdirs();

        File parsedDir = new File(outputDirPath + File.separator + PARSED_DIR);
        parsedDir.mkdirs();

        // parse raw input files
        Map<String, List<String>> parsedFileMap = new HashMap<>();
        final String FILENAME_PATTERN = "^([^_]+)(_.*)*\\.\\w+$";
        Pattern monitoringFileNamePattern = Pattern.compile(FILENAME_PATTERN);
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

            String category = null;
            if ("jtl".equalsIgnoreCase(ext)) {
                // parse all .jtl files to a single file
                fileName = "performance";
                category = "jmeter";
            } else if ("nmon".equalsIgnoreCase(ext) || "load".equalsIgnoreCase(ext)) {
                Matcher m = monitoringFileNamePattern.matcher(file.getName());
                if (m.matches())
                    fileName = m.group(1);
                category = "nmon";
            }

            File dir = new File(parsedDir, category);
            dir.mkdirs();
            File parsedFile = new File(dir, fileName);
            OutputStream parsedFileOut = new FileOutputStream(parsedFile, true);
            dataParser.parse(fileIn, parsedFileOut);
            List<String> parsedFiles = parsedFileMap.get(ext);
            if (parsedFiles == null) {
                parsedFileMap.put(ext, parsedFiles = new LinkedList<>());
            }
            parsedFiles.add(parsedFile.getAbsolutePath());
        }

        // create subreports dir
        String subreportsDirPath = outputDirPath + File.separator + SUBREPORTS_DIR;
        File subreportsDir = new File(subreportsDirPath);
        if (subreportsDir.exists())
            FileUtils.cleanDirectory(subreportsDir);
        else
            subreportsDir.mkdirs();

        List<File> subreportJsonFiles = new ArrayList<>();

        // generate performance report
        List<String> parsedJtlFiles = parsedFileMap.get("jtl");
        if (parsedJtlFiles != null && !parsedJtlFiles.isEmpty()) {
            InputStream parsedFileIn = new FileInputStream(parsedJtlFiles.get(0));
            ReportConfig perfReportConfig = createPerformanceReportConfig();
            ReportGenerator generator = new ReportGenerator(perfReportConfig);
            Report report = generator.generate(parsedFileIn);

            // write to output stream
            File performanceSubreportFile = new File(subreportsDir, "Performance.json");
            ReportWritter reportWritter = new ReportWritter();
            reportWritter.write(report, new FileOutputStream(performanceSubreportFile));
            subreportJsonFiles.add(performanceSubreportFile);
        }

        List<String> parsedNMONFiles = parsedFileMap.get("nmon");
        if (parsedNMONFiles != null && !parsedNMONFiles.isEmpty()) {
            for (String parsedNMONFilePath : parsedNMONFiles) {
                File parsedFile = new File(parsedNMONFilePath);
                InputStream parsedFileIn = new FileInputStream(parsedFile);

                ReportConfig reportConfig = createResourceMonitoringReportConfig(parsedFile.getName());
                ReportGenerator generator = new ReportGenerator(reportConfig);
                Report report = generator.generate(parsedFileIn);
                // write to output stream
                File subreportFile = new File(subreportsDir, parsedFile.getName() + ".json");
                ReportWritter reportWritter = new ReportWritter();
                reportWritter.write(report, new FileOutputStream(subreportFile));
                subreportJsonFiles.add(subreportFile);
            }
        }

        // generate data.js file
        File dataJsFile = new File(dataDir, "data.js");
        OutputStream dataJsOut = new FileOutputStream(dataJsFile);
        Writer dataJsWriter = new BufferedWriter(new OutputStreamWriter(dataJsOut));
        dataJsWriter.append("(function(){var d=ChartGeneration.data,j;\n");
        for (File subreportJsonFile : subreportJsonFiles) {
            dataJsWriter.append("j=");
            IOUtils.copy(new FileInputStream(subreportJsonFile), dataJsWriter);
            dataJsWriter.append(";d.push(j);\n");
        }
        dataJsWriter.append("})();\n");
        dataJsWriter.flush();

        // generate mono-report file
        File monoReportFile = new File(outputDir, "mono-report.html");
        FileUtils.copyFile(new File(PerfchartsContext.getInstance().getApplicationPath() + File.separator + reportTemplateHTML), monoReportFile);
        OutputStream monoReportFileOut = new FileOutputStream(monoReportFile, true);
        BufferedWriter monoReportWriter = new BufferedWriter(new OutputStreamWriter(monoReportFileOut));
        monoReportWriter.write("<script type='text/javascript'>\n");
        IOUtils.copy(new FileInputStream(dataJsFile), monoReportWriter);
        monoReportWriter.write("\n</script>\n");
        monoReportWriter.flush();
    }
}
