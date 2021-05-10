package com.example.zotrides;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SAXParserMapping extends DefaultHandler{
    List<Mapping> mappings;

    private String tempVal; // for all other values
    private String tempId; // for carID attribute string
    private int tempIdInt; // for carID attribute integer form

    private boolean skipRestOfCar; // for skipping rest of broken / invalid tags

    private PrintWriter out;

    // track inconsistencies
    private int numInconsistent = 0;
    private int numMissingAttr = 0;
    public int newCount = 0;

    private Mapping tempMap;

    // used to execute queries
    public static Connection conn = null;
    public static PreparedStatement psInsertRecord = null;

    public SAXParserMapping() { mappings = new ArrayList<Mapping>();}

    public void runExample() {
        try {
            out = new PrintWriter("data.txt");
            parseDocument();
            out.close();
        } catch (FileNotFoundException f) {
            System.out.println("Error: file not found :(");
        }
    }

    private void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("mappings_final.xml", this);
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData() {
        System.out.println("Total Missing Tags: " + Mapping.getNumMissing());
        System.out.println("Total Missing Attributes: " + numMissingAttr);
        System.out.println("Total Inconsistent Data Types: " + numInconsistent);
        System.out.println("-----------------");
//        System.out.println("No of Locations '" + mappings.size() + "'.");
//        Iterator<Mapping> it = mappings.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("CarE")) {
            tempId = attributes.getValue("id");
            if (skipRestOfCar = (tempId == null)) {
                System.out.println("Missing required car ID attribute 'id'");
                ++numMissingAttr;
            } else if(skipRestOfCar = (tempId.length() > 10)) {
                System.out.println("Inconsistent value type. Expected at most 10 digits for car's 'id' attribute but value is: '" + tempVal + "'");
                ++numInconsistent;
            } else {
                try {
                    tempIdInt = Integer.parseInt(tempId);
                } catch (Exception e) {
                    System.out.println("Inconsistent value type. Expected integer for car's 'id' attribute but value is: '" + tempVal + "'");
                    skipRestOfCar = true;
                    ++numInconsistent;
                }
            }
        }

        if (qName.equalsIgnoreCase("LocationID")) {
            tempMap = new Mapping();
            tempMap.setCarID(tempIdInt);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(skipRestOfCar) {
            if(qName.equalsIgnoreCase("CarE")) {
                skipRestOfCar = false;
            }
            return;
        }

        if (qName.equalsIgnoreCase("LocationID")) {
            if(tempVal.length() > 10) {
                System.out.println("Inconsistent value type. Expected at most 10 digits for <LocationID> but value is: '" + tempVal + "'");
                tempMap.setLocationID(-2);
                ++numInconsistent;
            } else {
                try {
                    tempMap.setLocationID(Integer.parseInt(tempVal));
                } catch (Exception e) {
                    System.out.println("Inconsistent value type. Expected integer for <LocationID> but value is: '" + tempVal + "'");
                    tempMap.setLocationID(-2);
                    ++numInconsistent;
                }
            }

            if(!tempMap.isInconsistent()) {
//                mappings.add(tempMap);
                out.println(tempMap.getCarID() + "," + tempMap.getLocationID());
                try {
                    psInsertRecord.setInt(1, tempMap.getLocationID());
                    psInsertRecord.setInt(2, tempMap.getCarID());
                    psInsertRecord.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //TODO : CONVERT TO QUERIES
        }
    }

    public static void main(String[] args) throws Exception{
        Mapping.resetMissing();

        // Setup database connection & prepared statement
        conn = null;
        psInsertRecord=null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/zotrides";
        String query = "CALL find_mapping_xml_errors(?, ?);";
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

            // Run parser
            SAXParserMapping spe = new SAXParserMapping();
            spe.runExample();

            // Execute batch
//            System.out.println("Starting to Execute");
            iNoRows = psInsertRecord.executeBatch();
            conn.commit();
            System.out.println("-----------------");
            System.out.println("Executed: " + iNoRows.length);

            // Retrieve new count
            PreparedStatement counting = conn.prepareStatement("SELECT COUNT(*) as val FROM pickup_car_from;");
            ResultSet rs = counting.executeQuery();
            if (rs.next())
                spe.newCount = rs.getInt("val") - 86221;
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