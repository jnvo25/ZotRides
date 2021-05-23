package edu.uci.ics.zotrides;

public class BackendServerConn {
    private static final String host = "18.217.144.182"; // IP address for localhost
    private static final String port = "8443";
    private static final String domain = "ZotRides";

    public static String getURL() {
        return "https://" + host + ":" + port + "/" + domain;
    }
}