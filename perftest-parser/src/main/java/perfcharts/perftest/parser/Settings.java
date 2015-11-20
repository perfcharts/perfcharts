package perfcharts.perftest.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import perfcharts.common.AppData;

/**
 * Represents the app settings
 * 
 * @author Rayson Zhu
 *
 */
public class Settings {
	/**
	 * the unique instance
	 */
	private static Settings instance = new Settings();
	/**
	 * start time for filtering
	 */
	private Date startTime;
	/**
	 * end time for filtering
	 */
	private Date endTime;
	/**
	 * time format for start time and end time
	 */
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"y-M-d h:m:s", Locale.ENGLISH);

	/**
	 * private constructor
	 */
	private Settings() {
		// get start time and end time from system properties
		Logger logger = AppData.getInstance().getLogger();
		String startTimeProp = System.getProperty("startTime");
		if (startTimeProp != null && !startTimeProp.isEmpty()) {
			try {
				startTime = simpleDateFormat.parse(startTimeProp);
				logger.info("specify start time: " + startTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		String endTimeProp = System.getProperty("endTime");
		if (endTimeProp != null && !endTimeProp.isEmpty()) {
			try {
				endTime = simpleDateFormat.parse(endTimeProp);
				logger.info("specify end time: " + endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get start time for filtering
	 * 
	 * @return time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Set start time for filtering
	 * 
	 * @param startTime
	 *            time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get end time for filtering
	 * 
	 * @return time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Set end time for filtering
	 * 
	 * @param endTime
	 *            time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Get the unique instance
	 * 
	 * @return
	 */
	public static Settings getInstance() {
		return instance;
	}

}
