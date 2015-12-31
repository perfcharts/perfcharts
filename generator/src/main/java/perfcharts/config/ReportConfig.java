package perfcharts.config;

import java.util.List;

import perfcharts.common.FieldSelector;
import perfcharts.configtemplate.ChartConfigTemplate;

/**
 * Represents a report configuration. This object is typically loaded by
 * {@link ReportConfigLoader} from configuration files.
 * 
 * @author Rayson Zhu
 *
 */
public class ReportConfig {

	private String basePath;
	/**
	 * The version of configuration file.
	 */
	private String version;
	/**
	 * The title of this report
	 */
	private String title;
	/**
	 * The author of this report
	 */
	private String author;
	/**
	 * The label field of data table. Usually it is the first column.
	 */
	private FieldSelector labelField;
	/**
	 * The path of input data table (CSV) file. The data table file is usually
	 * generated by parsers. If the field is null or empty, the generator will
	 * read data table from standard input stream.
	 */
	private String inputFile;
	/**
	 * The path of output file (in JSON format). Typically a web page loads this
	 * file to display the generated report. If the field is null or empty, the
	 * generator will write the result to standard output stream.
	 */
	private String outputFile;
	/**
	 * The set of {@link ChartConfigTemplate}s. Every
	 * {@link ChartConfigTemplate} defines a chart of this report.
	 */
	private List<ChartConfigTemplate> configTemplate;

	/**
	 * Get the version of configuration file.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the version of configuration file.
	 * 
	 * @param version
	 *            the version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * Get the title of this report.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of this report.
	 * 
	 * @param title
	 *            the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the author of this report.
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Set the author of this report.
	 * 
	 * @param author
	 *            the author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Get the label field of data table.
	 * 
	 * @return the label field
	 */
	public FieldSelector getLabelField() {
		return labelField;
	}

	/**
	 * Set the label field of data table. Usually it is the first column.
	 * 
	 * @param labelField
	 *            the label field
	 */
	public void setLabelField(FieldSelector labelField) {
		this.labelField = labelField;
	}

	/**
	 * Get the path of input data table (CSV) file. The data table file is
	 * usually generated by parsers. If the field is null or empty, the
	 * generator will read data table from standard input stream.
	 * 
	 * @return the path of input data table (CSV) file
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * Set the path of input data table (CSV) file. The data table file is
	 * usually generated by parsers. If the field is null or empty, the
	 * generator will read data table from standard input stream.
	 * 
	 * @param inputFile
	 *            the path of input data table (CSV) file
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * Get the path of output file (in JSON format). Typically a web page loads
	 * this file to display the generated report. If the field is null or empty,
	 * the generator will write the result to standard output stream.
	 * 
	 * @return the path of output file (in JSON format)
	 */
	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * Set the path of output file (in JSON format). Typically a web page loads
	 * this file to display the generated report. If the field is null or empty,
	 * the generator will write the result to standard output stream.
	 * 
	 * @param outputFile
	 *            the path of output file (in JSON format)
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * Get the set of {@link ChartConfigTemplate}s. Every
	 * {@link ChartConfigTemplate} defines a chart of this report.
	 * 
	 * @return the set of {@link ChartConfigTemplate}s
	 */
	public List<ChartConfigTemplate> getConfigTemplate() {
		return configTemplate;
	}

	/**
	 * Set the set of {@link ChartConfigTemplate}s. Every
	 * {@link ChartConfigTemplate} defines a chart of this report.
	 * 
	 * @param configTemplate
	 *            the set of {@link ChartConfigTemplate}s
	 */
	public void setConfigTemplate(List<ChartConfigTemplate> configTemplate) {
		this.configTemplate = configTemplate;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}