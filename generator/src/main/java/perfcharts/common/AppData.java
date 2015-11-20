package perfcharts.common;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a global point for accessing universal application data
 * 
 * @author Rayson Zhu
 *
 */
public class AppData {
	/**
	 * The $CGT_HOME environment variable. $CGT_HOME is the absolute path of
	 * installation directory.
	 */
	private String cgt_home;
	/**
	 * The $CGT_LIB environment variable. $CGT_LIB is the absolute path of
	 * directory for placing library files.
	 */
	private String cgt_lib;

	/**
	 * The logger for keeping logs.
	 */
	private Logger logger;

	/**
	 * The unique instance of this class.
	 */
	private static AppData instance = new AppData();

	private AppData() {
		// read $CGT_HOME, $CGT_LIB, and $CGT_LOG environment variables
		cgt_home = System.getenv("CGT_HOME");
		if (cgt_home == null) {
			try {
				cgt_home = new File(AppData.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI()).getParentFile()
						.getParentFile().getAbsolutePath();
			} catch (URISyntaxException e) {
				cgt_home = "/";
				e.printStackTrace();
			}

		}
		cgt_lib = System.getenv("CGT_LIB");
		if (cgt_lib == null)
			cgt_lib = cgt_home + File.separator + "lib";
		logger = Logger.getLogger("Generator");
		// config specified log level from system property "logLevel"
		String logLevel = System.getProperty("logLevel");
		if (logLevel != null && !logLevel.isEmpty()) {
			logger.setLevel(Level.parse(logLevel));
		} else {
			// default to INFO
			logger.setLevel(Level.INFO);
		}
		logger.config("CGT_HOME=" + cgt_home);
		logger.config("CGT_LIB=" + cgt_lib);
	}

	/**
	 * Get the unique instance of AppData
	 * 
	 * @return an AppData
	 */
	public static AppData getInstance() {
		return instance;
	}

	/**
	 * Get the value of $CGT_HOME environment variable
	 * 
	 * @return the value
	 */
	public String getCgt_home() {
		return cgt_home;
	}

	/**
	 * Set the value of $CGT_HOME environment variable
	 * 
	 * @param cgt_home
	 *            the value
	 */
	public void setCgt_home(String cgt_home) {
		this.cgt_home = cgt_home;
	}

	/**
	 * Get the value of $CGT_LIB environment variable
	 * 
	 * @return the value
	 */
	public String getCgt_lib() {
		return cgt_lib;
	}

	/**
	 * Set the value of $CGT_LIB environment variable
	 * 
	 * @param cgt_lib
	 *            the value
	 */
	public void setCgt_lib(String cgt_lib) {
		this.cgt_lib = cgt_lib;
	}

	/**
	 * Get the logger for this application
	 * 
	 * @return a logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Set the logger for this application
	 * 
	 * @param logger
	 *            a logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
