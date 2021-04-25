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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Declaring a WebServlet called BrowseServlet, which maps to url "/api/browse-car"
@WebServlet(name = "BrowseServlet", urlPatterns = "/api/browse-car")
public class BrowseServlet extends HttpServlet {
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
    /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String category = request.getParameter("category");
        String yearDigit = request.getParameter("year");
        String modelLetter = request.getParameter("model");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {
            // construct query
            String additional = "";
            if (category != null && !category.isEmpty()) {
                int catID = 8;
                switch (category) {
                    case "pickup":
                        catID = 1;
                        break;
                    case "wagon":
                        catID = 2;
                        break;
                    case "van":
                        catID = 3;
                        break;
                    case "coupe":
                        catID = 4;
                        break;
                    case "sedan":
                        catID = 5;
                        break;
                    case "hatchback":
                        catID = 6;
                        break;
                    case "convertible":
                        catID = 7;
                    default: // SUV
                        break;
                }
                additional += " AND categoryID = " + catID;
            }
            if (modelLetter != null && !modelLetter.isEmpty())
                if (modelLetter.equals("*")) {
                    // wild card
                    additional += " AND model RLIKE \"^[^A-Za-z0-9].*\"";
                } else {
                    // alphabetical
                    additional += " AND model LIKE \"" + modelLetter + "%\"";
                }
            if (yearDigit != null && !yearDigit.isEmpty())
                additional += " AND year LIKE \"" + yearDigit + "%\"";

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
                    "LIMIT 100;";

            String query = "WITH numberedPickup AS\n" +
                    "\t(SELECT carID, pickupLocationID, numCars \n" +
                    "\tFROM pickup_car_from NATURAL JOIN (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars FROM pickup_car_from GROUP BY pickupLocationID) as numberedPickup)\n" +
                    "    \n" +
                    "SELECT Cars.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, \n" +
                    "\t\tname as category, price, rating, numVotes,\n" +
                    "        group_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
                    "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
                    "        group_concat(DISTINCT pickupLocationID ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
                    "FROM category_of_car, Category, Cars, CarPrices, Ratings, numberedPickup, PickupLocation\n" +
                    "WHERE category_of_car.categoryID = Category.id AND category_of_car.carID = Cars.id AND Cars.id = CarPrices.carID"+ additional + "\n" +
                    "\tAND Ratings.carID = Cars.id AND numberedPickup.carID = Cars.id AND numberedPickup.pickupLocationID = PickupLocation.id\n" +
                    "GROUP BY Cars.id\n" +
                    "ORDER BY rating DESC\n" +
                    "LIMIT 100;";

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
                    "\tAND Ratings.carID = Cars.id AND pickup_car_from.carID = Cars.id AND pickup_car_from.pickupLocationID = pickupCarCounts.pickupLocationID AND pickup_car_from.pickupLocationID = PickupLocation.id\n" +
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
    }*/

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {
            // retrieve query
            HttpSession session = request.getSession();
            CarListSettings previousSettings = (CarListSettings) session.getAttribute("previousSettings");
            String query = previousSettings.toQuery();

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
//                System.out.println(jsonObject.toString());
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
    }*/

    /**
     * handles POST requests to store query in session & return results
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve parameters from payload
        String category = request.getParameter("category");
        String yearDigit = request.getParameter("year");
        String modelLetter = request.getParameter("model");

        // Process parameters to get query restrictions
        String additional = "";
        if (category != null && !category.isEmpty()) {
            int catID = 8;
            switch (category) {
                case "pickup":
                    catID = 1;
                    break;
                case "wagon":
                    catID = 2;
                    break;
                case "van":
                    catID = 3;
                    break;
                case "coupe":
                    catID = 4;
                    break;
                case "sedan":
                    catID = 5;
                    break;
                case "hatchback":
                    catID = 6;
                    break;
                case "convertible":
                    catID = 7;
                default: // SUV
                    break;
            }
            additional += " AND categoryID = " + catID;
        }
        if (modelLetter != null && !modelLetter.isEmpty())
            if (modelLetter.equals("*")) {
                // wild card
                additional += " AND model RLIKE \"^[^A-Za-z0-9].*\"";
            } else {
                // alphabetical
                additional += " AND model LIKE \"" + modelLetter + "%\"";
            }
        if (yearDigit != null && !yearDigit.isEmpty())
            additional += " AND year LIKE \"" + yearDigit + "%\"";

        // Integrate into a base form of the query
        String query =
                "WITH pickupCarCounts AS (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars \n" +
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
                "\tAND Ratings.carID = Cars.id AND pickup_car_from.carID = Cars.id AND pickup_car_from.pickupLocationID = pickupCarCounts.pickupLocationID AND pickup_car_from.pickupLocationID = PickupLocation.id\n" +
                "GROUP BY Cars.id\n";

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
        PrintWriter out = response.getWriter();      // Output stream to STDOUT
        try (Connection conn = dataSource.getConnection()) {
            // drop temporary table
            PreparedStatement statement = conn.prepareStatement("DROP TABLE IF EXISTS temp");
            statement.executeUpdate();
            statement.close();

            // store query in temporary table
            query = previousSettings.toQuery();
            statement = conn.prepareStatement("CREATE TEMPORARY TABLE temp\n" + query);
            statement.executeUpdate();
            statement.close();

            // access results
            statement = conn.prepareStatement("SELECT * FROM temp LIMIT 100;");
            ResultSet rs = statement.executeQuery();

            // process returned results
            JsonArray jsonArray = new JsonArray(); // Iterate through each row of rs
            int count = 0;
//            Pattern firstThree = Pattern.compile("^([^;]+;[^;]+;[^;]+).*");
            Pattern firstThree = Pattern.compile("^(([^;]+;{0,1}){1,3}).*");
            while (rs.next() && count++ < 100) {
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

                // Filter out only the top 3 based on sorted order
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
            synchronized (previousSettings) { // TODO: WOULDN'T BOTHER CACHING THIS IF IT'S 100
                previousSettings.setCache(jsonArray);
            }

            // return results
            JsonArray firstTen = new JsonArray();
            for (int i = 0; i < 10 && i < jsonArray.size(); ++i) { //TODO : FOR SORTING / PAGINATION WOULD JUST RETURN ITSELF IF IT'S 100
                firstTen.add(jsonArray.get(i));
            }
            //TODO: REPLACE WITH THIS SECTION & MAKE SURE THIS GETS PRINTED ON FRONTEND

            JsonObject result = new JsonObject();
            result.add("results", firstTen);
            result.addProperty("message", previousSettings.getPaginationMessage());
            out.write(result.toString());
            System.out.println("here");

//            out.write(firstTen.toString()); // write JSON string to output
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

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // display on backend (more detailed)
            System.out.println(e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }

}