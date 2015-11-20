package perfcharts.tool.zabbix.downloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

public class ZabbixDownloader {
    private final static Logger LOGGER = Logger
            .getLogger(ZabbixDownloader.class.getName());

    private int sequenceId = 0;
    private final static String[] defaultItemKeys = {"system.cpu.num",
            "system.cpu.load", "system.cpu.util", "vm.memory.size",
            "system.swap.in", "system.swap.out", "net.if.in", "net.if.out",
            "vfs.dev.read", "vfs.dev.write"};
    private String[] itemKeys;

    private String apiUrl;
    // private String ca;
    private String user;
    private String password;
    private String[] hosts;
    private Date startTime;
    private Date endTime;
    private String outputDirectory;

    public ZabbixDownloader(String apiUrl, String user, String password,
                            String[] hosts, Date startTime, Date endTime, String outputDirectory) {
        this.apiUrl = apiUrl;
        this.user = user;
        this.password = password;
        this.hosts = hosts;
        this.startTime = startTime;
        this.endTime = endTime;
        this.outputDirectory = outputDirectory;
    }

    public void download() throws MalformedURLException, IOException,
            ZabbixAPIException {
        if (itemKeys == null)
            itemKeys = defaultItemKeys;
        final long startTimeValue = startTime != null ? startTime.getTime() / 1000L
                : 0L;
        final long endTimeValue = endTime != null ? endTime.getTime() / 1000L
                : 0L;
        // final BufferedWriter writer = new BufferedWriter(
        // new OutputStreamWriter(out));
        // final CSVPrinter csvPrinter = new CSVPrinter(writer,
        // CSVFormat.DEFAULT);

        // Check API version
        LOGGER.info("Checking API version of target Zabbix server...");
        JSONObject retObj = callRPC(apiUrl, "apiinfo.version", null, null,
                ++sequenceId);
        throwIfFailed(retObj);
        String apiVersion = retObj.getString("result");
        if (!"2.2.8".equals(apiVersion))
            LOGGER.warning("Zabbix API " + apiVersion + " has not been tested.");
        else
            LOGGER.info("API version supported.");

        // Authenticate
        LOGGER.info("Authenticating...");
        retObj = callRPC(apiUrl, "user.authenticate",
                new JSONObject().put("user", user).put("password", password),
                null, ++sequenceId);
        throwIfFailed(retObj);
        final String auth = retObj.getString("result");

        LOGGER.info("Fetching information of monitored hosts...");
        // get host IDs
        retObj = callRPC(
                apiUrl,
                "host.get",
                new JSONObject().put("filter",
                        new JSONObject().put("host", new JSONArray(hosts)))
                        .put("output", "extend"), auth, ++sequenceId);
        throwIfFailed(retObj);
        JSONArray arr = retObj.getJSONArray("result");
        // final Map<Integer, CSVPrinter> hostID2CSVPrinter = new
        // HashMap<Integer, CSVPrinter>(
        // arr.length());
        final Map<Integer, String> hostID2Name = new HashMap<Integer, String>(
                arr.length());
        final Map<Integer, ConcurrentLinkedQueue<String[]>> hostID2Records = new HashMap<Integer, ConcurrentLinkedQueue<String[]>>(
                arr.length());
        for (int i = 0; i < arr.length(); i++) {
            JSONObject host = arr.getJSONObject(i);
            int hostId = host.getInt("hostid");
            String hostName = host.getString("host");
            LOGGER.info(hostId + " -> " + hostName);
            // csvPrinter.printRecord("HOST", hostId, hostName);
            hostID2Name.put(hostId, hostName);
            hostID2Records.put(hostId, new ConcurrentLinkedQueue<String[]>());
        }
        final JSONArray hostIds = new JSONArray(hostID2Records.keySet()
                .toArray());

        final ConcurrentLinkedQueue<Integer> itemsInQueue = new ConcurrentLinkedQueue<Integer>();
        // search for monitored items (concurrently)
        LOGGER.info("Searching for monitored items...");
        final ConcurrentHashMap<Integer, Integer> itemID2HostID = new ConcurrentHashMap<Integer, Integer>();
        final ConcurrentHashMap<Integer, Integer> itemID2ValueType = new ConcurrentHashMap<Integer, Integer>();

        int maxThreads = (int) (Runtime.getRuntime().availableProcessors() * 2);
        LOGGER.info("Max working threads: " + maxThreads);
        Thread[] threads = new Thread[maxThreads];
        final AtomicInteger remainedItemKeys = new AtomicInteger(
                itemKeys.length);
        for (int t = 0; t < threads.length; t++) {
            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int index;
                    while ((index = remainedItemKeys.decrementAndGet()) >= 0) {
                        String itemKey = itemKeys[index];
                        try {
                            JSONObject retObj = callRPC(
                                    apiUrl,
                                    "item.get",
                                    new JSONObject()
                                            .put("output", "extend")
                                            .put("hostids", hostIds)
                                            .put("search",
                                                    new JSONObject().put(
                                                            "key_", itemKey)),
                                    auth, ++sequenceId);
                            throwIfFailed(retObj);
                            JSONArray arr = retObj.getJSONArray("result");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = arr.getJSONObject(i);
                                int hostId = item.getInt("hostid");
                                int itemId = item.getInt("itemid");
                                String itemKey_ = item.getString("key_");
                                String itemName = item.getString("name");
                                int valueType = item.getInt("value_type");
                                if (valueType >= 0 && valueType < 5) {
                                    ConcurrentLinkedQueue<String[]> records = hostID2Records
                                            .get(hostId);
                                    if (records != null) {
                                        itemID2HostID.put(itemId, hostId);
                                        itemID2ValueType.put(itemId, valueType);
                                        records.add(new String[]{"ITEM",
                                                Integer.toString(itemId),
                                                itemKey_,
                                                Integer.toString(valueType),
                                                itemName,
                                                Integer.toString(hostId)});
                                        itemsInQueue.add(itemId);
                                    }
                                }
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ZabbixAPIFailureException e) {
                            e.printStackTrace();
                        } catch (ZabbixAPIException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
        for (int t = 0; t < threads.length; t++) {
            threads[t].start();
        }
        for (int t = 0; t < threads.length; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // get history (concurrently)
        LOGGER.info("Fetching data...");
        threads = new Thread[maxThreads];
        final int totalTasks = itemsInQueue.size();
        final AtomicInteger finishedTasks = new AtomicInteger(0);
        for (int t = 0; t < threads.length; t++) {
            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer itemID;
                    while ((itemID = itemsInQueue.poll()) != null) {
                        LOGGER.info("Downloading data (" + finishedTasks.incrementAndGet() + " of " + totalTasks + ")...");
                        try {
                            int valueType = itemID2ValueType.get(itemID);
                            JSONObject param = new JSONObject()
                                    .put("history", valueType)
                                    .put("output", "extend")
                                    .put("itemids", itemID);
                            if (startTimeValue > 0)
                                param.put("time_from", startTimeValue);
                            if (endTimeValue > 0)
                                param.put("time_till", endTimeValue);
                            JSONObject retObj = callRPC(apiUrl, "history.get",
                                    param, auth, ++sequenceId);
                            throwIfFailed(retObj);
                            JSONArray hr = retObj.getJSONArray("result");
                            for (int j = 0; j < hr.length(); j++) {
                                JSONObject obj = hr.getJSONObject(j);
                                long ms = obj.getLong("clock") * 1000L
                                        + obj.getLong("ns") / 1000000;
                                int itemId = obj.getInt("itemid");
                                Object value = obj.get("value");
                                Integer hostId = itemID2HostID.get(itemId);
                                if (hostId != null) {
                                    ConcurrentLinkedQueue<String[]> records = hostID2Records
                                            .get(hostId);
                                    if (records != null) {
                                        records.add(new String[]{"HISTORY",
                                                Integer.toString(itemId),
                                                Long.toString(ms),
                                                Integer.toString(valueType),
                                                value.toString()});
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ZabbixAPIFailureException e) {
                            e.printStackTrace();
                        } catch (ZabbixAPIException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        for (int t = 0; t < threads.length; t++) {
            threads[t].start();
        }
        for (int t = 0; t < threads.length; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Writing results...");
        for (int hostId : hostID2Records.keySet()) {
            ConcurrentLinkedQueue<String[]> records = hostID2Records
                    .get(hostId);
            String hostName = hostID2Name.get(hostId);
            String outputFileName = outputDirectory + File.separator + hostName
                    + ".zabbix";
            LOGGER.info("create file '" + outputFileName + "'");
            CSVPrinter printer = new CSVPrinter(
                    new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(outputFileName))),
                    CSVFormat.DEFAULT);
            printer.printRecords(records);
            printer.flush();
            printer.close();
        }
        LOGGER.info("Logging out...");
        callRPC(apiUrl, "user.logout", null, auth, ++sequenceId);
        LOGGER.info("Done.");
    }

    private static void throwIfFailed(JSONObject retObj) throws IOException,
            ZabbixAPIFailureException {
        if (!retObj.has("error"))
            return;
        JSONObject error = retObj.getJSONObject("error");
        if (error == null)
            return;
        int code = error.getInt("code");
        if (code == 0)
            return;
        throw new ZabbixAPIFailureException(code, error.getString("message"),
                error.getString("data"));
    }

    private static JSONObject callRPC(String url, String method,
                                      JSONObject params, String auth, int id)
            throws MalformedURLException, IOException, ZabbixAPIException {
        LOGGER.info("JSON RPC Request begins: " + method);
        HttpURLConnection http = (HttpURLConnection) new URL(url)
                .openConnection();
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setUseCaches(false);
        http.setConnectTimeout(1000 * 60);
        http.setRequestMethod("POST");
        http.setRequestProperty("Connection", "Close");
        http.setRequestProperty("Content-Type", "application/json-rpc");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                http.getOutputStream()));
        JSONWriter jsonWriter = new JSONWriter(writer);
        jsonWriter.object().key("jsonrpc").value("2.0").key("method")
                .value(method).key("id").value(id).key("auth").value(auth)
                .key("params").value(params).endObject();
        writer.flush();
        JSONObject r = new JSONObject(new JSONTokener(new BufferedReader(
                new InputStreamReader(http.getInputStream()))));
        http.disconnect();
        LOGGER.info("JSON RPC Request ends: " + method);
        return r;
    }


    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String[] getItemKeys() {
        return itemKeys;
    }

    public void setItemKeys(String[] itemKeys) {
        this.itemKeys = itemKeys;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static String[] getDefaultitemkeys() {
        return defaultItemKeys;
    }


    public static class ZabbixAPIException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -5315732547492948417L;

        public ZabbixAPIException() {
        }

        public ZabbixAPIException(String message) {
            super(message);
        }
    }

    public static class ZabbixAPIFailureException extends ZabbixAPIException {
        /**
         *
         */
        private static final long serialVersionUID = 1063340990737841665L;
        private final int code;
        private final String data;
        private final String shortMessage;

        public ZabbixAPIFailureException() {
            this.shortMessage = "";
            this.code = 0;
            this.data = "";
        }

        public ZabbixAPIFailureException(int code, String message, String data) {
            super("Zabbix API error " + code + ". " + message + " " + data);
            this.shortMessage = message;
            this.code = code;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public String getData() {
            return data;
        }

        public String getShortMessage() {
            return shortMessage;
        }
    }
}
