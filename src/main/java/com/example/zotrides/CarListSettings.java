package com.example.zotrides;

/**
 * This CarListSettings class stores information about the current
 * page's query, sorting order, pagination, and number of results per page.
 */
public class CarListSettings {
    private static final int MAX_CARS = 9581;

    private String query;
    private int pageNumber;
    private int numResultsPerPage;
    private boolean ratingDescend;
    private boolean nameDescend;
    private boolean ratingFirst;

    public CarListSettings(String query, int pageNumber, int numResultsPerPage) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.numResultsPerPage = numResultsPerPage;
        this.ratingDescend = true;
        this.nameDescend = true;
        this.ratingFirst = true;
    }

    /* helper methods */

    /* mutator methods */
    public void setQuery(String newQuery) {this.query = newQuery;}
    //TODO : ERROR MESSAGE IF GO PAST VALID PAGE?
    public void nextPageNumber() {
        if ((pageNumber + 1) * numResultsPerPage > MAX_CARS) {
            return;
        }
        this.pageNumber++;
    }
    public void prevPageNumber() {
        if (pageNumber == 1) {
            return;
        }
        this.pageNumber--;
    }
    public void setNumResultsPerPage(int newNumResults) {this.numResultsPerPage = newNumResults;}

    /* resets everything for a new search / browse result without having to create
    * a completely new object */
    public void reset(String query, int pageNumber, int numResultsPerPage) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.numResultsPerPage = numResultsPerPage;
        this.ratingDescend = true;
        this.nameDescend = true;
        this.ratingFirst = true;
    }

    /* get the query based off of current settings */
    public String toQuery() {
        // determine ordering
        String ordering = "";
        if (ratingFirst) {
            ordering += "ORDER BY rating" + (ratingDescend ? " DESC, " : ", ") +
                    (nameDescend ? "make DESC, model DESC, year DESC" : "make, model, year");
        } else {
            ordering += "ORDER BY " + (nameDescend ? "make DESC, model DESC, year DESC" : "make, model, year")
                    + ", rating" + (ratingDescend ? " DESC, " : "");
        }

        // determine pagination offsets
        String pagination = "";
        if (pageNumber > 1) {
            pagination += "OFFSET " + (pageNumber - 1) * numResultsPerPage;
        }

        return query + ordering + "\n" + pagination + "\n" + "LIMIT 100; ";
    }

    /* check if can use cache */
    public boolean isWithinCache() {
        return false;
    }
/*
    "WITH pickupCarCounts AS (SELECT pickupLocationID, COUNT(DISTINCT carID) as numCars \n" +
            "\tFROM pickup_car_from \n" +
            "    GROUP BY pickupLocationID)\n" +
            "    \n" +
            "SELECT Cars.id as id, group_concat(DISTINCT concat_ws(' ', make, model, year)) as name, \n" +
            "\t\tname as category, price, rating, numVotes,\n" +
            "        group_concat(DISTINCT address ORDER BY numCars DESC, address SEPARATOR ';') as address, \n" +
            "        group_concat(DISTINCT phoneNumber ORDER BY numCars DESC, address SEPARATOR ';') as phoneNumber,\n" +
            "        group_concat(DISTINCT PickupLocation.id ORDER BY numCars DESC, address SEPARATOR ';') as pickupID\n" +
            "FROM category_of_car, Category, Cars, CarPrices, Ratings, pickupCarCounts, pickup_car_from, PickupLocation\n" +
            "WHERE category_of_car.categoryID = Category.id AND category_of_car.carID = Cars.id AND Cars.id = CarPrices.carID" + additional + " \n" +
            "\tAND Ratings.carID = Cars.id AND pickup_car_from.carID = Cars.id AND pickup_car_from.pickupLocationID = pickupCarCounts.pickupLocationID AND pickup_car_from.pickupLocationID = PickupLocation.id\n" +
            "GROUP BY Cars.id\n" */
}
