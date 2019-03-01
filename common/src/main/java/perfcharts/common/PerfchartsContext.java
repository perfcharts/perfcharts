package perfcharts.common;

import java.io.File;

/**
 * Created by vfreex on 2/1/16.
 */
public class PerfchartsContext {
    private String applicationPath;

    private static PerfchartsContext instance = new PerfchartsContext();

    private PerfchartsContext() {
        try {
            if (System.getenv("PERFCHARTS_HOME") != null) {
                applicationPath = System.getenv("PERFCHARTS_HOME");
            } else {
                applicationPath = new File(PerfchartsContext.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent();
            }
        } catch (Exception ex) {
            applicationPath = "/usr/share/perfcharts";
        }
    }

    public static PerfchartsContext getInstance() {
        return instance;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }
}
