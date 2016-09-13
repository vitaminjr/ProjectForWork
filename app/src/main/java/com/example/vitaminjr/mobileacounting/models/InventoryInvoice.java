package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryInvoice {

    long inventoryId;
    long inventoryCode;
    String number;
    long storeId;
    String date;
    int created;

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    String nameStore;


    public InventoryInvoice(){
        inventoryId = 0;
        inventoryCode = 0;
        number = "";
        storeId = 0;
        date = "";
        created = 1;
    }


    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public long getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(long inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }
}
