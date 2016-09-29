package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 19.09.16.
 */
public class Store {
    private long storeId;
    private long storeCode;
    private String name;

    public Store(){
        storeId = 0;
        storeCode = 0;
        name = "";
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(long storeCode) {
        this.storeCode = storeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
