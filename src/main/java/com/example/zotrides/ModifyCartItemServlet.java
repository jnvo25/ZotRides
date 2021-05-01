package com.example.zotrides;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This ModifyCartItemServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/modify-cart-item.
 */
@WebServlet(name = "ModifyCartItemServlet", urlPatterns = "/api/modify-cart-item")
public class ModifyCartItemServlet extends HttpServlet {

    /**
     * handles GET requests to view shopping cart (called after modifying dates)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        JsonObject responseJsonObject = new JsonObject();

        ArrayList<CartItem> previousItems = (ArrayList<CartItem>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
        }
        JsonArray previousItemsJsonArray = new JsonArray();

        for (CartItem item : previousItems) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", item.getCarName());
            jsonObject.addProperty("pickupLocation", item.getPickupLocation());
            jsonObject.addProperty("startDate", item.getStartDate());
            jsonObject.addProperty("endDate", item.getEndDate());
            jsonObject.addProperty("unitPrice", item.getUnitPrice());
            jsonObject.addProperty("id", item.getItemID()); // used to uniquely identify cart item
            previousItemsJsonArray.add(jsonObject);
        }

        responseJsonObject.add("previousItems", previousItemsJsonArray);

        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }

    /**
     * handles POST requests to edit shopping cart item quantity
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get elements of item to store into shopping cart
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int id = Integer.parseInt(request.getParameter("itemID"));
//        System.out.println("id: " + id + " dates: " + startDate + " " + endDate);

        HttpSession session = request.getSession();

        // get the previous items in a ArrayList, which must already exist
        ArrayList<CartItem> previousItems = (ArrayList<CartItem>) session.getAttribute("previousItems");

        // prevent corrupted states through sharing under multi-threads
        // will only be executed by one thread at a time
        synchronized (previousItems) {
            for (CartItem item : previousItems) {
                if (item.getItemID() == id) {
                    item.setStartDate(startDate);
                    item.setEndDate(endDate);
                    break;
                }
            }
        }

        // return updated cart
        doGet(request, response);
    }
}
