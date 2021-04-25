package com.example.zotrides;

/**
 * This SaleMetaInfo class stores meta information about
 * a particular shopping transaction
 */
public class SaleMetaInfo {
    private int saleID; // tracks current sale ID

    public SaleMetaInfo(int saleID) {
        this.saleID = saleID;
    }

    //TODO: IMPLEMENT DATE QUANTITY + PRICE CALCULATION?

    /* accessor methods */
    public int getSaleID() {return this.saleID;}

    /* mutator methods */
    public void nextSaleID() {this.saleID++;}
}
