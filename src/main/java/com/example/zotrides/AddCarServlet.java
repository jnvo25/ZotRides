package com.example.zotrides;

import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AddCarServlet", urlPatterns = "/api/add-car")
public class AddCarServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides-master");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // TODO : QUERY WORKS ON SQL WORKBENCH BUT HAVEN'T TESTED THE SERVLET YET, LMK IF THERE'S SOMETHING WACK
    // PRINT STATEMENTS BELOW TO PRINT STATUSES TO CONSOLE
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* API
         *   - Parameters (ALL ARE REQUIRED) : make, model, year, address, category
         *   - Return --> {"message": "..."}
         *   ---------
         *   - Make : string with length <= 100
         *   - Model : string with length <= 100
         *   - Year : should be able to be converted to integer
         *   - Address : string with length <= 200
         *   - Category : string with length <= 100 */

        String make = request.getParameter("make");
        String model = request.getParameter("model");
        String year = request.getParameter("year");
        String address = request.getParameter("address");
        String category = request.getParameter("category");

        // TODO : INPUT VALIDATION

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        /* run stored procedure */
        try (Connection conn = dataSource.getConnection()) {
            // Check for nulls
            if(make == null)
                throw new NullPointerException("ERROR: make cannot be null");
            else if(model == null)
                throw new NullPointerException("ERROR: model cannot be null");
            else if(year == null)
                throw new NullPointerException("ERROR: year cannot be null");
            else if(address == null)
                throw new NullPointerException("ERROR: address cannot be null");
            else if(category == null)
                throw new NullPointerException("ERROR: category cannot be null");

            try {
                Integer.parseInt(year);
            } catch (Exception e) {
                throw new Exception("ERROR: Year must be an integer");
            }

            String query = "CALL add_car(?, ?, ?, ?, ?, @message);";

            // Prepare statement with parameters
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, make);
            statement.setString(2, model);
            statement.setInt(3, Integer.parseInt(year));
            statement.setString(4, address);
            statement.setString(5, category);

            System.out.println("query is:\n" + statement.toString()); // DEBUGGING

            // Run stored procedure
            statement.executeUpdate();
            statement.close();

            // Retrieve results of procedure
            statement = conn.prepareStatement("SELECT @message;");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                JsonObject result = new JsonObject();
                String message = rs.getString("@message");
                result.addProperty("message", message);
                out.write(result.toString());
                System.out.println(message); // DEBUGGINg
            }
            rs.close();
            statement.close();
            out.close();
        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // for more detail
            System.out.println(e.getMessage());

            // set response status to 500 (Internal Server Error)
//            response.setStatus(500);
        }
        out.close();
    }
}
