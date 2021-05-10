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

/* The DefaultHandler that SAXParserExample inherits from
* follows the slide architecture */
public class SAXParserCar extends DefaultHandler {

    List<Car> myCars;

    // store temporary values
    private String tempVal;
    private String tempMake; // for storing make attribute

    // for skipping when make tag is corrupted / missing values
    private boolean skipRestOfMake;

    // to track number data inconsistencies & missing attributes
    private int numInconsistent = 0;
    private int numMissingAttr = 0;
    public int newCount = 0;

    // to maintain context
    private Car tempCar;

    // used to execute queries
    public static Connection conn = null;
    public static PreparedStatement psInsertRecord = null;

    public SAXParserCar() {
        myCars = new ArrayList<Car>();
    }

    /* parses document then prints the data */
    public void runExample() {
        parseDocument();
    }

    private void parseDocument() {
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("cars_final.xml", this);
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {
        System.out.println("-----------------");
        System.out.println("Total Missing Tags: " + Car.getNumMissing());
        System.out.println("Total Missing Attributes: " + numMissingAttr);
        System.out.println("Total Inconsistent Data Types: " + numInconsistent);
        System.out.println("Total Duplicate Entries: " + newCount); //TODO : UPDATE FROM DATA TABLE
        System.out.println("-----------------");
//        System.out.println("No of Cars '" + myCars.size() + "'.");
//        Iterator<Car> it = myCars.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//        }
    }

    //Event Handlers
    /* the event handlers are implemented by us, the developers */

    /* called upon opening tag of an element
    *  Parameters (context of info for start element):
    *       - namespace URI
    *       - local name
    *       - qualified (prefixed) name
    *       - attributes */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("Makecars")) {
            //record the new type
            tempMake = attributes.getValue("make");
            if (skipRestOfMake = (tempMake == null)) {
                System.out.println("Missing required attribute 'make'");
                ++numMissingAttr;
            } else if(skipRestOfMake = (tempMake.length() > 100)) {
                System.out.println("Inconsistent value type. Max string length is 100 for 'make' but value is: '" + tempMake + "'");
                ++numInconsistent;
            }
        } else if (qName.equalsIgnoreCase("vehicle")) {
            //create a new instance of employee
            tempCar = new Car();
            tempCar.setMake(tempMake);
        }
    }

    /* called when we reach actual values of an element ?? */
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    /* called upon closing tag of any element */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        boolean encounteredError = false;

        // Skip the rest of vehicles if make is not valid
        if (skipRestOfMake) {
            if (qName.equalsIgnoreCase("Makecars")) {
                skipRestOfMake = false;
            }
            return;
        }

        if (qName.equalsIgnoreCase("vehicle")) {
            //add it to the list
            if (!tempCar.isInconsistent()) {
//                myCars.add(tempCar);
                try {
                    psInsertRecord.setString(1, tempCar.getMake());
                    psInsertRecord.setString(2, tempCar.getModel());
                    psInsertRecord.setInt(3, tempCar.getYear());
                    psInsertRecord.setString(4, tempCar.getCategory());
                    psInsertRecord.setInt(5, tempCar.getId());
                    psInsertRecord.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //TODO : CONVERT THIS INTO QUERY & ADD TO BATCH

            /*
            * 1) convert omitted / unrecognizable fields to NULL
            * 2) check for null values --> don't add if any fields are null
            * 3) check for type mismatches & invalid lengths
            * 3) run stored procedure
            *       - checks for duplicates --> returns error message
            *       - updates Category / category_of_car if necessary
            * */

        } else if (qName.equalsIgnoreCase("id")) {
            if (tempVal.length() > 10) {
                System.out.println("Inconsistent value type. Expected at most 10 digits for <id> but value is: '" + tempVal + "'");
                tempCar.setId(-2);
                ++numInconsistent;
            } else {
                try {
                    tempCar.setId(Integer.parseInt(tempVal));
                } catch (Exception e) {
                    System.out.println("Inconsistent value type. Expected integer for <id> but value is: '" + tempVal + "'");
                    tempCar.setId(-2);
                    ++numInconsistent;
                }
            }
        } else if (qName.equalsIgnoreCase("category")) {
            if(tempVal.length() <= 100)
                tempCar.setCategory(tempVal);
            else {
                System.out.println("Inconsistent value type. Max string length is 100 for <category> but value is: '" + tempVal + "'");
                tempCar.setCategory("");
                ++numInconsistent;
            }
        } else if (qName.equalsIgnoreCase("model")) {
            if(tempVal.length() <= 100)
                tempCar.setModel(tempVal);
            else {
                System.out.println("Inconsistent value type. Max string length is 100 for <model> but value is: '" + tempVal + "'");
                tempCar.setModel("");
                ++numInconsistent;
            }
        } else if (qName.equalsIgnoreCase("year")) {
            try {
                tempCar.setYear(Integer.parseInt(tempVal));
            } catch (Exception e) {
                System.out.println("Inconsistent year type. Expected integer for <year> but value is: '" + tempVal + "'");
                tempCar.setYear(-2);
                ++numInconsistent;
            }
        }

    }

    public static void main(String[] args) throws Exception{
        // entry point to example code
        Car.resetMissing();

        // Setup database connection & prepared statement
        conn = null;
        psInsertRecord=null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/zotrides";
        String query = "CALL add_car_xml(?, ?, ?, ?, ?);";
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

            // Run Batch Statements
            SAXParserCar spe = new SAXParserCar();
            spe.runExample();

            // Execute batch
//            System.out.println("Starting to Execute");
            iNoRows = psInsertRecord.executeBatch();
            conn.commit();
            System.out.println("Executed: " + iNoRows.length);

            // Retrieve new count
            PreparedStatement counting = conn.prepareStatement("SELECT COUNT(*) as val FROM Cars;");
            ResultSet rs = counting.executeQuery();
            if (rs.next())
                spe.newCount = rs.getInt("val") - 9581;
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
