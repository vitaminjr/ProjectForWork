package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 15.09.16.
 */
public class InventoryAction {
    long inventoryActionId;
    long inventoryId;
    long articleId;
    float quantity;
    String barcode;
    String date;
    String time;
    int actionTypeId;
    Article article;


    public InventoryAction(){
        clear();
    }

    public long getInventoryActionId() {
        return inventoryActionId;
    }

    public void setInventoryActionId(long inventoryActionId) {
        this.inventoryActionId = inventoryActionId;
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void clear(){
        inventoryActionId = 0;
        inventoryId = 0;
        articleId = 0;
        quantity = 0;
        barcode = "";
        date = "";
        time = "";
        actionTypeId = 0;
        article = new Article();
    }
}
