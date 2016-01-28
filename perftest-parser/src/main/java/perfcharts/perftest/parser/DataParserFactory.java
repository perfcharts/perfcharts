package perfcharts.perftest.parser;

import java.io.IOException;

/**
 * Created by vfreex on 1/20/16.
 */
public interface DataParserFactory {
    DataParser createParser(String inputType) throws IOException, InterruptedException;
}
