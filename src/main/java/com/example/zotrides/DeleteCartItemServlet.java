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
 * This DeleteCartItemServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "DeleteCartItemServlet", urlPatterns = "/api/delete-cart-item")
public class DeleteCartItemServlet extends HttpServlet {

    /**
     * handles GET requests to view shopping cart (called after deleting entry)
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
     * handles POST requests to delete item from shopping cart
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get elements of item to store into shopping cart
        int id = Integer.parseInt(request.getParameter("itemID"));
        System.out.println("id: " + id);

        HttpSession session = request.getSession();

        // get the previous items in a ArrayList, which must already exist
        ArrayList<CartItem> previousItems = (ArrayList<CartItem>) session.getAttribute("previousItems");

        // prevent corrupted states through sharing under multi-threads
        // will only be executed by one thread at a time
        synchronized (previousItems) {
            int removeIndex = -1;
            for (int i = 0; i < previousItems.size(); ++i) {
                if (previousItems.get(i).getItemID() == id) {
                    removeIndex = i;
                    break;
                }
            }
            if (removeIndex >= 0 && removeIndex < previousItems.size()) {
                previousItems.remove(removeIndex);
            }
        }

        // return updated cart
        doGet(request, response);
    }
}
