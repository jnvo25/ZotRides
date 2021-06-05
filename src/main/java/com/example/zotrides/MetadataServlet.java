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

@WebServlet(name = "MetadataServlet", urlPatterns = "/api/metadata")
public class MetadataServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/zotrides-master");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // TODO : HAVEN'T TESTED THE SERVLET YET, LMK IF THERE'S SOMETHING WACK
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* API
         *   - Parameters : NONE
         *   - Return --> {"numTables": "...", 1 : {tableName: "...", "fields": [...], "types": [...]}, ...}
         *   ---------
         *   Use the numTables property to determine how many tables' metadata need to be displayed.
         *   For each i = 1 ... n -th table, it will have an element for its name, a JSON array for all its fields,
         *     and another JSONArray for all of its fields' types.   */

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        /* run stored procedure */
        try (Connection conn = dataSource.getConnection()) {
            // View all tables
            PreparedStatement statement = conn.prepareStatement("SHOW TABLES;");
            ResultSet rs = statement.executeQuery();

            // Get metadata for each table
            PreparedStatement statement2;
            ResultSet rs2;
            JsonObject finalResult = new JsonObject();
            int i;
            for (i = 0; rs.next(); ++i) {
                String table = rs.getString("Tables_in_zotrides");
                statement2 = conn.prepareStatement("DESCRIBE " + table);
                rs2 = statement2.executeQuery();

                JsonObject result = new JsonObject();
                JsonArray fields = new JsonArray();
                JsonArray types = new JsonArray();

                // filter out backup table
                if (table.equals("Customers_backup")) {
                    statement2.close();
                    rs2.close();
                    continue;
                }

                while(rs2.next()) {
                    fields.add(rs2.getString("Field"));
                    types.add(rs2.getString("Type"));
                }
                result.addProperty("tableName", table);
                result.add("fields", fields);
                result.add("types", types);
                finalResult.add(Integer.toString(i + 1), result);
                System.out.println(result.toString()); // DEBUGGING
                statement2.close();
                rs2.close();
            }
            finalResult.addProperty("numTables", i);
            out.write(finalResult.toString());
            rs.close();
            statement.close();
            out.close();
        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // for more detail
            System.out.println(e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();
    }
}
