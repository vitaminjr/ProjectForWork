package com.example.vitaminjr.mobileacounting.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vitaminjr on 08.07.16.
 */
public class DBHelperResult extends SQLiteOpenHelper {


    public DBHelperResult(Context context) {
        super(context, "/sdcard/mobileAcounting/invoices_result.odf", null, 3);
    }

    public DBHelperResult(Context context, String name) {
        super(context, name, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS invoices ( " +
                " invoice_id       integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " invoice_code     varchar(50), " +
                " number           varchar(50), " +
                " provider_id      integer, " +
                " date_d           date, " +
                " invoice_type_id  integer, " +
                " created          integer DEFAULT 0); ");

        db.execSQL(" CREATE TABLE IF NOT EXISTS invoice_rows ( " +
                " invoice_row_id      integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " invoice_id          integer, " +
                " barcode             varchar(20), " +
                " article_id          integer, " +
                " quantity            float(11,3), " +
                " price               float(11,2), " +
                " suma                float(11,2), " +
                " quantity_account    float(11,3), " +
                " price_account       float(11,2), " +
                " suma_account        float(11,2), " +
                " correction_type_id  integer, " +
                " datetime_change     datetime );");

        db.execSQL(" CREATE TABLE IF NOT EXISTS providers ( " +
                " provider_id  integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " provider_code  varchar(50), " +
                " code_EDRPOU  integer, " +
                " name         varchar(100), " +
                " type_agent   integer DEFAULT 0);");

        db.execSQL(" CREATE TABLE IF NOT EXISTS articles ( " +
                " article_id        integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " article_code      varchar(50), " +
                " code              varchar(50), " +
                " name              varchar(100), " +
                " unit_name         varchar(20), " +
                " price             float(15,3), " +
                " quantity_remains  float(15,3));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
