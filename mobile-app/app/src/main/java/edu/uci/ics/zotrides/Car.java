package edu.uci.ics.zotrides;

public class Car {
    private final String id;
    private final String title; // includes make/model/year
    private final String category;
    private final String pickup1;
    private final String pickup2;
    private final String pickup3;
    private final double rating;

    public Car(String id, String title, String category, String pickup1, String pickup2, String pickup3, double rating) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.pickup1 = pickup1;
        this.pickup2 = pickup2;
        this.pickup3 = pickup3;
        this.rating = rating;
    }

    public String getID() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public String getPickup1() { return pickup1; }
    public String getPickup2() { return pickup2; }
    public String getPickup3() { return pickup3; }
    public double getRating() { return rating; }
}