package com.example.vitaminjr.mobileacounting.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vitaminjr on 08.07.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    Context context;

    public DBHelper(Context context) {
        super(context, "/sdcard/mobileAcounting/mobile_accounting.odf", null, 3);
    }

    public DBHelper(Context context, String name) {
        super(context, name, null, 3);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        SqlQuery.createSql(db, context);

        db.execSQL("CREATE TABLE IF NOT EXISTS article_barcodes ("
                + "article_barcode_id integer PRIMARY KEY AUTOINCREMENT,"
                + "article_id        integer,"
                + "barcode           varchar(20)" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS articles ("
                + "article_id integer PRIMARY KEY AUTOINCREMENT,"
                + "article_code      varchar(50),"
                + "code              varchar(50),"
                + "name              varchar(100),"
                + "unit_name         varchar(20),"
                + "price             float(15,3),"
                + "price_out             float(15,3),"
                + "quantity_remains  float(15,3)" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS barcode_templates ("
                + "barcode_template_id integer PRIMARY KEY AUTOINCREMENT,"
                + "TYPE            integer,"
                + "template          varchar(20) " + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS inventories (   " +
                "inventory_id  integer PRIMARY KEY AUTOINCREMENT,   " +
                "inventory_code  varchar(50),   " +
                "NUMBER          varchar(50),   " +
                "store_id        integer,   " +
                "date_d          integer,   " +
                "created         integer " + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS inventory_action (    " +
                "inventory_action_id  integer PRIMARY KEY AUTOINCREMENT," +
                "inventory_id         integer," +
                "article_id           integer," +
                "barcode              varchar(20)," +
                "quantity             float(15,3)," +
                "date_d               date," +
                "time_t               time," +
                "action_type_id       integer " + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS inventory_rows (" +
                "inventory_row_id  integer PRIMARY KEY AUTOINCREMENT," +
                "inventory_id      integer," +
                "article_id        integer," +
                "quantity_account  float(15,3)" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS invoice_rows (" +
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

        db.execSQL("CREATE TABLE IF NOT EXISTS invoices (" +
                "invoice_id       integer PRIMARY KEY AUTOINCREMENT," +
                "invoice_code     varchar(50)," +
                "number           varchar(50)," +
                "provider_id      integer," +
                "date_d           date," +
                "number_doc           varchar(50)," +
                "date_doc           date," +
                "invoice_type_id  integer," +
                "created          integer DEFAULT 0" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS price_check (" +
                "price_check_id  integer PRIMARY KEY AUTOINCREMENT," +
                "barcode         varchar(20)," +
                "article_id      integer " + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS providers (" +
                "provider_id  integer PRIMARY KEY AUTOINCREMENT," +
                "provider_code  varchar(50)," +
                "code_EDRPOU  integer," +
                "name         varchar(100)," +
                "type_agent   integer DEFAULT 0 " + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS stores (" +
                "store_id  integer PRIMARY KEY AUTOINCREMENT," +
                "store_code  varchar(50)," +
                "name       varchar(50) " + ");");


        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS  articles_index0 ON articles(article_id);");
        db.execSQL("CREATE INDEX IF NOT EXISTS  articles_Index01 ON articles(article_code);");

        db.execSQL("CREATE INDEX IF NOT EXISTS providers_Index01 ON providers(provider_code);");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS providers_index0 ON providers(provider_id);");

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS invoice_rows_Index01 ON invoice_rows(invoice_row_id);");
        db.execSQL("CREATE INDEX IF NOT EXISTS invoice_rows_Index02 ON invoice_rows(invoice_id);");

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS  invoices_Index01 ON invoices(invoice_id);");
        db.execSQL("CREATE INDEX IF NOT EXISTS  invoices_Index02 ON invoices(invoice_code);");

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS inventories_Index0 ON inventories(inventory_id);");
        db.execSQL(" CREATE INDEX IF NOT EXISTS inventories_Index01 ON inventories(inventory_code);");

        db.execSQL("CREATE INDEX inventory_action_Index01 ON inventory_action(inventory_id);");
        db.execSQL("CREATE INDEX inventory_action_Index02 ON inventory_action(article_id);");


        db.execSQL("CREATE INDEX inventory_rows_Index01 ON inventory_rows(inventory_id);");
        db.execSQL("CREATE INDEX inventory_rows_Index02 ON inventory_rows(article_id);");


        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS stores_Index0 ON stores(store_id);");
        db.execSQL(" CREATE INDEX IF NOT EXISTS stores_Index01 ON stores(store_code);");

        ContentValues values = new ContentValues();
        values.put("name", "Постачальник");
        db.insert("providers", null, values);



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



}
