package perfcharts.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import perfcharts.common.Utilities;
import perfcharts.model.DataTable;

/**
 * Loads the data table file (in CSV format) to memory
 * 
 * @author Rayson Zhu
 *
 */
public class DataTableLoader {
	/**
	 * Load the data table file (in CSV format) to memory.
	 * 
	 * @param in
	 *            the {@link InputStream} contains the content of data table
	 *            file
	 * @return the data table
	 * @throws IOException
	 */
	public DataTable load(InputStream in) throws IOException {
		List<List<Object>> rows = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
		// don't use csvParser.getRecords() for iteration, may cause huge memory usage and lead to out-of-memory error
		for (CSVRecord csvRecord : csvParser)
			rows.add(getFields(csvRecord));
		csvParser.close();
		/*String line;
		while ((line = reader.readLine()) != null) {
			rows.add(getFields(line));
		}*/
		return new DataTable(rows);
	}
	
	private static List<Object> getFields(CSVRecord record){
		List<Object> result = new ArrayList<Object>(record.size());
		for (String col : record) {
			result.add(Utilities.parseString(col));
		}
		return result;
	}

	private static List<Object> getFields(String line){
		String[] cols = line.split(",");
		List<Object> result = new ArrayList<Object>(cols.length);
		for (String col : cols) {
			if (col.length() >= 2 && col.charAt(0) == '"' && col.charAt(col.length() - 1) == '"'){
				col = col.substring(1, col.length() - 1).replace("\"\"", "\"");
			}
			result.add(Utilities.parseString(col));
		}
		return result;
	}
}
