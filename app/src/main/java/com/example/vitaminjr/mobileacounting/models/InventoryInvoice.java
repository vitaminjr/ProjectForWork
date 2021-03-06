package com.example.vitaminjr.mobileacounting.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryInvoice {

    private long inventoryId;
    private String inventoryCode;
    private String number;
    private long storeId;
    private String date;
    private int created;
    private String nameStore;


    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }




    public InventoryInvoice(){
        inventoryId = 0;
        inventoryCode = "";
        number = "";
        storeId = 0;
        date = "";
        created = 1;
        nameStore = "";
    }


    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
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

    public void setCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sDate = dateFormat.format(date);
        setDate(sDate);
    }

    public boolean isBackPattern(String date){
        int count = 0;
        try {
            char[] ch = date.toCharArray();

            if(ch[4] == '-'){
                count ++;
            }
            if(ch[7] == '-'){
                count ++;
            }

            if(count == 2)
                return true;
        }catch (Exception ex){
            return false;
        }
        return false;
    }

}
