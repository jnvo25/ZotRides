package com.example.zotrides;

import java.time.LocalDate;
/**
 * This CartItem class stores all information about a particular
 * car reservation.
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
        this.saleDate = LocalDate.now().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.unitPrice = unitPrice;
        this.itemID = currentID++;
    }

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
