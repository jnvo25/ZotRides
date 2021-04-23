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
import java.sql.Statement;
import java.util.ArrayList;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // TODO : REMOVE LATER
    // FOR TESTING ONLY
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

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

        /* verify username / password from database */
        boolean isValid = false;
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT id\n" +
                    "FROM CreditCards\n" +
                    "WHERE firstName = \"" + firstName + "\" AND lastName = \"" + lastName +
                    "\" AND id = \"" + ccNumber + "\" AND expiration = \"" + expDate + "\";";

            // System.out.println("query is:\n" + query);

            // Perform the query & check result
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
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

            // Get shopping cart
            HttpSession session = request.getSession();
            ArrayList<CartItem> previousItems = (ArrayList<CartItem>) session.getAttribute("previousItems");

            // Generate data update query
            query = previousItems != null && !previousItems.isEmpty()
                    ? "INSERT INTO Reservations(startDate, endDate, customerID, carID, saleDate) VALUES"
                    : "";
            String customerID = request.getSession().getAttribute("customerID").toString();
            int i;
            for (i = 0; i < previousItems.size() - 1; ++i) {
                query += previousItems.get(i).toQuery(customerID) + ",\n";
            }
            query += (previousItems.size() - 1 >= 0
                        ? previousItems.get(i).toQuery(customerID) + ";"
                        : "");

            System.out.println("update query is: " + query);

            // Execute data update query
            statement = conn.createStatement();
            int rowsUpdated = statement.executeUpdate(query);
            statement.close();
            System.out.println("updated " + rowsUpdated + " rows!");

            // Clear the shopping cart
            synchronized (previousItems) {
                previousItems.clear();
                System.out.println("empty : " + previousItems.isEmpty());
            }

            // Return success message
            JsonObject result = new JsonObject();
            result.addProperty("status", "success");
            result.addProperty("message", "success");
            out.write(result.toString());
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
        out.close();
    }
}
