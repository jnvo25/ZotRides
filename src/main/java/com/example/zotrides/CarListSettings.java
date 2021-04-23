package com.example.zotrides;

import com.google.gson.JsonArray;

/**
 * This CarListSettings class stores information about the current
 * page's query, sorting order, pagination, and number of results per page.
 */
public class CarListSettings {
    /* member variables */
    private String query;
    private int pageNumber;
    private int numResultsPerPage;
    private boolean ratingDescend;
    private boolean nameDescend;
    private boolean ratingFirst;
    private String errorMessage;
    private int maxNumResults;
    private JsonArray cache;

    /* constructor */
    public CarListSettings(String query, int pageNumber, int numResultsPerPage) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.numResultsPerPage = numResultsPerPage;

        // defaults
        this.ratingDescend = false;
        this.nameDescend = false;
        this.ratingFirst = false;
        this.errorMessage = "";
        this.maxNumResults = 0;
        this.cache = null;
    }

    /* helper method
    *  - determine which bracket of 100 queries
    *    to offset by in query */
    private int convertToBase() {
        if (numResultsPerPage == 100) {
            return pageNumber;
        } else {
            return (pageNumber - 1) * numResultsPerPage / 100 + 1;
        }
    }

    /* mutator methods */
    public void setQuery(String newQuery) {this.query = newQuery;}
    public void setOrder(boolean ratingDescend, boolean nameDescend, boolean ratingFirst) {
        this.ratingDescend = ratingDescend;
        this.nameDescend = nameDescend;
        this.ratingFirst = ratingFirst;
        this.errorMessage = "";
    }
    public void setMaxNumResults(int newMax) {
        this.maxNumResults = newMax;
    }
    public void setCache(JsonArray cache) {
        this.cache = cache;
    }

    /* utility methods */

    /* updates how many results there are per page */
    public void setNumResultsPerPage(int newNumResults) {
        this.numResultsPerPage = newNumResults;
        this.pageNumber = 1;
        this.errorMessage = "";
    }

    /* increments page number, returning boolean indicating success */
    public boolean nextPageNumber() {
        if (pageNumber * numResultsPerPage > maxNumResults) {
            this.errorMessage = "Error: reached end. ";
            return false;
        }
        this.errorMessage = "";
        this.pageNumber++;
        return true;
    }

    /* decrements page number, returning boolean indicating success */
    public boolean prevPageNumber() {
        if (pageNumber == 1) {
            this.errorMessage = "Error: already at start. ";
            return false;
        }
        this.errorMessage = "";
        this.pageNumber--;
        return true;
    }

    /* resets everything for a new search / browse result without having to create
    * a completely new object */
    public void reset(String query, int pageNumber, int numResultsPerPage) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.numResultsPerPage = numResultsPerPage;
        this.ratingDescend = false;
        this.nameDescend = false;
        this.ratingFirst = false;
        this.errorMessage = "";
    }

    //TODO : DETERMINE HOW TO DO CACHING

    //TODO : RETURN THE PAGINATION MESSAGE IN A JSONOBJCECT

    //TODO : CHANGE HOW WE DO THE OFFSET

    /* get the query based off of current settings */
    public String toQuery() {
        // determine ordering
        String ordering = "";
        if (ratingFirst) {
            ordering += "ORDER BY rating" + (ratingDescend ? " DESC, " : ", ") +
                    (nameDescend ? "make DESC, model DESC, year DESC" : "make, model, year");
        } else {
            ordering += "ORDER BY " + (nameDescend ? "make DESC, model DESC, year DESC" : "make, model, year")
                    + ", rating" + (ratingDescend ? " DESC" : "");
        }

        // determine pagination offsets
        String pagination = "";
        if (pageNumber > 1) {
            pagination += "\nLIMIT 100\nOFFSET " + (convertToBase() - 1) * 100 + ";";
        }

        return query + ordering + pagination;
    }

    /* get message to display page number (and possible errors) on front-end */
    public String getPaginationMessage() {
        return errorMessage + "Page: " + pageNumber;
    }

    /* access the cache */
    public JsonArray getCache() {
        return cache;
    }

    /* access # of results per page */
    public int getNumResultsPerPage() {
        return numResultsPerPage;
    }

    /* check if can increment using cache */
    public boolean isWithinCache(boolean pageIncr) {
        int cacheRange = 100 / numResultsPerPage;
        int base = pageNumber % cacheRange;
        return numResultsPerPage != 100 && ((pageIncr && base != 0) || (pageIncr == false && base != 1));
    }

    /* returns where to start returning from within cache */
    public int getStartIndex() {
        int cacheRange = 100 / numResultsPerPage;
        int base = pageNumber % cacheRange;
        if (base == 0) {
            return 100 - numResultsPerPage;
        } else {
            return (base - 1) * numResultsPerPage;
        }
    }

    /* returns where to stop returning from within cache */
    public int getEndIndex() {
        int cacheRange = 100 / numResultsPerPage;
        int base = pageNumber % cacheRange;
        if (base == 0) {
            return 100;
        } else {
            return base * numResultsPerPage;
        }
    }
}
