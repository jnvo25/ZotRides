package com.example.zotrides;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Declaring a WebServlet called SearchServlet, which maps to url "/api/search-car"
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search-car")
public class SearchServlet extends HttpServlet {
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
        String model = request.getParameter("model");
        String year = request.getParameter("year");
        String make = request.getParameter("make");
        String location = request.getParameter("location");


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {
            // construct query
            String additional = "";
            if (model != null && !model.isEmpty())
                additional += " AND model LIKE \"%" + model + "%\"";
            if (year != null && !year.isEmpty())
                additional += " AND year = " + year;
            if (make != null && !make.isEmpty())
                additional += " AND make LIKE \"%" + make + "%\"";

            String addtionalLoc = "";
            if (location != null && !location.isEmpty())
                addtionalLoc += " AND address LIKE \"%" + location + "%\"";

            /*
            String query = "SELECT Cars.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, \n" +
                    "\t\tname as category, rating, numVotes,\n" +
                    "        group_concat(DISTINCT address ORDER BY pickupLocationID SEPARATOR ';') as address, \n" +
                    "        group_concat(DISTINCT phoneNumber ORDER BY pickupLocationID SEPARATOR ';') as phoneNumber,\n" +
                    "        group_concat(DISTINCT pickupLocationID ORDER BY pickupLocationID SEPARATOR ';') as pickupID\n" +
                    "FROM category_of_car, Category, Cars, Ratings, pickup_car_from, PickupLocation\n" +
                    "WHERE category_of_car.categoryID = Category.id AND category_of_car.carID = Cars.id" + additional + " \n" +
                    "\tAND Ratings.carID = Cars.id AND pickup_car_from.carID = Cars.id AND pickup_car_from.pickupLocationID = PickupLocation.id\n" +
                    "GROUP BY Cars.id\n" +
                    "ORDER BY rating DESC\n"+
                    "LIMIT 100;";*/

            /*
            String query = "WITH car_info(id, model, make, year, name, price, rating, numVotes) AS\n" +
                    "\t(SELECT Cars.id, model, make, year, name, price, rating, numVotes\n" +
                    "\tFROM Cars, category_of_car, Category, CarPrices, Ratings\n" +
                    "\tWHERE Cars.id = category_of_car.carID \n" +
                    "\t\t\tAND category_of_car.categoryID = Category.id\n" +
                    "            AND Cars.id = CarPrices.carID\n" +
                    "            AND Ratings.carID = Cars.id" + additional + "),\n" +
                    "\n" +
                    "numbered_pickup(carID, pickupLocationID, numCars, num) AS\n" +
                    "\t(SELECT carID, pickupLocationID, numCars, ROW_NUMBER() OVER(PARTITION BY carID) \n" +
                    "\tFROM pickup_car_from NATURAL JOIN (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars FROM pickup_car_from GROUP BY pickupLocationID) as numberedPickup)\n" +
                    "\n" +
                    "SELECT car_info.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, \n" +
                    "\t\tname as category, price, rating, numVotes,\n" +
                    "        group_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
                    "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
                    "        group_concat(DISTINCT pickupLocationID ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
                    "FROM car_info, numbered_pickup, PickupLocation \n" +
                    "WHERE car_info.id = numbered_pickup.carID AND numbered_pickup.num < 4 AND numbered_pickup.pickupLocationID = PickupLocation.id"+ addtionalLoc + "\n" +
                    "GROUP BY car_info.id\n" +
                    "ORDER BY rating DESC\n" +
                    "LIMIT 100;";*/

            String query = "WITH pickupCarCounts AS (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars \n" +
                    "\tFROM pickup_car_from \n" +
                    "    GROUP BY pickupLocationID)\n" +
                    "    \n" +
                    "SELECT Cars.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, \n" +
                    "\t\tname as category, price, rating, numVotes,\n" +
                    "        group_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
                    "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
                    "        group_concat(DISTINCT PickupLocation.id ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
                    "FROM category_of_car, Category, Cars, CarPrices, Ratings, pickupCarCounts, pickup_car_from, PickupLocation\n" +
                    "WHERE category_of_car.categoryID = Category.id AND category_of_car.carID = Cars.id AND Cars.id = CarPrices.carID" + additional + " \n" +
                    "\tAND Ratings.carID = Cars.id AND pickup_car_from.carID = Cars.id AND pickup_car_from.pickupLocationID = pickupCarCounts.pickupLocationID AND pickup_car_from.pickupLocationID = PickupLocation.id"+
                    addtionalLoc + "\n" +
                    "GROUP BY Cars.id\n" +
                    "ORDER BY rating DESC\n" +
                    "LIMIT 100; ";

            System.out.println("query:\n" + query);

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            int count = 0;
            Pattern firstThree = Pattern.compile("^([^;]+;[^;]+;[^;]+).*");
            while (rs.next() && count++ < 20) {
                String car_id = rs.getString("id");
                String car_name = rs.getString("name");
                String car_category = rs.getString("category");
                double car_rating = rs.getDouble("rating");
                int car_votes = rs.getInt("numVotes");
                String location_address = rs.getString("address");
                String location_phone = rs.getString("phoneNumber");
                String location_ids = rs.getString("pickupID");
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

                Matcher addresses = firstThree.matcher(location_address);
                Matcher phones = firstThree.matcher(location_phone);
                Matcher ids = firstThree.matcher(location_ids);
                addresses.find();
                phones.find();
                ids.find();

                jsonObject.addProperty("location_address", addresses.group(1));
                jsonObject.addProperty("location_phone", phones.group(1));
                jsonObject.addProperty("location_ids", ids.group(1));

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

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }


    }

}