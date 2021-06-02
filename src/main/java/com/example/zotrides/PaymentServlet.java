package com.example.zotrides;

import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
//    private DataSource dataSource;
//
//    public void init(ServletConfig config) {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String ccNumber = request.getParameter("ccNumber");
        String expDate = request.getParameter("expDate");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        // Get shopping cart
        HttpSession session = request.getSession();
        ArrayList<CartItem> previousItems = (ArrayList<CartItem>) session.getAttribute("previousItems");
        // Get current sale ID
        SaleMetaInfo saleInfo = (SaleMetaInfo) session.getAttribute("saleMetaInfo");

        /* verify username / password from database */
        boolean isValid = false;
        try (Connection conn = ((DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides-slave")).getConnection()) {
            String query = "SELECT id\n" +
                    "FROM CreditCards\n" +
                    "WHERE firstName = ? AND lastName = ?" +
                    "AND id = ? AND expiration = ?;";

            // Prepare statement with parameters
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, ccNumber);
            statement.setString(4, expDate);

//             System.out.println("query is:\n" + statement.toString());

            // Perform the query & check result
            ResultSet rs = statement.executeQuery();
            isValid = rs.next();
            rs.close();
            statement.close();

            // Incorrect payment info --> return error & fail status
            if (!isValid) {
                System.out.println("fail");
                JsonObject result = new JsonObject();
                result.addProperty("status", "fail");
                result.addProperty("message", "incorrect payment information");
                out.write(result.toString());
                out.close();
                return;
            }

            // NOTE : at this point payment info is valid --> need to update database with items
            System.out.println("valid payment info");

            if (saleInfo == null) {
                // perform query
                query = "SELECT MAX(saleID) AS base FROM Reservations;";
                statement = conn.prepareStatement(query);
                rs = statement.executeQuery();

                // if valid result, set saleID to returned value + 1
                if (rs.next()) {
                    saleInfo = new SaleMetaInfo(rs.getInt("base") + 1);
                }

                // otherwise table is empty, set first saleID
                else {
                    saleInfo = new SaleMetaInfo(1);
                }

                // add attribute to session
                session.setAttribute("saleMetaInfo", saleInfo);
                System.out.println("saleID: " + saleInfo.getSaleID());

                statement.close();
                rs.close();
            }
        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // for more detail
            System.out.println(e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }

        try (Connection conn = ((DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides-master")).getConnection()) {
            // Generate data update query
            if (previousItems != null && !previousItems.isEmpty()) {
                String query = "INSERT INTO Reservations(startDate, endDate, customerID, carID, saleDate, saleID) VALUES";
                String customerID = session.getAttribute("customerID").toString();

                /* NOTE : for our convenience encapsulated manually using '?' inside the CartItem.java class's toQuery method
                which we defined.  What it outputs is described below
                    Format:
                        CartItem.toQuery() function outputs -->
                        "('" + startDate + "', '" + endDate + "', '" + customerID + "', '" + carID + "', '" + saleDate + "', '" + saleID + "')"
                */
                int i;
                for (i = 0; i < previousItems.size() - 1; ++i) {
                    query += previousItems.get(i).toQuery(customerID, saleInfo.getSaleID()) + ",\n";
                }
                query += previousItems.get(i).toQuery(customerID, saleInfo.getSaleID()) + ";";

                System.out.println("update query is: " + query);

                // Execute data update query
                PreparedStatement statement = conn.prepareStatement(query);
                int rowsUpdated = statement.executeUpdate();
                statement.close();
                System.out.println("updated " + rowsUpdated + " rows!");
            }

            // Clear the shopping cart
            synchronized (previousItems) {
                previousItems.clear();
                System.out.println("empty : " + previousItems.isEmpty());
            }

            // Return success message
            JsonObject result = new JsonObject();
            result.addProperty("status", "success");
            result.addProperty("message", saleInfo.getSaleID());
            out.write(result.toString());

            // Increment sale ID
            synchronized (saleInfo) {
                saleInfo.nextSaleID();
                System.out.println("incremented saleID to: " + saleInfo.getSaleID());
            }
        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // for more detail
            System.out.println(e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
