package com.example.zotrides;

import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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


@WebServlet(name = "AppLogin", urlPatterns = "/api/app-login")
public class AppLogin extends HttpServlet {

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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
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
            /* new version */
            String query = "SELECT id, password\n" +
                    "FROM Customers\n" +
                    "WHERE email = ?;";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
//            System.out.println("query is:\n" + statement.toString());

            // Perform the query
            ResultSet rs = statement.executeQuery();

            isValid = rs.next();
            if (isValid) {
                customerID = rs.getInt("id");
                isValid = new StrongPasswordEncryptor().checkPassword(password, rs.getString("password"));
//                System.out.println("pw is: " + isValid);
            }

//            System.out.println("the user/pass combo is: " + isValid);

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
            out.close();
            return;
        }

        /* verify Recaptcha credentials & return response */
        JsonObject responseJsonObject = new JsonObject();
        if (isValid) {
            // Login success

            // set this user into the session
            request.getSession().setAttribute("user", new User(username));
            request.getSession().setAttribute("customerID", customerID);

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            System.out.println("success on both");

        } else {
            // Login fail
            System.out.println("Problem at login matching");
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "incorrect username / password");
        }
        // response.getWriter().write(responseJsonObject.toString());
        out.write(responseJsonObject.toString());
        out.close();
    }
}
