package com.example.zotrides;

import com.google.gson.JsonArray;
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
import java.sql.ResultSet;
import java.sql.PreparedStatement;

// Declaring a WebServlet called SingleLocationServlet, which maps to url "/api/single-loc"
@WebServlet(name = "SingleLocationServlet", urlPatterns = "/api/single-loc")
public class SingleLocationServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {
            String query = "WITH pickup_info(id, address, phoneNumber) AS \n" +
                    "\t(SELECT *\n" +
                    "\tFROM PickupLocation\n" +
                    "\tWHERE PickupLocation.id = '" + id + "')\n" +
                    "\n" +
                    "SELECT pickup_info.id as id, address, phoneNumber,\n" +
                    "\tgroup_concat(DISTINCT concat_ws(' ', make, model, year) ORDER BY carID ASC SEPARATOR ';') as name,\n" +
                    "    group_concat(DISTINCT carID ORDER BY carID ASC SEPARATOR ';') as carID\n" +
                    "FROM pickup_info, pickup_car_from, Cars\n" +
                    "WHERE pickup_info.id = pickup_car_from.pickupLocationID\n" +
                    "\tAND Cars.id = pickup_car_from.carID\n" +
                    "GROUP BY pickup_info.id;";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            // THIS DOESN'T WORK !?
            // statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();
            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            int count = 0;
            while (rs.next() && count++ < 20) {
                String location_id = rs.getString("id");
                String location_address = rs.getString("address");
                String location_phone = rs.getString("phoneNumber");
                String cars_offered = rs.getString("name");
                String cars_ids = rs.getString("carID");

                /* NOTE : unlike previous example, we are wrapping everything into JSON to return it
                while previous example returned HTML.  Now HTML is generated by front-end
                 */

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("location_id", location_id);
                jsonObject.addProperty("location_address", location_address);
                jsonObject.addProperty("location_phone", location_phone);
                jsonObject.addProperty("cars_offered", cars_offered);
                jsonObject.addProperty("cars_ids", cars_ids);
                System.out.println(jsonObject.toString());
                jsonArray.add(jsonObject);
            }

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

    }

}