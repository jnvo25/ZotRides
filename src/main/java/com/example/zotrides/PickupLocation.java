package com.example.zotrides;

public class PickupLocation {
    // static variable for storing number missing tags
    public static int numMissing = 0;

    // private member variables
    private int id;
    private String phone = ""; // default empty
    private String address;

    // default constructor
    public PickupLocation(){
        this.id = -1;
    }

    public PickupLocation(int id, String phone, String address) {
        this.id = id;
        this.phone = phone;
        this.address = address;
    }

    // accessor and mutator methods
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    // check if Pickup Location is consistent with database schema
    public boolean isInconsistent() {
        boolean isInconsistent = false;
        if (address == null) {
            System.out.println("Missing required tag <address>");
            isInconsistent = true;
            ++numMissing;
        }
        if (id == -1) {
            System.out.println("Missing required tag <id>");
            isInconsistent = true;
            ++numMissing;
        }
        return isInconsistent || phone == null || address.isEmpty() || id == -2;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Pickup Location  Details - ");
        sb.append("ID:" + getId());
        sb.append(", ");
        sb.append("Address:" + getAddress());
        sb.append(", ");
        sb.append("Phone:" + getPhone());
        return sb.toString();
    }

    public static int getNumMissing() { return numMissing; }
    public static void resetMissing() { numMissing = 0; }

}

