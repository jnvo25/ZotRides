package com.example.zotrides;

public class Mapping {
    public static int numMissing = 0;

    private int carID;
    private int locationID;

    public Mapping(){
        this.locationID = -1;
    }

    public Mapping(int carID, int locationID) {
        this.carID = carID;
        this.locationID = locationID;
    }

    public int getCarID() {return carID;}
    public void setCarID(int carID) {this.carID = carID;}
    public int getLocationID() {return locationID;}
    public void setLocationID(int locationID) {this.locationID = locationID;}

    public boolean isInconsistent() {
        boolean isInconsistent = false;
        if (locationID == -1) {
            System.out.println("Missing required tag <LocationID>");
            isInconsistent = true;
            ++numMissing;
        }
        return isInconsistent || locationID == -2;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Mapping  Details - ");
        sb.append("Car ID:" + getCarID());
        sb.append(", ");
        sb.append("Pickup Location ID:" + getLocationID());
        return sb.toString();
    }

    public static int getNumMissing() { return numMissing; }
    public static void resetMissing() { numMissing = 0; }

}

