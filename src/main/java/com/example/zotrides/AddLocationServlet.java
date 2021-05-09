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

@WebServlet(name = "AddLocationServlet", urlPatterns = "/api/add-loc")
public class AddLocationServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides");
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
         *   - Parameters : address (REQUIRED), phoneNumber (OPTIONAL)
         *   - Return --> {"message": "..."}
         *   ---------
         *   - Address : string with length <= 200
         *   - PhoneNumber : string with length <= 20 OR NULL */

        String address = request.getParameter("address");
        String phone = request.getParameter("phoneNumber");

        // TODO: INPUT VALIDATION
        if (phone == null)
            phone = "";

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        /* run stored procedure */
        try (Connection conn = dataSource.getConnection()) {
            String query = "CALL add_pickup_location(?, ?, @message);";

            // Prepare statement with parameters
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, address);
            statement.setString(2, phone);

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
//            response.sendError(500, e.getMessage());
            // set response status to 500 (Internal Server Error)
//            response.setStatus(500);
        }
        out.close();
    }
}
