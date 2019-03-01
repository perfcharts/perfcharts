package perfcharts.perftest.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A parser is the object that converts raw data to data tables (in CSV format).
 *
 * @author Rayson Zhu
 */
public interface DataParser {
    /**
     * converts raw data to data tables (in CSV format)
     *
     * @param in  the {@link InputStream} containing raw data
     * @param out the {@link OutputStream} to store output
     *
     */
    public void parse(InputStream in, OutputStream out) throws IOException, InterruptedException;
}
