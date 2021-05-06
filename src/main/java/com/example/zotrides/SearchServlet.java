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
import javax.servlet.http.HttpSession;
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
        doPost(request, response);
    }

    /**
     * handles POST requests to store query in session
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve parameters from url request
        String model = request.getParameter("model");
        String year = request.getParameter("year");
        String make = request.getParameter("make");
        String location = request.getParameter("location");

        String additional = "";
        if (model != null && !model.isEmpty())
            additional += " AND model LIKE \"%" + model + "%\"";
        if (year != null && !year.isEmpty())
            additional += " AND year = " + year;
        if (make != null && !make.isEmpty())
            additional += " AND make LIKE \"%" + make + "%\"";
        if (location != null && !location.isEmpty())
            additional += " AND address LIKE \"%" + location + "%\"";
        String query = "WITH pickupCarCounts AS (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars \n" +
                "\tFROM pickup_car_from \n" +
                "    GROUP BY pickupLocationID),\n" +
                "    \n" +
                "    car_info AS (SELECT Cars.id as id, model, make, year, name, price, rating, numVotes\n" +
                "\tFROM category_of_car, Category, Cars, CarPrices, Ratings, pickup_car_from, PickupLocation\n" +
                "\tWHERE category_of_car.categoryID = Category.id\n" +
                "\t\t\tAND Cars.id = category_of_car.carID \n" +
                "            AND Cars.id = CarPrices.carID\n" +
                "            AND Ratings.carID = Cars.id\n" +
                "            AND pickup_car_from.carID = Cars.id\n" + additional + "\n" +
                "            AND PickupLocation.id = pickup_car_from.pickupLocationID)\n" +
                "    \n" +
                "SELECT car_info.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, \n" +
                "\t\tname as category, price, rating, numVotes,\n" +
                "        group_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
                "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
                "        group_concat(DISTINCT PickupLocation.id ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
                "FROM car_info, pickup_car_from, pickupCarCounts, PickupLocation\n" +
                "WHERE pickup_car_from.carID = car_info.id AND pickup_car_from.pickupLocationID = pickupCarCounts.pickupLocationID AND pickup_car_from.pickupLocationID = PickupLocation.id\n" +
                "GROUP BY car_info.id\n";

//        System.out.println("query: \n" + query);

        // Store base query into session (to maintain consistency when jumping back to CarsList page)
        HttpSession session = request.getSession();
        CarListSettings previousSettings = (CarListSettings) session.getAttribute("previousSettings");
        if (previousSettings == null) {
            previousSettings = new CarListSettings(query, 1, 10);
            session.setAttribute("previousSettings", previousSettings);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousSettings) {
                previousSettings.reset(query, 1, 10);
            }
        }

        // Run query & return response
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        try (Connection conn = dataSource.getConnection()) {
            // drop temporary table
            PreparedStatement statement = conn.prepareStatement("DROP TABLE IF EXISTS temp");
            statement.executeUpdate();
            statement.close();

            // store query in temporary table
            query = previousSettings.toQuery();
            statement = conn.prepareStatement("CREATE TEMPORARY TABLE temp\n" + query + ";");
//            System.out.println("query:\n" + query + "\n");
            statement.executeUpdate();
            statement.close();


            // access results
            statement = conn.prepareStatement("SELECT * FROM temp LIMIT 100;");
            ResultSet rs = statement.executeQuery();

            // process results
            JsonArray jsonArray = new JsonArray();
            int count = 0;
            //Pattern firstThree = Pattern.compile("^([^;]+;[^;]+;[^;]+).*");
            Pattern firstThree = Pattern.compile("^(([^;]+;{0,1}){1,3}).*");
            while (rs.next() && count++ < 100) { // Iterate through each row of rs
                String car_id = rs.getString("id");
                String car_name = rs.getString("name");
                String car_category = rs.getString("category");
                double car_rating = rs.getDouble("rating");
                int car_votes = rs.getInt("numVotes");
                String location_address = rs.getString("address");
                String location_phone = rs.getString("phoneNumber");
                String location_ids = rs.getString("pickupID");

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
                String addr = addresses.group(1);
                String phone = phones.group(1);
                String ID = ids.group(1);

                jsonObject.addProperty("location_address", addr.charAt(addr.length() - 1) == ';' ? addr.substring(0, addr.length() - 1) : addr);
                jsonObject.addProperty("location_phone", phone.charAt(phone.length() - 1) == ';' ? phone.substring(0, phone.length() - 1) : phone);
                jsonObject.addProperty("location_ids", ID.charAt(ID.length() - 1) == ';' ? ID.substring(0, ID.length() - 1) : ID);

//                System.out.println(jsonObject.toString());
                jsonArray.add(jsonObject);
            }

            // cache at most 100 returned results
            synchronized (previousSettings) {
                previousSettings.setCache(jsonArray);
            }

//            System.out.println(jsonArray);

            // return results
            JsonArray firstTen = new JsonArray();
            for (int i = 0; i < 10 && i < jsonArray.size(); ++i) {
                firstTen.add(jsonArray.get(i));
            }

            JsonObject result = new JsonObject();
            result.add("results", firstTen);
            result.addProperty("message", previousSettings.getPaginationMessage());
            out.write(result.toString());
            response.setStatus(200);         // set response status to 200 (OK)
            rs.close();
            statement.close();

            // get the max number of results
            statement = conn.prepareStatement("SELECT COUNT(id) as numResults FROM temp;");
            rs = statement.executeQuery(); // Perform the query
            if (rs.next()) {
                int maxResults = rs.getInt("numResults");
                synchronized (previousSettings) {
                    previousSettings.setMaxNumResults(maxResults);
                }
                System.out.println("stored max results: " + maxResults);
            }
            rs.close();
            statement.close();
            System.out.println("no issues");

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // display on backend (more detailed)
            System.out.println("error: " + e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}