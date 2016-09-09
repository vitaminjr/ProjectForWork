package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 02.08.16.
 */
public class InvoiceRow extends Article {

    private int invoiceRowId;
    private long invoiceId;
    private float quantity;
    private float price;
    private float suma;
    private String barcode;
    private float quantityAccount;
    private float priceAccount;
    private float sumaAccount;
    private int correctionTypeId;
    private String dateTimeChange;



    public InvoiceRow() {
        clear();
    }



    public int getInvoiceRowId() {
        return invoiceRowId;
    }

    public void setInvoiceRowId(int invoiceRowId) {
        this.invoiceRowId = invoiceRowId;
    }

    public int getCorrectionTypeId() {
        return correctionTypeId;
    }

    public void setCorrectionTypeId(int correctionTypeId) {
        this.correctionTypeId = correctionTypeId;
    }

    public String getDateTimeChange() {
        return dateTimeChange;
    }

    public void setDateTimeChange(String dateTimeChange) {
        this.dateTimeChange = dateTimeChange;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public float getQuantityAccount() {
        return quantityAccount;
    }

    public void setQuantityAccount(float quantityAccount) {
        this.quantityAccount = quantityAccount;
    }

    public float getPriceAccount() {
        return priceAccount;
    }

    public void setPriceAccount(float priceAccount) {
        this.priceAccount = priceAccount;
    }

    public float getSumaAccount() {
        return sumaAccount;
    }

    public void setSumaAccount(float sumaAccount) {
        this.sumaAccount = sumaAccount;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSuma() {
        return suma;
    }

    public void setSuma(float suma) {
        this.suma = suma;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setUnitName(String unitName) {
        super.setUnitName(unitName);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getUnitName() {
        return super.getUnitName();
    }

    public void clear(){
        invoiceRowId = 0;
        invoiceId = 0;
        articleId = 0;
        quantity = 0;
        price = 0;
        suma = 0;
        barcode = "";
        quantityAccount = 0;
        priceAccount = 0;
        sumaAccount = 0;
        correctionTypeId = 0;
        dateTimeChange = "";
    }
}
