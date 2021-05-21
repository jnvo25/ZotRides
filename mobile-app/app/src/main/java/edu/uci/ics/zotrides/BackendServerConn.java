package edu.uci.ics.zotrides;

public class BackendServerConn {
    private static final String host = "10.0.2.2"; // IP address for localhost
    private static final String port = "8443";
    private static final String domain = "ZotRides";

    public static String getURL() {
        return "https://" + host + ":" + port + "/" + domain;
    }
}