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
import java.sql.PreparedStatement;
import java.sql.ResultSet;


// Declaring a WebServlet called SingleCarServlet, which maps to url "/api/single-car"
@WebServlet(name = "SingleCarServlet", urlPatterns = "/api/single-car")
public class SingleCarServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides-slave");
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
            /*
            String query = "WITH numberedPickup AS\n" +
                    "\t(SELECT carID, pickupLocationID, numCars \n" +
                    "\tFROM pickup_car_from NATURAL JOIN (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars FROM pickup_car_from GROUP BY pickupLocationID) as numberedPickup),\n" +
                    "\n" +
                    "car_info(id, model, make, year, name, price, rating, numVotes) AS\n" +
                    "\t(SELECT Cars.id, model, make, year, name, price, rating, numVotes\n" +
                    "\tFROM Cars, category_of_car, Category, CarPrices, Ratings\n" +
                    "\tWHERE Cars.id = ? \n" +
                    "\t\t\tAND category_of_car.carID = '" + id + "' \n" +
                    "\t\t\tAND Category.id = category_of_car.categoryID\n" +
                    "\t\t\tAND CarPrices.carID = '" + id + "' \n" +
                    "\t\t\tAND Ratings.carID = '" + id + "')\n" +
                    "\n" +
                    "SELECT car_info.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, name as category, price, rating, numVotes, \n" +
                    "\t\tgroup_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
                    "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
                    "        group_concat(DISTINCT PickupLocation.id ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
                    "FROM car_info, numberedPickup, PickupLocation\n" +
                    "WHERE car_info.id = numberedPickup.carID AND numberedPickup.pickupLocationID = PickupLocation.id;";*/

            String query = "WITH numberedPickup AS\n" +
                    "\t(SELECT carID, pickupLocationID, numCars \n" +
                    "\tFROM pickup_car_from NATURAL JOIN (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars FROM pickup_car_from GROUP BY pickupLocationID) as numberedPickup),\n" +
                    "\n" +
                    "car_info(id, model, make, year, name, price, rating, numVotes) AS\n" +
                    "\t(SELECT Cars.id, model, make, year, name, price, rating, numVotes\n" +
                    "\tFROM ((Cars, category_of_car, Category) LEFT OUTER JOIN CarPrices ON CarPrices.carID = ?)\n" +
                    "\t\tLEFT OUTER JOIN Ratings ON Ratings.carID = ?\n" +
                    "\tWHERE Cars.id = ? \n" +
                    "\t\t\tAND category_of_car.carID = ? \n" +
                    "\t\t\tAND Category.id = category_of_car.categoryID)\n" +
                    "\n" +
                    "SELECT car_info.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, name as category, price, rating, numVotes, \n" +
                    "\t\tgroup_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
                    "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
                    "        group_concat(DISTINCT PickupLocation.id ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
                    "FROM (car_info LEFT OUTER JOIN numberedPickup ON car_info.id = numberedPickup.carID) LEFT OUTER JOIN PickupLocation ON numberedPickup.pickupLocationID = PickupLocation.id;";

            System.out.println("query is\n" + query);
            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, id);
            statement.setString(2, id);
            statement.setString(3, id);
            statement.setString(4, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            int count = 0;
            while (rs.next() && count++ < 1) { //TODO : DON'T THINK WE NEED TO ITERATE?
                String car_id = rs.getString("id");
                String car_name = rs.getString("name");
                String car_category = rs.getString("category");
                int unit_price = rs.getInt("price");
                double car_rating = rs.getDouble("rating");
                int car_votes = rs.getInt("numVotes");
                String location_address = rs.getString("address");
                String location_phone = rs.getString("phoneNumber");
                if (location_phone != null)
                    location_phone = location_phone.replace(";", "<br>");
                String location_ids = rs.getString("pickupID");
                /* NOTE : unlike previous example, we are wrapping everything into JSON to return it
                while previous example returned HTML.  Now HTML is generated by front-end
                 */

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("car_id", car_id);
                jsonObject.addProperty("car_name", car_name);
                jsonObject.addProperty("car_category", car_category);
                jsonObject.addProperty("unit_price", unit_price);
                jsonObject.addProperty("car_rating", car_rating);
                jsonObject.addProperty("car_votes", car_votes);
                jsonObject.addProperty("location_address", location_address);
                jsonObject.addProperty("location_phone", location_phone);
                jsonObject.addProperty("location_ids", location_ids);
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
            System.out.println("Error: " + e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }


    }

}