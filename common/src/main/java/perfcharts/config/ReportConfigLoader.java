package perfcharts.config;

import perfcharts.common.AppData;
import perfcharts.common.FieldSelector;
import perfcharts.common.Utilities;
import perfcharts.configtemplate.ChartConfigTemplate;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Loads specified configuration files into memory
 *
 * @author vfree
 */
public class ReportConfigLoader {
    private static final Logger LOGGER = Logger.getLogger(ReportConfigLoader.class.getName());
    /**
     * The name of this package (with a appended point for concatenation)
     */
    private final static String CONFIG_TEMPLATE_PACKAGE = ChartConfigTemplate.class
            .getPackage().getName() + ".";

    /**
     * Load specified configuration file into memory
     *
     * @param fileName a configuration file
     * @return the {@link ReportConfig}
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws XMLStreamException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public ReportConfig load(String fileName) throws IOException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, SecurityException,
            NoSuchFieldException, XMLStreamException, IllegalArgumentException,
            InvocationTargetException {
        File configFile = new File(fileName);
        InputStream in = new FileInputStream(configFile);
        ReportConfig cfg = new ReportConfig();
        cfg.setBasePath(configFile.getParent());
        List<ChartConfigTemplate> list = new ArrayList<ChartConfigTemplate>();
        cfg.setConfigTemplates(list);
        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(in);
        int level = 0; // the level in the XML tree when reading the
        // configuration file
        FieldSelector labelField = null;
        ChartConfigTemplate configTemplate = null;

        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamReader.START_ELEMENT:
                        switch (++level) {
                            case 1: // root element
                                // load jars specified by 'classPath' attribute
                                String classPathAttr = reader.getAttributeValue(null,
                                        "classPath");
                                if (classPathAttr != null && !classPathAttr.isEmpty()) {
                                    String[] classPathStrings = classPathAttr
                                            .split(":");
                                    URL[] urls = new URL[classPathStrings.length];
                                    for (int i = 0; i < classPathStrings.length; i++) {
                                        if (!classPathStrings[i].startsWith("/"))
                                            classPathStrings[i] = AppData.getInstance()
                                                    .getCgt_lib()
                                                    + File.separator
                                                    + classPathStrings[i];
                                        if (!classPathStrings[i].startsWith("file:"))
                                            classPathStrings[i] = "file:"
                                                    + classPathStrings[i];
                                        urls[i] = new URL(classPathStrings[i]);
                                        AppData.getInstance()
                                                .getLogger()
                                                .info("add config class location '"
                                                        + urls[i].toString() + "'");
                                    }
                                    Thread.currentThread().setContextClassLoader(
                                            new URLClassLoader(urls, Thread
                                                    .currentThread()
                                                    .getContextClassLoader()));
                                }
                                // load other attributes
                                cfg.setVersion(reader
                                        .getAttributeValue(null, "version"));
                                cfg.setTitle(reader.getAttributeValue(null, "title"));
                                cfg.setAuthor(reader.getAttributeValue(null, "author"));
                                cfg.setInputFile(reader.getAttributeValue(null,
                                        "inputFile"));
                                cfg.setOutputFile(reader.getAttributeValue(null,
                                        "outputFile"));
                                String labelFieldAttr = reader.getAttributeValue(null,
                                        "labelField");
//                                cfg.setLabelField(labelField = new IndexFieldSelector(
//                                        labelFieldAttr == null
//                                                || labelFieldAttr.isEmpty() ? 0
//                                                : Integer.parseInt(labelFieldAttr)));
                                break;
                            case 2: // the second level
                                if (reader.getLocalName().equals("chartConfigTemplate")) {
                                    String className = reader.getAttributeValue(null,
                                            "class");
                                    if (!className.startsWith("/"))
                                        className = CONFIG_TEMPLATE_PACKAGE + className;
                                    else
                                        className = className.substring(1);

                                    ClassLoader loader = Thread.currentThread()
                                            .getContextClassLoader();
                                    Class<? extends ChartConfigTemplate> clazz = loader
                                            .loadClass(className).asSubclass(
                                                    ChartConfigTemplate.class);
                                    configTemplate = clazz.newInstance();
                                    configTemplate.setLabelField(labelField);
                                    list.add(configTemplate);
                                }
                                break;
                            case 3: // the third level are properties to be set for
                                // chartConfigTemplate
                                String fieldName = reader.getLocalName();
                                String valueType = reader.getAttributeValue(null,
                                        "type");
                                Object value = valueType == null ? Utilities
                                        .parseString(reader.getElementText())
                                        : Utilities.parseString(
                                        reader.getElementText(), valueType);
                                --level;
                                setField(configTemplate, fieldName, value);
                                break;
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        --level;
                        break;
                }
            }
        } finally {
            if (in != null)
                in.close();
        }

        return cfg;
    }

    /**
     * call setXXX method to assign a new property value to an object
     *
     * @param obj       an object
     * @param fieldName the name of property
     * @param value     the new value
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private static void setField(Object obj, String fieldName, Object value)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        String methodName = "set" + Character.toUpperCase(fieldName.charAt(0))
                + fieldName.substring(1);
        // TODO: need optimization
        for (Method m : obj.getClass().getMethods()) {
            try {
                if (m.getName().equals(methodName)) {
                    if (m.getParameterTypes().length == 0)
                        continue;
                    Class<?> paramType = m.getParameterTypes()[0];
                    if (paramType == int.class) {
                        if (value instanceof Number) {
                            value = ((Number) value).intValue();
                        } else if (value instanceof String) {
                            value = Integer.parseInt(value.toString());
                        }
                    } else if (paramType == Long.class || paramType == long.class) {
                        if (value instanceof Number) {
                            value = paramType
                                    .cast(((Number) value).longValue());
                        } else if (value instanceof String) {
                            value = Long.parseLong(value.toString());
                        }
                    } else if (paramType == String.class) {
                        value = value.toString();
                    }
                    m.invoke(obj, value);
                    break;
                }
            } catch (Exception ex) {
                LOGGER.warning("ignore config parameter: " + fieldName + " -> " + value);
            }
        }
    }

}
