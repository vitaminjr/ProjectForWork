package com.example.vitaminjr.mobileacounting.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vitaminjr on 09.09.16.
 */
public class DBHelperResultInventories extends SQLiteOpenHelper {

    public DBHelperResultInventories(Context context) {
        super(context, "/sdcard/mobileAcounting/inventory_result.odf", null, 3);
    }

    public DBHelperResultInventories(Context context, String name) {
        super(context, name, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

        db.execSQL("CREATE TABLE stores (" +
                "store_id  integer PRIMARY KEY AUTOINCREMENT," +
                "store_code  varchar(50)," +
                "name       varchar(50) " + ");");

        db.execSQL("CREATE TABLE articles ("
                + "article_id integer PRIMARY KEY AUTOINCREMENT,"
                + "article_code      varchar(50),"
                + "code              varchar(50),"
                + "name              varchar(100),"
                + "unit_name         varchar(20),"
                + "price             float(15,3),"
                + "quantity_remains  float(15,3)" + ");");

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS inventories_Index0 ON inventories(inventory_id);");
        db.execSQL(" CREATE INDEX IF NOT EXISTS inventories_Index01 ON inventories(inventory_code);");

        db.execSQL("CREATE INDEX inventory_action_Index01 ON inventory_action(inventory_id);");
        db.execSQL("CREATE INDEX inventory_action_Index02 ON inventory_action(article_id);");


        db.execSQL("CREATE INDEX inventory_rows_Index01 ON inventory_rows(inventory_id);");
        db.execSQL("CREATE INDEX inventory_rows_Index02 ON inventory_rows(article_id);");


        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS stores_Index0 ON stores(store_id);");
        db.execSQL(" CREATE INDEX IF NOT EXISTS stores_Index01 ON stores(store_code);");

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS  articles_index0 ON articles(article_id);");
        db.execSQL("CREATE INDEX IF NOT EXISTS  articles_Index01 ON articles(article_code);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
