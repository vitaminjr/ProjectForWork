package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 13.07.16.
 */
public class Invoice {

    private String numberInvoice;
    private String nameProvider;
    private String codeEDRPOU;
    private String dateCreateInvoice;
    private int providerId;
    private String invoiceCode;

    private int invoiceId;
    private int invoiceTypeId;
    private int created;

    public Invoice(){
        numberInvoice = "";
        nameProvider = "";
        codeEDRPOU = "";
        dateCreateInvoice = "";
        invoiceCode = "";
        providerId = 0;
        invoiceTypeId = 0;
        created = 0;
    }


    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }



    public int getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(int invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getNumberInvoice() {
        return numberInvoice;
    }

    public void setNumberInvoice(String numberInvoice) {
        this.numberInvoice = numberInvoice;
    }

    public String getNameProvider() {
        return nameProvider;
    }

    public void setNameProvider(String nameProvider) {
        this.nameProvider = nameProvider;
    }

    public String getCodeEDRPOU() {
        return codeEDRPOU;
    }

    public void setCodeEDRPOU(String codeEDRPOU) {
        this.codeEDRPOU = codeEDRPOU;
    }

    public String getDateCreateInvoice() {
        return dateCreateInvoice;
    }

    public void setDateCreateInvoice(String dateCreateInvoice) {
        this.dateCreateInvoice = dateCreateInvoice;
    }
}
