package com.example.vitaminjr.mobileacounting.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vitaminjr on 18.07.16.
 */
public class DatePickerDialogFragment extends DialogFragment {

    int yearInvoice;
    int monthInvoice;
    int dayInvoice;

    public static final String DATE = "date";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        yearInvoice = c.get(Calendar.YEAR);
        monthInvoice = c.get(Calendar.MONTH);
        dayInvoice = c.get(Calendar.DAY_OF_MONTH);
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(getContext(),myCallBack, yearInvoice, monthInvoice, dayInvoice);

        return datePickerDialog;

    }

    android.app.DatePickerDialog.OnDateSetListener myCallBack = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            yearInvoice = year;
            monthInvoice = monthOfYear+1;
            dayInvoice = dayOfMonth;
            Intent intent = new Intent();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();

            String s = dayInvoice + "-" + monthInvoice + "-" + yearInvoice;

            try {
                date = dateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String sDate = dateFormat.format(date);

            intent.putExtra(DATE, sDate);

            getTargetFragment().onActivityResult(getTargetRequestCode(),AppCompatActivity.RESULT_OK,intent);
        }
    };

}
