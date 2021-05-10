package com.example.zotrides;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SAXParserPickup extends DefaultHandler{
    List<PickupLocation> myLocations;

    // store temporary values
    private String tempVal;

    // track number of data inconsistencies
    private int numInconsistent = 0;
    public int newCount = 0;

    private PickupLocation tempLoc;

    // used to execute queries
    public static Connection conn = null;
    public static PreparedStatement psInsertRecord = null;

    public SAXParserPickup() { myLocations = new ArrayList<PickupLocation>();}

    public void runExample() {
        parseDocument();
    }

    private void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("locations_final.xml", this);
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData() {
        System.out.println("-----------------");
        System.out.println("Total Missing Tags: " + PickupLocation.getNumMissing());
        System.out.println("Total Inconsistent Data Types: " + numInconsistent);
        System.out.println("Total Duplicate Entries: " + newCount);
        System.out.println("-----------------");
//        System.out.println("No of Locations '" + myLocations.size() + "'.");
//        Iterator<PickupLocation> it = myLocations.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("Location")) {
            tempLoc = new PickupLocation();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("Location")) {
            if (!tempLoc.isInconsistent()) {
//                myLocations.add(tempLoc);
                // TODO: CONVERT TO QUERY
                try {
                    psInsertRecord.setString(1, tempLoc.getAddress());
                    psInsertRecord.setString(2, tempLoc.getPhone());
                    psInsertRecord.setInt(3, tempLoc.getId());
                    psInsertRecord.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if (qName.equalsIgnoreCase("LocID")) {
            if (tempVal.length() > 10) {
                System.out.println("Inconsistent value type. Expected at most 10 digits for <LocId> but value is: '" + tempVal + "'");
                tempLoc.setId(-2);
                ++numInconsistent;
            } else {
                try {
                    tempLoc.setId(Integer.parseInt(tempVal));
                } catch (Exception e) {
                    System.out.println("Inconsistent value type. Expected integer for <LocId> but value is: '" + tempVal + "'");
                    tempLoc.setId(-2);
                    ++numInconsistent;
                }
            }
        } else if (qName.equalsIgnoreCase("Phone")) {
            if(tempVal.length() <= 20)
                tempLoc.setPhone(tempVal);
            else {
                System.out.println("Inconsistent value type. Max string length is 20 for <Phone> but value is: '" + tempVal + "'");
                tempLoc.setPhone(null);
                ++numInconsistent;
            }
        } else if (qName.equalsIgnoreCase("Address")) {
            if(tempVal.length() <= 200)
                tempLoc.setAddress(tempVal);
            else {
                System.out.println("Inconsistent value type. Max string length is 200 for <Address> but value is: '" + tempVal + "'");
                tempLoc.setAddress("");
                ++numInconsistent;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        PickupLocation.resetMissing();

        // Setup database connection & prepared statement
        conn = null;
        psInsertRecord=null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/zotrides";
        String query = "CALL add_pickup_xml(?, ?, ?);";
        int[] iNoRows = null;
        try {
            conn = DriverManager.getConnection(loginUrl,loginUser, loginPasswd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Run SAX Parser
        try {
            conn.setAutoCommit(false);
            psInsertRecord = conn.prepareStatement(query);

            // Run SAX Parser
            SAXParserPickup spe = new SAXParserPickup();
            spe.runExample();

            // Execute batch
//            System.out.println("Starting to Execute");
            iNoRows = psInsertRecord.executeBatch();
            conn.commit();
            System.out.println("Executed: " + iNoRows.length);

            // Retrieve new count
            PreparedStatement counting = conn.prepareStatement("SELECT COUNT(*) as val FROM PickupLocation;");
            ResultSet rs = counting.executeQuery();
            if (rs.next())
                spe.newCount = rs.getInt("val") - 60000;
            rs.close();
            counting.close();

            // Print stats
            spe.printData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cleanup
        try {
            if(psInsertRecord!=null) psInsertRecord.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}