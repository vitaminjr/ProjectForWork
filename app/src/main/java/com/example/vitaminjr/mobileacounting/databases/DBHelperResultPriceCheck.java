package com.example.vitaminjr.mobileacounting.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vitaminjr on 09.09.16.
 */
public class DBHelperResultPriceCheck  extends SQLiteOpenHelper {

    public DBHelperResultPriceCheck(Context context) {
        super(context, "/sdcard/mobileAcounting/price_check_result.odf", null, 3);
    }

    public DBHelperResultPriceCheck(Context context, String name) {
        super(context, name, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE price_check (" +
                "price_check_id  integer PRIMARY KEY AUTOINCREMENT," +
                "barcode         varchar(20)," +
                "article_id      integer " + ");");

        db.execSQL(" CREATE TABLE IF NOT EXISTS articles ( " +
                " article_id        integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " article_code      varchar(50), " +
                " code              varchar(50), " +
                " name              varchar(100), " +
                " unit_name         varchar(20), " +
                " price             float(15,3), " +
                " quantity_remains  float(15,3));");

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS  articles_index0 ON articles(article_id);");
        db.execSQL("CREATE INDEX IF NOT EXISTS  articles_Index01 ON articles(article_code);");
        db.execSQL(" CREATE INDEX IF NOT EXISTS price_check_Index01 ON price_check(article_id);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
