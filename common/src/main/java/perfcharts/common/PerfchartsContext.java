package perfcharts.common;

/**
 * Created by vfreex on 2/1/16.
 */
public class PerfchartsContext {
    private String applicationPath;

    private static PerfchartsContext instance = new PerfchartsContext();

    private PerfchartsContext() {
        if (System.getenv("PERFCHARTS_HOME") != null) {
            applicationPath = System.getenv("PERFCHARTS_HOME");
        } else {
            applicationPath = "/usr/share/perfcharts";
        }
    }

    public static PerfchartsContext getInstance(){
        return instance;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }
}
