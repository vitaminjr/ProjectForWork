package com.example.vitaminjr.mobileacounting.helpers;

import java.io.CharArrayReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vitaminjr on 27.03.17.
 */

public class ReverseDate {

    public static String getReverseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormatReverse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try{
            char[] ch = date.toCharArray();
            if (ch[2] == '-' && ch[5] == '-') {
                try {
                    Date date1 = dateFormat.parse(date);
                    date = dateFormatReverse.format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception ex){

        }


        return date;
    }


    public static String getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormatReverse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try{
            char[] ch = date.toCharArray();
            if (ch[4] == '-' && ch[7] == '-') {
                try {
                    Date date1 = dateFormatReverse.parse(date);
                    date = dateFormat.format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception ex){

        }


        return date;
    }


}
