package com.example.vitaminjr.mobileacounting.models;

import android.hardware.camera2.params.Face;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vitaminjr on 13.07.16.
 */
public class Invoice implements Parcelable {

    private String numberInvoice;
    private String numberInvoiceProvider;
    private String nameProvider;
    private String codeEDRPOU;
    private String dateCreateInvoice;
    private String dateCreateInvoiceProvider;
    private int providerId;
    private String invoiceCode;

    private int invoiceId;
    private int invoiceTypeId;
    private int created;

    public Invoice(){
        numberInvoice = "";
        nameProvider = "";
        codeEDRPOU = "";
        numberInvoiceProvider = "";
        dateCreateInvoice = "";
        dateCreateInvoiceProvider = "";
        invoiceCode = "";
        providerId = 0;
        invoiceTypeId = 0;
        created = 0;
    }


    protected Invoice(Parcel in) {
        invoiceId = in.readInt();
        numberInvoice = in.readString();
        nameProvider = in.readString();
        numberInvoiceProvider = in.readString();
        dateCreateInvoiceProvider = in.readString();
        codeEDRPOU = in.readString();
        dateCreateInvoice = in.readString();
        providerId = in.readInt();
        invoiceTypeId = in.readInt();
        created = in.readInt();
    }


    public String getDateCreateInvoiceProvider() {

        if(dateCreateInvoiceProvider != null) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            SimpleDateFormat dateFormatBack = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            String sDate = dateCreateInvoiceProvider;

            Date date;
            try {
                if(isBackPattern(dateCreateInvoiceProvider) == true){
                    date = dateFormatBack.parse(dateCreateInvoiceProvider);
                }else
                    date = dateFormat.parse(dateCreateInvoiceProvider);

                sDate = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateCreateInvoiceProvider = sDate;
        }

        return dateCreateInvoiceProvider;
    }

    public void setDateCreateInvoiceProvider(String dateCreateInvoiceProvider) {
        this.dateCreateInvoiceProvider = dateCreateInvoiceProvider;
    }

    public String getNumberInvoiceProvider() {
        return numberInvoiceProvider;
    }

    public void setNumberInvoiceProvider(String numberInvoiceProvider) {
        this.numberInvoiceProvider = numberInvoiceProvider;
    }


    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormatBack = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        String sDate = dateCreateInvoice;

       Date date;
        try {
            if(isBackPattern(dateCreateInvoice) == true){
                date = dateFormatBack.parse(dateCreateInvoice);
            }else
                date = dateFormat.parse(dateCreateInvoice);

            sDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateCreateInvoice = sDate;
        return dateCreateInvoice;
    }

    public void setDateCreateInvoice(String dateCreateInvoice) {
        this.dateCreateInvoice = dateCreateInvoice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(invoiceId);
        dest.writeString(numberInvoice);
        dest.writeString(nameProvider);
        dest.writeString(codeEDRPOU);
        dest.writeString(dateCreateInvoice);
        dest.writeInt(providerId);
        dest.writeInt(invoiceTypeId);
        dest.writeInt(created);
    }

    public void setCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sDate = dateFormat.format(date);
        setDateCreateInvoice(sDate);
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
