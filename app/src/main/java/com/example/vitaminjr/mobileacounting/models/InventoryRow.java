package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 19.09.16.
 */
public class InventoryRow {

    private long inventoryRowId;
    private long inventoryId;
    private long articleId;
    private float quantityAccount;


    public InventoryRow(){
        inventoryRowId = 0;
        inventoryId = 0;
        articleId = 0;
        quantityAccount = 0;
    }

    public long getInventoryRowId() {
        return inventoryRowId;
    }

    public void setInventoryRowId(long inventoryRowId) {
        this.inventoryRowId = inventoryRowId;
    }

    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public float getQuantityAccount() {
        return quantityAccount;
    }

    public void setQuantityAccount(float quantityAccount) {
        this.quantityAccount = quantityAccount;
    }
}
