package com.example.vitaminjr.mobileacounting.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vitaminjr on 08.07.16.
 */
public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "/sdcard/mobileAcounting/mobile_accounting.odf", null, 3);
    }

    public DBHelper(Context context, String name) {
        super(context, name, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE article_barcodes ("
                + "article_barcode_id integer PRIMARY KEY AUTOINCREMENT,"
                + "article_id        integer,"
                + "barcode           varchar(20)" + ");");

        db.execSQL("CREATE TABLE articles ("
                + "article_id integer PRIMARY KEY AUTOINCREMENT,"
                + "article_code      varchar(50),"
                + "code              varchar(50),"
                + "name              varchar(100),"
                + "unit_name         varchar(20),"
                + "price             float(15,3),"
                + "quantity_remains  float(15,3)" + ");");

        db.execSQL("CREATE TABLE barcode_templates ("
                + "barcode_template_id integer PRIMARY KEY AUTOINCREMENT,"
                + "TYPE            integer,"
                + "template          varchar(20) " + ");");

        db.execSQL("CREATE TABLE inventories (   " +
                "inventory_id  integer PRIMARY KEY AUTOINCREMENT,   " +
                "inventory_code  varchar(50),   " +
                "NUMBER          varchar(50),   " +
                "store_id        integer,   " +
                "date_d          integer,   " +
                "created         integer " + ");");

        db.execSQL("CREATE TABLE inventory_action (    " +
                "inventory_action_id  integer PRIMARY KEY AUTOINCREMENT," +
                "inventory_id         integer," +
                "article_id           integer," +
                "barcode              varchar(20)," +
                "quantity             float(15,3)," +
                "date_d               date," +
                "time_t               time," +
                "action_type_id       integer " + ");");

        db.execSQL("CREATE TABLE inventory_rows (" +
                "inventory_row_id  integer PRIMARY KEY AUTOINCREMENT," +
                "inventory_id      integer," +
                "article_id        integer," +
                "quantity_account  float(15,3)" + ");");

        db.execSQL("CREATE TABLE invoice_rows (" +
                "invoice_row_id      integer PRIMARY KEY AUTOINCREMENT," +
                "invoice_id          integer," +
                "barcode             varchar(20)," +
                "article_id          integer," +
                "quantity            float(11,3)," +
                "price               float(11,2)," +
                "suma                float(11,2)," +
                "quantity_account    float(11,3)," +
                "price_account       float(11,2)," +
                "suma_account        float(11,2)," +
                "correction_type_id  integer," +
                "datetime_change     datetime " + ");");

        db.execSQL("CREATE TABLE invoices (" +
                "invoice_id       integer PRIMARY KEY AUTOINCREMENT," +
                "invoice_code     varchar(50)," +
                "NUMBER           varchar(50)," +
                "provider_id      integer," +
                "date_d           date," +
                "invoice_type_id  integer," +
                "created          integer DEFAULT 0" + ");");

        db.execSQL("CREATE TABLE price_check (" +
                "price_check_id  integer PRIMARY KEY AUTOINCREMENT," +
                "barcode         varchar(20)," +
                "article_id      integer " + ");");

        db.execSQL("CREATE TABLE providers (" +
                "provider_id  integer PRIMARY KEY AUTOINCREMENT," +
                "provider_code  varchar(50)," +
                "code_EDRPOU  integer," +
                "name         varchar(100)," +
                "type_agent   integer DEFAULT 0 " + ");");

        db.execSQL("CREATE TABLE stores (" +
                "store_id  integer PRIMARY KEY AUTOINCREMENT," +
                "store_code  varchar(50)," +
                "name       varchar(50) " + ");");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
