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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
