package edu.uci.ics.zotrides;

public class PickupLocation {
    private final String id;
    private final String address; // includes make/model/year
    private final String phone;

    public PickupLocation(String id, String address, String phone) {
        this.id = id;
        this.address = address;
        this.phone = phone;
    }

    public String getID() {
        return id;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }
}