package com.example.zotrides;

import java.time.LocalDate;
/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class CartItem {
    private static int currentID = 0;

    private final String carName;
    private final String carID;
    private final String pickupLocation;
    private final String saleDate;
    private final int unitPrice;
    private final int itemID;
    private String startDate;
    private String endDate;

    public CartItem(String carName, String carID, String pickupLocation, String startDate, String endDate, int unitPrice) {
        this.carName = carName;
        this.carID = carID;
        this.pickupLocation = pickupLocation;
        this.saleDate = LocalDate.now().toString(); // TODO: verify that this works
        this.startDate = startDate;
        this.endDate = endDate;
        this.unitPrice = unitPrice;
        this.itemID = currentID++; // TODO: verify that ID is changing for each item
    }

    //TODO: IMPLEMENT DATE QUANTITY + PRICE CALCULATION?

    /* accessor methods */
    public String getCarName() {return carName;}
    public String getPickupLocation() {return pickupLocation;}
    public String getStartDate() {return startDate;}
    public String getEndDate() {return endDate;}
    public int getUnitPrice() {return unitPrice;}
    public int getItemID() {return itemID;}

    /* mutator methods */
    public void setStartDate(String date) {this.startDate = date;}
    public void setEndDate(String date) {this.endDate = date;}

    public String toQuery(String customerID, int saleID) {
        return "('" + startDate + "', '" + endDate + "', '" + customerID + "', '" + carID + "', '" + saleDate + "', '" + saleID + "')";
    }

}
