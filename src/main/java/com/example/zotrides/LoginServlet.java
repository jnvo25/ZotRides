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
import java.sql.Statement;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        /* verify username / password from database */
        boolean isValid = false;
        int customerID = -1;
        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();

            String query = "SELECT id\n" +
                    "FROM Customers\n" +
                    "WHERE email = \"" + username + "\" AND password = \"" + password + "\";";

            System.out.println("query is:\n" + query);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            isValid = rs.next();
            if (isValid) {
                customerID = rs.getInt("id");
                System.out.println("success");
            }

            System.out.println("the user/pass combo is: " + isValid);

            rs.close();
            statement.close();
        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
            out.close();
            return;
        }

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        JsonObject responseJsonObject = new JsonObject();
        // TODO : change this condition to be valid
        if (isValid) {
            // Login success:

            // set this user into the session
            request.getSession().setAttribute("user", new User(username));
            request.getSession().setAttribute("customerID", customerID);

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

        } else {
            // Login fail
            responseJsonObject.addProperty("status", "fail");

            // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
            // TODO : change this part too
            /*
            if (!username.equals("anteater")) {
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            } else {
                responseJsonObject.addProperty("message", "incorrect password");
            }*/
            responseJsonObject.addProperty("message", "incorrect username / password");
        }
        // response.getWriter().write(responseJsonObject.toString());
        out.write(responseJsonObject.toString());
        out.close();
    }
}
