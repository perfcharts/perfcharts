package perfcharts.perftest.parser;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by vfreex on 1/20/16.
 */
public class DataParserFactoryImpl implements DataParserFactory {

    private static final Map<String, Class<? extends DataParser>> parserClasses;

    static {
        parserClasses = new HashMap<>();
        parserClasses.put("jtl", JmeterParser.class);
        parserClasses.put("nmon", NMONParser.class);
        parserClasses.put("load", CPULoadParser.class);
        parserClasses.put("perfcmp", PerformanceComparisonParser.class);
        parserClasses.put("perftrend", PerfTrendParser.class);
    }

    private Map<String, DataParser> parsers = new HashMap<>();

    @Override
    public DataParser createParser(String inputType) throws IOException, InterruptedException {
        DataParser parser = null;
        if (parsers.containsKey(inputType))
            parser = parsers.get(inputType);
        if (parser == null) {
            if (!parserClasses.containsKey(inputType))
                throw new InvalidParameterException("Cannot find a parser to parse '" + inputType + "' files");
            Class<? extends DataParser> parserClass = parserClasses.get(inputType);
            try {
                parser = parserClass.getConstructor(new Class<?>[0]).newInstance();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return parser;
    }
}
