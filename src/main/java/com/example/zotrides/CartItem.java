package com.example.zotrides;

import java.time.LocalDate;
/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class CartItem {

    private final String carName;
    private final String carID;
    private final String pickupLocation;
    private final String saleDate;
    private String startDate;
    private String endDate;
    private int quantity;

    public CartItem(String carName, String carID, String pickupLocation, String startDate, String endDate, int quantity) {
        this.carName = carName;
        this.carID = carID;
        this.pickupLocation = pickupLocation;
        this.saleDate = LocalDate.now().toString(); // TODO: verify that this works
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
    }

    public String toQuery(String customerID) {
        return "INSERT INTO Reservations(startDate, endDate, saleDate, customerID, carID) VALUES('" +
                startDate + "', '" + endDate + "', '" + saleDate + "', '" + customerID + "', '" + carID + "');";
    }

}
