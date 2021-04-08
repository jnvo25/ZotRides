package com.example.zotrides;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleCarServlet", urlPatterns = "/api/single-loc")
public class SingleLocationServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/zotrides")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            Statement statement = dbcon.createStatement();

            /*
            String query = "WITH car_info(id, model, make, year, name, rating, numVotes) AS\n" +
                    "\t(SELECT Cars.id, model, make, year, name, rating, numVotes\n" +
                    "\tFROM Cars, category_of_car, Category, Ratings\n" +
                    "\tWHERE Cars.id = category_of_car.carID \n" +
                    "\t\t\tAND category_of_car.categoryID = Category.id\n" +
                    "\t\t\tAND Ratings.carID = Cars.id),\n" +
                    "\n" +
                    "numbered_pickup(carID, pickupLocationID, num) AS\n" +
                    "\t(SELECT carID, pickupLocationID, ROW_NUMBER() OVER(PARTITION BY carID) \n" +
                    "\tFROM pickup_car_from)\n" +
                    "\n" +
                    "SELECT car_info.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, name as category, rating, numVotes, group_concat(DISTINCT address SEPARATOR '\\n') as address, group_concat(DISTINCT phoneNumber SEPARATOR '\\n') as phoneNumber\n" +
                    "FROM car_info, PickupLocation, numbered_pickup\n" +
                    "WHERE car_info.id = numbered_pickup.carID AND numbered_pickup.pickupLocationID = PickupLocation.id AND numbered_pickup.num < 4\n AND car_info.id = \"" + id + "\"" +
                    "GROUP BY carID\n" +
                    "ORDER BY rating DESC; "; */

            String query = "WITH car_info(id, model, make, year, name, rating, numVotes) AS\n" +
                    "\t(SELECT Cars.id, model, make, year, name, rating, numVotes\n" +
                    "\tFROM Cars, category_of_car, Category, Ratings\n" +
                    "\tWHERE Cars.id = '" + id + "' \n" +
                    "\t\t\tAND category_of_car.carID = '" + id + "' \n" +
                    "\t\t\tAND Category.id = category_of_Car.categoryID\n" +
                    "\t\t\tAND Ratings.carID = '" + id + "')\n" +
                    "\n" +
                    "SELECT car_info.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, name as category, rating, numVotes, group_concat(DISTINCT address SEPARATOR ';') as address, group_concat(DISTINCT phoneNumber SEPARATOR ';') as phoneNumber\n" +
                    "FROM car_info, pickup_car_from, PickupLocation\n" +
                    "WHERE car_info.id = pickup_car_from.carID AND pickup_car_from.pickupLocationID = PickupLocation.id;";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            int count = 0;
            while (rs.next() && count++ < 20) {
                String car_id = rs.getString("id");
                String car_name = rs.getString("name");
                String car_category = rs.getString("category");
                double car_rating = rs.getDouble("rating");
                int car_votes = rs.getInt("numVotes");
                String location_address = rs.getString("address").replace(";", "<br>");
                String location_phone = rs.getString("phoneNumber").replace(";", "<br>");

                /* NOTE : unlike previous example, we are wrapping everything into JSON to return it
                while previous example returned HTML.  Now HTML is generated by front-end
                 */

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("car_id", car_id);
                jsonObject.addProperty("car_name", car_name);
                jsonObject.addProperty("car_category", car_category);
                jsonObject.addProperty("car_rating", car_rating);
                jsonObject.addProperty("car_votes", car_votes);
                jsonObject.addProperty("location_address", location_address);
                jsonObject.addProperty("location_phone", location_phone);
                // FOR DEBUGGING: System.out.println(jsonObject.toString());
                jsonArray.add(jsonObject);
            }

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();

    }

}