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


// Declaring a WebServlet called PaginateServlet, which maps to url "/api/paginate"
@WebServlet(name = "PaginateServlet", urlPatterns = "/api/paginate")
public class PaginateServlet extends HttpServlet {
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

    //TODO : TAKE THIS OUT WHEN DEPLOYING, IT'S JUST SO I CAN TEST FROM SAFARI

    /* FOR TESTING */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    /**
     * handles POST requests to handle pagination & stores settings into session
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* API
        *   - Next Page : pageStatus = 1, resultsPerPage = NULL
        *   - Previous Page : pageStatus = -1, resultsPerPage = NULL
        *   - Change # Results Per Page : pageStatus = NULL, resultsPerPage = 10, 25, 50, or 100 */

        // Retrieve parameters from payload
        String pageStatus = request.getParameter("pageStatus");
        String resultsPerPage = request.getParameter("resultsPerPage");
        boolean switchPg = pageStatus != null;
        boolean switchNumResults = resultsPerPage != null;

        // Setup for response + get session, which should already exist
        HttpSession session = request.getSession();
        CarListSettings previousSettings = (CarListSettings) session.getAttribute("previousSettings");
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();

        // If doesn't exist, should be blank
        if (previousSettings == null) {
            // should be empty page
            JsonObject result = new JsonObject();
            result.add("results", new JsonArray());
            result.addProperty("message", "Please perform a search or browse first");
            out.write(result.toString());
            return;
        }

        // Increment or Decrement Page Number
        if (switchPg) {
            boolean isPageForward = pageStatus.equals("1");
            boolean useCache = previousSettings.isWithinCache(isPageForward);
            boolean newPageInBounds;

            // update page number
            synchronized (previousSettings) {
                newPageInBounds = (isPageForward ? previousSettings.nextPageNumber() : previousSettings.prevPageNumber());
            }

            // if page is out of bounds OR we don't need to query (we use the cache), can immediately return results & quit
            if (!newPageInBounds || useCache) {
                System.out.println((!newPageInBounds ? "incr out of bounds" : "using cache to incr"));
                // return correct range from cache
                JsonArray subArr = new JsonArray();
                JsonArray jsonArray = previousSettings.getCache();
                for (int i = previousSettings.getStartIndex(); i < previousSettings.getEndIndex() && i < jsonArray.size(); ++i) {
                    subArr.add(jsonArray.get(i));
                }
                JsonObject result = new JsonObject();
                result.add("results", subArr);
                result.addProperty("message", previousSettings.getPaginationMessage());
                out.write(result.toString()); // write JSON string to output
                return;
            }

            // NOTE: at this point, page change is valid but we need to do query (it's beyond our cache), it's done a few lines down
            System.out.println("incr but have to recache");
        }

        // Change Number Results Per Page
        else if (switchNumResults) {
            // update # results per pg & reset to first page
            synchronized (previousSettings) {
                previousSettings.setNumResultsPerPage(Integer.parseInt(resultsPerPage));
            }
            System.out.println("changing # results per page");
        }

        // Run any necessary queries
        try (Connection conn = dataSource.getConnection()) {
            // prepare query
            String query = previousSettings.toQuery();
            PreparedStatement statement = conn.prepareStatement(query);

            // update parameters
            int counter = 1;
            for (String token : previousSettings.getTokens()) {
                statement.setString(counter++, token);
            }

            // execute query
            System.out.println("query:\n" + statement.toString());
            ResultSet rs = statement.executeQuery();

            // extract query results
            JsonArray jsonArray = new JsonArray(); // Iterate through each row of rs
            int count = 0;
//            Pattern firstThree = Pattern.compile("^([^;]+;[^;]+;[^;]+).*");
            Pattern firstThree = Pattern.compile("^(([^;]+;{0,1}){0,3}).*");
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

                if (location_address == null || location_phone == null || location_ids == null) {
                    jsonObject.addProperty("location_address", "");
                    jsonObject.addProperty("location_phone", "");
                    jsonObject.addProperty("location_ids", "");
                } else {
                    Matcher addresses = firstThree.matcher(location_address);
                    Matcher phones = firstThree.matcher(location_phone);
                    Matcher ids = firstThree.matcher(location_ids);
                    addresses.find();
                    phones.find();
                    ids.find();

                    String addr = addresses.group(1);
                    String phone = phones.group(1);
                    String ID = ids.group(1);

                    if (addr != null && addr.length() != 0)
                        jsonObject.addProperty("location_address", addr.charAt(addr.length() - 1) == ';' ? addr.substring(0, addr.length() - 1) : addr);
                    else
                        jsonObject.addProperty("location_address", "");

                    if (phone != null && phone.length() != 0)
                        jsonObject.addProperty("location_phone", phone.charAt(phone.length() - 1) == ';' ? phone.substring(0, phone.length() - 1) : phone);
                    else
                        jsonObject.addProperty("location_phone", "");

                    if (ID != null && ID.length() != 0)
                        jsonObject.addProperty("location_ids", ID.charAt(ID.length() - 1) == ';' ? ID.substring(0, ID.length() - 1) : ID);
                    else
                        jsonObject.addProperty("location_ids", "");
//                System.out.println(jsonObject.toString());
                }
                jsonArray.add(jsonObject);
            }

            // cache at most 100 returned results
            synchronized (previousSettings) {
                previousSettings.setCache(jsonArray);
            }

            // return result to frontend
            JsonObject result = new JsonObject();
            if (previousSettings.getNumResultsPerPage() == 100) {
                // 100 results per page, so just return entire cache
                result.add("results", jsonArray);
                System.out.println("size is 100 per page");
            } else {
                // return correct range from cache
                JsonArray subArr = new JsonArray();
                for (int i = previousSettings.getStartIndex(); i < previousSettings.getEndIndex() && i < jsonArray.size(); ++i) {
                    subArr.add(jsonArray.get(i));
                }
                result.add("results", subArr); // write JSON string to output
                System.out.println("size is not 100 per page");
            }
            result.addProperty("message", previousSettings.getPaginationMessage());
            out.write(result.toString());
            response.setStatus(200);         // set response status to 200 (OK)
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