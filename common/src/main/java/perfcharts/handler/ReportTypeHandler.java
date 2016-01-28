package perfcharts.handler;

import java.io.IOException;
import java.util.List;

/**
 * Created by vfreex on 1/19/16.
 */
public interface ReportTypeHandler {
    void handle(List<String> argList) throws IOException, InterruptedException;
}
