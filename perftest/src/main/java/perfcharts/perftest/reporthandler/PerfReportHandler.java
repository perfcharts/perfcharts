package perfcharts.perftest.reporthandler;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import perfcharts.chart.Report;
import perfcharts.common.PerfchartsContext;
import perfcharts.config.ReportConfig;
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

/**
 * Created by vfreex on 2/4/16.
 */
public abstract class PerfReportHandler implements ReportTypeHandler {
    protected final static Options options;
    private final static Logger LOGGER = Logger.getLogger(PerfBaselineReportHandler.class.getName());

    static {
        options = new Options();
        options.addOption("h", "help", false, "show help message");
        options.addOption("d", "output-dir", true, "write generated files into DIRECTORY");
        options.addOption("o", "output-file", true, "relocate generated report to FILE");
        options.addOption("e", "exclude", true, "specify the pattern for average TPS & RT calculation exclusion, like 'ping\\.html'");
        options.addOption("z", "time-zone", true, "fallback TIME_ZONE, like 'GMT+8'");
    }

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

        if (cmd.hasOption("z")) {
            TimeZone tz = TimeZone.getTimeZone(cmd.getOptionValue("z"));
            TimeZone.setDefault(tz);
        }

        LOGGER.info("Use fallback time zone '" + TimeZone.getDefault().toZoneId() + "'.");

        // paths
        final String DEFAULT_OUTPUT_DIR = "output";
        final String DATA_DIR = "data";
        final String PARSED_DIR = DATA_DIR + File.separator + "parsed";
        final String SUBREPORTS_DIR = DATA_DIR + File.separator + "subreports";

        String input = cmd.getArgs().length > 0 ? cmd.getArgs()[0] : ".";
        File inputDir = new File(input);

        LOGGER.info("Processing files located in '" + inputDir.getAbsolutePath() + "'...");

        DataParserFactory dataParserFactory = new DataParserFactoryImpl();
        String outputDirPath = cmd.getOptionValue("d", input + File.separator + DEFAULT_OUTPUT_DIR);
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
        Map<String, Set<String>> parsedFileMap = new HashMap<>();
        //final String FILENAME_PATTERN = "^([^_]+)(_.*)*\\.\\w+$";
        //Pattern monitoringFileNamePattern = Pattern.compile(FILENAME_PATTERN);
        for (String fileName :
                inputDir.list()) {
            File file = new File(inputDir, fileName);
            if (!file.exists() || file.isDirectory())
                continue;
            String ext = "";
            int dotPosition = fileName.lastIndexOf('.');
            if (dotPosition < 0)
                continue;
            ext = fileName.substring(dotPosition + 1);
            if (ext == null || ext.isEmpty())
                continue;
            PerfReportHandlerRule rule = getReportHandlerRules().get(ext);
            if (rule == null) {
                LOGGER.warning("No rules for file '" + fileName + ".");
                continue;
            }
            Matcher matcher = rule.getInputFileNamePattern().matcher(fileName);
            if (!matcher.matches()) {
                LOGGER.warning("File '" + fileName + " has an invalid name format.");
                continue;
            }
            String subreportName = matcher.replaceAll(rule.getOutputReportNamePattern());

            DataParser dataParser = null;
            try {
                dataParser = dataParserFactory.createParser(ext);
            } catch (Exception e) {
                LOGGER.warning("No parser found for '" + fileName + ".");
                continue;
            }

            InputStream fileIn = new BufferedInputStream(new FileInputStream(file));
            File subreportOutputDir = new File(parsedDir, rule.getCategory());
            subreportOutputDir.mkdirs();
            File parsedFile = new File(subreportOutputDir, subreportName);
            if (rule.getDuplicatedFileAction() == PerfReportHandlerRule.DuplicatedAction.IGNORE && parsedFile.exists()) {
                LOGGER.warning("Ignore duplicated for '" + fileName + ".");
                continue;
            }
            OutputStream parsedFileOut = new FileOutputStream(parsedFile, rule.getDuplicatedFileAction() == PerfReportHandlerRule.DuplicatedAction.APPEND);
            dataParser.parse(fileIn, parsedFileOut);
            Set<String> parsedFiles = parsedFileMap.get(rule.getCategory());
            if (parsedFiles == null) {
                parsedFileMap.put(rule.getCategory(), parsedFiles = new TreeSet<String>());
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
        for (String category : parsedFileMap.keySet()) {
            Set<String> parsedFiles = parsedFileMap.get(category);
            if (parsedFiles == null || parsedFiles.isEmpty())
                continue;
            for (String parsedFilePath : parsedFiles) {
                File parsedFile = new File(parsedFilePath);
                if (!parsedFile.exists()) {
                    LOGGER.warning("Parsed file '" + parsedFile.getAbsolutePath() + "' does not exist.");
                    continue;
                }
                ReportConfig reportConfig = createReportConfigs(category, parsedFile.getName(), null, cmd);
                if (reportConfig == null) {
                    LOGGER.warning("No report configs found for parsed file category '" + category + "'.");
                    continue;
                }
                InputStream parsedFileIn = new FileInputStream(parsedFile);
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
        File monoReportFile = new File(cmd.getOptionValue("o", outputDir.getAbsolutePath() + File.separator + "mono-report.html"));
        FileUtils.copyFile(new File(PerfchartsContext.getInstance().getApplicationPath() + File.separator + getReportTemplateHTMLFileName()), monoReportFile);
        OutputStream monoReportFileOut = new FileOutputStream(monoReportFile, true);
        BufferedWriter monoReportWriter = new BufferedWriter(new OutputStreamWriter(monoReportFileOut));
        monoReportWriter.write("<script type='text/javascript'>\n");
        IOUtils.copy(new FileInputStream(dataJsFile), monoReportWriter);
        monoReportWriter.write("\n</script>\n");
        monoReportWriter.flush();
    }

    /**
     * file extension -&gt; rule
     */
    protected abstract Map<String, PerfReportHandlerRule> getReportHandlerRules();

    protected abstract String getReportTemplateHTMLFileName();

    /**
     * parsed file category -&gt; ReportConfigs
     *
     * @return
     */
    protected abstract ReportConfig createReportConfigs(String category, String reportTitle, String reportSubtitle, CommandLine cmd);
}
