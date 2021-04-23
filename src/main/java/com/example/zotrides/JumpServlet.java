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


// Declaring a WebServlet called JumpServlet, which maps to url "/api/jump"
@WebServlet(name = "JumpServlet", urlPatterns = "/api/jump")
public class JumpServlet extends HttpServlet {
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
     * handles GET requests & returns exact same previous query results with the same page / # results per page
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get previous settings from session, which should already exist
        HttpSession session = request.getSession();
        CarListSettings previousSettings = (CarListSettings) session.getAttribute("previousSettings");
        //TODO: SHOULD ERROR MESSAGES BE CLEARED

        // return results
        JsonArray subArr = new JsonArray();
        JsonArray jsonArray = previousSettings.getCache();
        for (int i = previousSettings.getStartIndex(); i < previousSettings.getEndIndex() && i < jsonArray.size(); ++i) {
            subArr.add(jsonArray.get(i));
        }
        JsonObject result = new JsonObject();
        result.add("results", subArr);
        result.addProperty("message", previousSettings.getPaginationMessage()); //TODO: MAKE SURE THIS GETS PRINTED ON FRONTEND
        response.getWriter().write(result.toString()); // write JSON string to output
    }
}