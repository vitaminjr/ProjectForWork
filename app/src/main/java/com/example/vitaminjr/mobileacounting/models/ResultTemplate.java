package com.example.vitaminjr.mobileacounting.models;

import android.database.Cursor;

/**
 * Created by vitaminjr on 05.09.16.
 */
public class ResultTemplate {

    Cursor cursor;
    float quantity;
    float price;

    public ResultTemplate() {
        this.cursor = null;
        this.quantity = 0;
        this.price = 0;
    }

    public ResultTemplate(Cursor cursor, float quantity, float price) {
        this.cursor = cursor;
        this.quantity = quantity;
        this.price = price;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
