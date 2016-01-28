package perfcharts.model;

import java.util.List;

/**
 * Represents a data table
 * 
 * @author Rayson Zhu
 *
 */
public class DataTable {
	/**
	 * rows
	 */
	private List<List<Object>> rows;

	/**
	 * constructor
	 */
	public DataTable() {

	}

	/**
	 * constructor
	 * 
	 * @param rows
	 *            rows
	 */
	public DataTable(List<List<Object>> rows) {
		this.rows = rows;
	}

	/**
	 * Get rows.
	 * 
	 * @return rows
	 */
	public List<List<Object>> getRows() {
		return rows;
	}

	/**
	 * Set rows
	 * 
	 * @param rows
	 *            new rows
	 */
	public void setRows(List<List<Object>> rows) {
		this.rows = rows;
	}

}
