package com.example.vitaminjr.mobileacounting.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.models.Article;
import com.example.vitaminjr.mobileacounting.models.BarcodeTamplateInfo;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;
import com.example.vitaminjr.mobileacounting.models.Provider;
import com.example.vitaminjr.mobileacounting.models.ResultTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vitaminjr on 08.08.16.
 */
public class SqlQuery {

    public static Cursor getListArticle(Context context, int invoiceId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT ir.*, ir.invoice_row_id AS _id, " +
                "   a.name as article_name, " +
                "   a.unit_name as article_unit_name, " +
                "   a.price as article_price " +
                " FROM invoice_rows ir " +
                "   LEFT JOIN articles a ON (a.article_id = ir.article_id) " +
                " WHERE ir.invoice_id = " + invoiceId +

                " ORDER BY CASE WHEN ir.datetime_change IS NULL THEN 1 ELSE 0 END, ir.datetime_change ", null);

        return c;
    }

    public static Cursor searchArticle(Context context, int invoiceId, String filter) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT ir.*, ir.invoice_row_id AS _id, " +
                "   a.name as article_name, " +
                "   a.unit_name as article_unit_name, " +
                "   a.price as article_price " +
                " FROM invoice_rows ir " +
                "   LEFT JOIN articles a ON (a.article_id = ir.article_id) " +
                " WHERE (ir.invoice_id = " + invoiceId + ") AND (article_name  LIKE \"%" + filter + "%\" " +
                " OR ir.barcode LIKE \"%" + filter + "%\" )" , null);

        return c;
    }




    public static InvoiceRow getArticleFromPosition(Cursor cursor, int position){

        InvoiceRow invoiceArticle = new InvoiceRow();
        if(cursor.moveToPosition(position)) {
            int articleIdColIndex = cursor.getColumnIndex("_id");

            int invoiceRowIdColIndex = cursor.getColumnIndex("invoice_row_id");
            int invoiceSumaAccount = cursor.getColumnIndex("suma_account");
            int nameColIndex = cursor.getColumnIndex("article_name");
            int unitNameColIndex = cursor.getColumnIndex("article_unit_name");
            int barcodeColIndex = cursor.getColumnIndex("barcode");
            int quantityAccountColIndex = cursor.getColumnIndex("quantity_account");
            int priceAccountColIndex = cursor.getColumnIndex("price_account");
            int priceColIndex = cursor.getColumnIndex("price");
            int sumaColIndex = cursor.getColumnIndex("suma");
            int quantityColIndex = cursor.getColumnIndex("quantity");

            invoiceArticle.setInvoiceRowId(cursor.getInt(invoiceRowIdColIndex));
            invoiceArticle.setQuantityAccount(cursor.getFloat(quantityAccountColIndex));
            invoiceArticle.setPriceAccount(cursor.getFloat(priceAccountColIndex));
            invoiceArticle.setSumaAccount(cursor.getFloat(invoiceSumaAccount));
            invoiceArticle.setName(cursor.getString(nameColIndex));
            invoiceArticle.setUnitName(cursor.getString(unitNameColIndex));
            invoiceArticle.setBarcode(cursor.getString(barcodeColIndex));
            invoiceArticle.setQuantity(cursor.getFloat(quantityColIndex));
            invoiceArticle.setPrice(cursor.getFloat(priceColIndex));
            invoiceArticle.setSuma(cursor.getFloat(sumaColIndex));

        }

        return invoiceArticle;
    }

    public static int getInvoiceRowCount(int invoiceId, Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT COUNT(ir.invoice_row_id) AS _id " +
                " FROM invoice_rows ir " +
                " WHERE ir.invoice_id = " + invoiceId, null);

        c.moveToFirst();

        return c.getInt(c.getColumnIndex("_id"));
    }

    public static void updateInvoiceRow(InvoiceRow invoiceRow, Context context)
    {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(" UPDATE invoice_rows " +
                " SET " +
                "   barcode = "+ invoiceRow.getBarcode() + "," +
                "   quantity_account = " + invoiceRow.getQuantityAccount() + "," +
                "   price_account = " + invoiceRow.getPriceAccount() + "," +
                "   suma_account = " + invoiceRow.getSumaAccount() + "," +
                "   correction_type_id = " + invoiceRow.getCorrectionTypeId() + "," +
                "   datetime_change = " + invoiceRow.getDateTimeChange() +
                " WHERE " +
                "   invoice_row_id = " + invoiceRow.getInvoiceRowId() );
        sqLiteDatabase.close();


    }

    public static ResultTemplate getArticleByBarcode(String barcode, Context context, List<BarcodeTamplateInfo> listTamplate) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c;
        float quantity = -1;
        float price = -1;
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setQuantity(quantity);
        resultTemplate.setPrice(price);
        //    try
        //    {
        // Перевірка штрих-коду

        int ebtInner = 3;

        String article;
        String barcodeNew = "";
        String prefiks = "";
        String codeTemplate;
        int codeSize;
        int quantitySize;
        int priceSize;
        boolean isTemplateUse = false;

        for (int k=0;k<listTamplate.size();k++)
        {
            codeSize = 0;
            quantitySize = 0;
            priceSize = 0;
            prefiks = "";

            codeTemplate = listTamplate.get(k).getTamplate();

            String[] code = codeTemplate.split("[A-Z]");


            prefiks = code[0];
            Log.d("prefics",prefiks);

            if(barcode.substring(0,prefiks.length()).equals(prefiks) && barcode.length() >= codeTemplate.length())
            {
                codeSize = 0;
                quantitySize = 0;
                char[] codeTemplateChar = codeTemplate.toUpperCase().toCharArray();

                for (int i = prefiks.length()-1;i<codeTemplate.length();i++)
                {
                    if (codeTemplateChar[i] =='T') codeSize++;
                    if (codeTemplateChar[i] =='M') quantitySize++;
                    if (codeTemplateChar[i] =='S') priceSize++;
                }

                if(codeSize != 0)
                    barcodeNew = barcode.substring(prefiks.length(),prefiks.length() + codeSize);

                if(quantitySize != 0) {
                    if(listTamplate.get(k).getType() == ebtInner)
                        quantity = new BigDecimal((Float.parseFloat(barcode.substring(prefiks.length()
                            + codeSize, prefiks.length() + codeSize + quantitySize))))
                            .setScale(3,RoundingMode.UP).floatValue();
                    else {
                        String s = barcode.substring(prefiks.length()
                                        + codeSize, prefiks.length() + codeSize + quantitySize);
                        quantity = (float) (Float.parseFloat(barcode.substring(prefiks.length()
                                                        + codeSize, prefiks.length() + codeSize + quantitySize)) / 1000.0);
                    }
                }

                if(priceSize != 0)
                        price = new BigDecimal((Float.parseFloat(barcode.substring(prefiks.length()
                                    + codeSize + quantitySize,
                                prefiks.length() + codeSize + quantitySize + priceSize))))
                                .setScale(2, RoundingMode.UP).floatValue();

                String codeTemp = barcodeNew;
                try {
                    codeTemp = String.valueOf(Integer.parseInt(barcodeNew));
                }
                catch (NumberFormatException ex){

                }
                c = db.rawQuery("SELECT a.article_id AS _id, " +
                        "a.price, " +
                        "a.unit_name, " +
                        "a.name, " +
                        "ab.barcode " +
                        " FROM articles a " +
                        " LEFT JOIN article_barcodes ab ON (a.article_id = ab.article_id) " +
                        "WHERE a.code = " + codeTemp + " GROUP BY a.article_id", null);
                if (c.getCount() != 0) {
                    resultTemplate.setCursor(c);
                    resultTemplate.setQuantity(quantity);
                    resultTemplate.setPrice(price);
                    return resultTemplate;
                }

            }

        }

        c = db.rawQuery("SELECT a.article_id AS _id, " +
                "a.price, " +
                "a.unit_name, " +
                "a.name, " +
                "ab.barcode " +
                " FROM article_barcodes ab " +
                " LEFT JOIN articles a ON (a.article_id = ab.article_id) " +
                "WHERE ab.barcode = " + String.valueOf(barcode), null);

        if(c.getCount() == 0){
            c = db.rawQuery("SELECT a.article_id AS _id, " +
                    "a.price, " +
                    "a.unit_name, " +
                    "a.name, " +
                    "ab.barcode " +
                    " FROM articles a " +
                    " LEFT JOIN article_barcodes ab ON (a.article_id = ab.article_id) " +
                    "WHERE a.code = " + String.valueOf(barcode) + " GROUP BY a.article_id", null);

        }

        resultTemplate.setCursor(c);
        resultTemplate.setQuantity(quantity);
        resultTemplate.setPrice(price);
        return resultTemplate;
    }

    public static InvoiceRow getInvoiceRowByArticleId(int articleId, int invoiceId, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT ir.*, " +
                "   a.name as article_name, " +
                "   a.unit_name as article_unit_name, " +
                "   a.price as article_price " +
                " FROM invoice_rows ir " +
                "   LEFT JOIN articles a ON (a.article_id = ir.article_id) " +
                " WHERE ir.invoice_id = " + invoiceId + " AND a.article_id = " + String.valueOf(articleId) +
                " ORDER BY ir.invoice_row_id ", null);

        InvoiceRow invoiceRow = new InvoiceRow();

        if(c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("invoice_id");
            int barcodeColIndex = c.getColumnIndex("barcode");
            int articleIdColIndex = c.getColumnIndex("article_id");
            int quantityColIndex = c.getColumnIndex("quantity");
            int priceColIndex = c.getColumnIndex("price");
            int sumaColIndex = c.getColumnIndex("suma");
            int quantityAccountColIndex = c.getColumnIndex("quantity_account");
            int priceAccountColIndex = c.getColumnIndex("price_account");
            int sumaAccountColIndex = c.getColumnIndex("suma_account");
            int correctionTypeIdColIndex = c.getColumnIndex("correction_type_id");
            int datetimeChangeColIndex = c.getColumnIndex("datetime_change");


            invoiceRow.setName(c.getString(c.getColumnIndex("article_name")));
            invoiceRow.setUnitName(c.getString(c.getColumnIndex("article_unit_name")));
            invoiceRow.setInvoiceId(Integer.parseInt(c.getString(idColIndex)));
            invoiceRow.setBarcode(c.getString(barcodeColIndex));
            invoiceRow.setArticleId(c.getLong(articleIdColIndex));
            invoiceRow.setQuantity(c.getInt(quantityColIndex));
            invoiceRow.setPrice(c.getInt(priceColIndex));
            invoiceRow.setSuma(c.getInt(sumaColIndex));
            invoiceRow.setQuantityAccount(c.getFloat(quantityAccountColIndex));
            invoiceRow.setPriceAccount(c.getFloat(priceAccountColIndex));
            invoiceRow.setSumaAccount(c.getFloat(sumaAccountColIndex));
            invoiceRow.setCorrectionTypeId(c.getInt(correctionTypeIdColIndex));
            invoiceRow.setDateTimeChange(c.getString(datetimeChangeColIndex));
            invoiceRow.setInvoiceRowId(c.getInt(c.getColumnIndex("invoice_row_id")));
        }

                return invoiceRow;
    }

    public static InvoiceRow getArticleInvoice(Cursor cursor){
        InvoiceRow invoiceRow = new InvoiceRow();
        if(cursor.moveToFirst()) {
            int articleIdColIndex = cursor.getColumnIndex("_id");
            int nameColIndex = cursor.getColumnIndex("name");
            int unitNameColIndex = cursor.getColumnIndex("unit_name");
            int barcodeColIndex = cursor.getColumnIndex("barcode");
            int priceColIndex = cursor.getColumnIndex("price");

            do {

                invoiceRow.setArticleId(cursor.getInt(articleIdColIndex));
                invoiceRow.setName(cursor.getString(nameColIndex));
                invoiceRow.setPrice(cursor.getFloat(priceColIndex));
                invoiceRow.setBarcode(cursor.getString(barcodeColIndex));
                invoiceRow.setUnitName(cursor.getString(unitNameColIndex));;
            }
            while (cursor.moveToNext());
        }
        return invoiceRow;
    }




    public static Cursor getInvoices(Context context, int invoiceTypeId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.invoice_id AS _id, " +
                "   inv.number, " +
                "   inv.provider_id, " +
                "   p.name as provider_name, " +
                "   p.code_EDRPOU as provider_code_EDRPOU, " +
                "   inv.date_d, " +
                "   inv.invoice_type_id, " +
                "   inv.created" +
                " FROM invoices inv " +
                "   LEFT JOIN providers p ON (p.provider_id = inv.provider_id) " +
                " WHERE inv.invoice_type_id = " + invoiceTypeId +
                " ORDER BY inv.number ", null);
        return c;
    }


    public static List getListInvoices(Cursor cursor){
        List invoiceList = new ArrayList<>();
        if(cursor.moveToFirst()) {

            int idColIndex = cursor.getColumnIndex("_id");
            int numberColIndex = cursor.getColumnIndex("number");
            int providerIdColIndex = cursor.getColumnIndex("provider_id");
            int nameColIndex = cursor.getColumnIndex("provider_name");
            int codeColIndex = cursor.getColumnIndex("provider_code_EDRPOU");
            int dateColIndex = cursor.getColumnIndex("date_d");
            int typeColIndex = cursor.getColumnIndex("invoice_type_id");
            int createdColIndex = cursor.getColumnIndex("created");

            do {

                Invoice invoice = new Invoice();
                invoice.setInvoiceId(Integer.parseInt(cursor.getString(idColIndex)));
                invoice.setNumberInvoice(cursor.getString(numberColIndex));
                invoice.setNameProvider(cursor.getString(nameColIndex));
                invoice.setCodeEDRPOU(cursor.getString(codeColIndex));
                invoice.setDateCreateInvoice(cursor.getString(dateColIndex));
                invoice.setProviderId(cursor.getInt(providerIdColIndex));
                invoice.setInvoiceTypeId(cursor.getInt(typeColIndex));
                invoice.setCreated(cursor.getInt(createdColIndex));
                invoice.setInvoiceCode(cursor.getString(idColIndex));


                invoiceList.add(invoice);

            }while (cursor.moveToNext());
        }
        return invoiceList;
    }


    public static Cursor getInvoiceById(Context context, long invoiceId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.invoice_id AS _id, " +
                "   inv.number, " +
                "   inv.provider_id, " +
                "   p.name as provider_name, " +
                "   p.code_EDRPOU as provider_code_EDRPOU, " +
                "   inv.date_d, " +
                "   inv.invoice_type_id, " +
                "   inv.created" +
                " FROM invoices inv " +
                "   LEFT JOIN providers p ON (p.provider_id = inv.provider_id) " +
                " WHERE inv.invoice_id = " + invoiceId +
                " ORDER BY inv.number ", null);

        return c;
    }

    public static Cursor searchInvoice(Context context, String filter) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.invoice_id AS _id, " +
                "   inv.number, " +
                "   inv.provider_id, " +
                "   p.name as provider_name, " +
                "   p.code_EDRPOU as provider_code_EDRPOU, " +
                "   inv.date_d, " +
                "   inv.invoice_type_id, " +
                "   inv.created" +
                " FROM invoices inv " +
                "   LEFT JOIN providers p ON (p.provider_id = inv.provider_id) " +
                " WHERE inv.number LIKE \"%" + filter + "%\" OR " +
                "       p.name LIKE \"%" + filter + "%\" OR " +
                "       p.code_EDRPOU LIKE \"%" + filter + "%\" " +
                " ORDER BY inv.number ", null);

        return c;
    }

    public static Cursor getInvoiceById(Context context, int invoiceId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.invoice_id AS _id, " +
                "   inv.number, " +
                "   inv.provider_id, " +
                "   p.name as provider_name, " +
                "   p.code_EDRPOU as provider_code_EDRPOU, " +
                "   inv.date_d, " +
                "   inv.invoice_type_id, " +
                "   inv.created" +
                " FROM invoices inv " +
                "   LEFT JOIN providers p ON (p.provider_id = inv.provider_id) " +
                " WHERE inv.invoice_id = " + invoiceId +
                " ORDER BY inv.number ", null);

        return c;
    }


    public static Invoice getInvoice(Cursor cursor){
        Invoice invoice = new Invoice();
        if(cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("_id");
            int numberColIndex = cursor.getColumnIndex("number");
            int providerIdColIndex = cursor.getColumnIndex("provider_id");
            int nameColIndex = cursor.getColumnIndex("provider_name");
            int codeColIndex = cursor.getColumnIndex("provider_code_EDRPOU");
            int dateColIndex = cursor.getColumnIndex("date_d");
            int typeColIndex = cursor.getColumnIndex("invoice_type_id");
            int createdColIndex = cursor.getColumnIndex("created");

            invoice.setInvoiceId(Integer.parseInt(cursor.getString(idColIndex)));
            invoice.setNumberInvoice(cursor.getString(numberColIndex));
            invoice.setNameProvider(cursor.getString(nameColIndex));
            invoice.setCodeEDRPOU(cursor.getString(codeColIndex));
            invoice.setDateCreateInvoice(cursor.getString(dateColIndex));
            invoice.setProviderId(cursor.getInt(providerIdColIndex));
            invoice.setInvoiceTypeId(cursor.getInt(typeColIndex));
            invoice.setCreated(cursor.getInt(createdColIndex));
            invoice.setInvoiceCode(cursor.getString(idColIndex));
        }
        return invoice;
    }

    public static void insertInvoice(Context context, Invoice invoice) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",invoice.getNumberInvoice());
        values.put("provider_id",invoice.getProviderId());
        values.put("date_d",invoice.getDateCreateInvoice());
        values.put("invoice_type_id",invoice.getInvoiceTypeId() );
        values.put("created",invoice.getCreated());



        invoice.setInvoiceId( (int) sqLiteDatabase.insert("invoices","",values));
        sqLiteDatabase.close();

    }

    public static void insertInvoiceRow(InvoiceRow invoiceRow, Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("invoice_id",invoiceRow.getInvoiceId());
        values.put("barcode",invoiceRow.getBarcode());
        values.put("article_id",invoiceRow.getArticleId());
        values.put("quantity",invoiceRow.getQuantity());
        values.put("price",invoiceRow.getPrice());
        values.put("suma",invoiceRow.getSuma());
        values.put("quantity_account",invoiceRow.getQuantityAccount());
        values.put("price_account",invoiceRow.getPriceAccount());
        values.put("suma_account",invoiceRow.getSumaAccount());
        values.put("correction_type_id",invoiceRow.getCorrectionTypeId());

        invoiceRow.setInvoiceRowId((int)sqLiteDatabase.insert("invoice_rows","",values));


        sqLiteDatabase.close();
        dbHelper.close();
    }

    public static void updateInvoice(Context context, Invoice invoice){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE invoices SET number = " + invoice.getNumberInvoice() + "," +
                " provider_id = " + invoice.getProviderId() + "," + "date_d = '" + invoice.getDateCreateInvoice() + "', " + "invoice_code = " + invoice.getInvoiceCode() + " WHERE invoice_id = " + invoice.getInvoiceId() + ";");
        sqLiteDatabase.close();

    }

    public static Cursor getProviderById(Context context, long id){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT provider_id AS _id, name, code_EDRPOU " +
                "FROM providers" +
                " WHERE providers.provider_id = " + String.valueOf(id) + ";", null);

        return cursor;
    }

    public static Cursor getListProvider(Context context, int typeAgent){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT provider_id AS _id, name, code_EDRPOU " +
                "FROM providers " +
                "WHERE type_agent = " + typeAgent + ";", null);
        return cursor;
    }

    public static Cursor searchProvider(Context context, int typeAgent, String filter){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT provider_id AS _id, name, code_EDRPOU " +
                "FROM providers " +
                "WHERE (type_agent = " + typeAgent +  ") AND (name LIKE \"%" + filter + "%\" " +
                "OR code_EDRPOU LIKE \"%" + filter + "%\" )" , null);
        return cursor;
    }

    public static void getInvoices(Context context){

        context.deleteDatabase("/sdcard/mobileAcounting/invoices_result.odf");

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        DBHelperResult dbHelperResult = new DBHelperResult(context);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();


        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM invoices",null);

        List<Invoice> invoiceList = new ArrayList<>();

        if(cursor.moveToFirst()) {

            int idColIndex = cursor.getColumnIndex("invoice_id");
            int numberColIndex = cursor.getColumnIndex("number");
            int providerIdColIndex = cursor.getColumnIndex("provider_id");
            int dateColIndex = cursor.getColumnIndex("date_d");
            int typeColIndex = cursor.getColumnIndex("invoice_type_id");
            int createdColIndex = cursor.getColumnIndex("created");

            do {

                Invoice invoice = new Invoice();
                invoice.setInvoiceId(Integer.parseInt(cursor.getString(idColIndex)));
                invoice.setNumberInvoice(cursor.getString(numberColIndex));
                invoice.setDateCreateInvoice(cursor.getString(dateColIndex));
                invoice.setProviderId(cursor.getInt(providerIdColIndex));
                invoice.setInvoiceTypeId(cursor.getInt(typeColIndex));
                invoice.setCreated(cursor.getInt(createdColIndex));
                invoice.setInvoiceCode(cursor.getString(idColIndex));

                invoiceList.add(invoice);

            }while (cursor.moveToNext());
        }

        for (int i = 0; i < invoiceList.size(); i++) {

            sqLiteDatabaseResult.execSQL("INSERT INTO invoices (invoice_code, number, provider_id, date_d, invoice_type_id, created) " +
                    "VALUES ( "+ invoiceList.get(i).getInvoiceCode()+ "," + invoiceList.get(i).getNumberInvoice()
                    + "," + invoiceList.get(i).getProviderId() + "," + "'" + invoiceList.get(i).getDateCreateInvoice() + "'"
                    + "," + invoiceList.get(i).getInvoiceTypeId() + "," + invoiceList.get(i).getCreated() + ");");
        }
    }
    public static void deleteInvoice(Context context, long id){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM invoices WHERE invoice_id = " + id + ";");
    }
    public static void deleteInvoiceRow(Context context, long id){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM invoice_rows WHERE invoice_row_id = " + id + ";");
    }

    public static void getInvoicesRows(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        DBHelperResult dbHelperResult = new DBHelperResult(context);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM invoice_rows",null);

        List<InvoiceRow> invoiceRowList = new ArrayList<>();

        if(cursor.moveToFirst()) {

            int idColIndex = cursor.getColumnIndex("invoice_id");
            int barcodeColIndex = cursor.getColumnIndex("barcode");
            int articleIdColIndex = cursor.getColumnIndex("article_id");
            int quantityColIndex = cursor.getColumnIndex("quantity");
            int priceColIndex = cursor.getColumnIndex("price");
            int sumaColIndex = cursor.getColumnIndex("suma");
            int quantityAccountColIndex = cursor.getColumnIndex("quantity_account");
            int priceAccountColIndex = cursor.getColumnIndex("price_account");
            int sumaAccountColIndex = cursor.getColumnIndex("suma_account");
            int correctionTypeIdColIndex = cursor.getColumnIndex("correction_type_id");
            int datetimeChangeColIndex = cursor.getColumnIndex("datetime_change");

            do {

                InvoiceRow invoiceRow = new InvoiceRow();
                invoiceRow.setInvoiceId(Integer.parseInt(cursor.getString(idColIndex)));
                invoiceRow.setBarcode(cursor.getString(barcodeColIndex));
                invoiceRow.setArticleId(cursor.getLong(articleIdColIndex));
                invoiceRow.setQuantity(cursor.getInt(quantityColIndex));
                invoiceRow.setPrice(cursor.getInt(priceColIndex));
                invoiceRow.setSuma(cursor.getInt(sumaColIndex));
                invoiceRow.setQuantityAccount(cursor.getFloat(quantityAccountColIndex));
                invoiceRow.setPriceAccount(cursor.getFloat(priceAccountColIndex));
                invoiceRow.setSumaAccount(cursor.getFloat(sumaAccountColIndex));
                invoiceRow.setCorrectionTypeId(cursor.getInt(correctionTypeIdColIndex));
                invoiceRow.setDateTimeChange(cursor.getString(datetimeChangeColIndex));




                invoiceRowList.add(invoiceRow);

            }while (cursor.moveToNext());
        }

        for (int i = 0; i < invoiceRowList.size(); i++) {

            sqLiteDatabaseResult.execSQL("INSERT INTO invoice_rows (invoice_id, barcode, article_id, " +
                    "quantity, price, suma, quantity_account, price_account, suma_account, " +
                    "correction_type_id, datetime_change) " +
                    "VALUES ( "+ invoiceRowList.get(i).getInvoiceId()+ "," + invoiceRowList.get(i).getBarcode()
                    + "," + invoiceRowList.get(i).getArticleId() + "," + invoiceRowList.get(i).getQuantity()
                    + "," + invoiceRowList.get(i).getPrice() + "," + invoiceRowList.get(i).getSuma()
                    + "," + invoiceRowList.get(i).getQuantityAccount()+ "," + invoiceRowList.get(i).getPriceAccount()
                    + "," + invoiceRowList.get(i).getSumaAccount() + "," + invoiceRowList.get(i).getCorrectionTypeId()
                    + "," + invoiceRowList.get(i).getDateTimeChange() + ")");
        }

    }

    public static void getInvoiceProviders(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        DBHelperResult dbHelperResult = new DBHelperResult(context);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT p.* " +
                " FROM invoices inv " +
                "      LEFT JOIN providers p ON (p.provider_id = inv.provider_id) " +
                " GROUP BY inv.provider_id"  , null);

        List<Provider> providerInvoiceList = new ArrayList<>();

        if(cursor.moveToFirst()) {

            int providerIdColIndex = cursor.getColumnIndex("provider_code");
            int codeColIndex = cursor.getColumnIndex("code_EDRPOU");
            int nameColIndex = cursor.getColumnIndex("name");
            int typeAgentColIndex = cursor.getColumnIndex("type_agent");
            do {

                Provider provider = new Provider();
                provider.setProviderCode(cursor.getString(providerIdColIndex));
                provider.setName(cursor.getString(nameColIndex));
                provider.setCodeEDRPOU(cursor.getInt(codeColIndex));
                provider.setTypeAgent(cursor.getInt(typeAgentColIndex));


                providerInvoiceList.add(provider);

            }while (cursor.moveToNext());
        }

        for (int i = 0; i < providerInvoiceList.size(); i++) {


            sqLiteDatabaseResult.execSQL("INSERT INTO providers (provider_code, code_EDRPOU, name, type_agent)" +
                    " VALUES ( "+ providerInvoiceList.get(i).getProviderCode()+ "," + providerInvoiceList.get(i).getCodeEDRPOU()
                    + "," + "'" + providerInvoiceList.get(i).getName() + "'" + "," + providerInvoiceList.get(i).getTypeAgent() + ")");
        }
        Toast.makeText(context,"Успішно", Toast.LENGTH_SHORT).show();
    }


    public static void getInvoicesRowTovars(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT a.* " +
                " FROM invoice_rows inv " +
                "      LEFT JOIN articles a ON (a.article_id = inv.article_id) " +
                " GROUP BY a.article_id" , null);

        DBHelperResult dbHelperResult = new DBHelperResult(context);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();


        List<Article> articleList = new ArrayList<>();

        if(cursor.moveToFirst()) {

            int articleCodeColIndex = cursor.getColumnIndex("article_code");
            int codeColIndex = cursor.getColumnIndex("code");
            int nameColIndex = cursor.getColumnIndex("name");
            int unitNameColIndex = cursor.getColumnIndex("unit_name");
            int priceColIndex = cursor.getColumnIndex("price");
            int quantityRemainsColIndex = cursor.getColumnIndex("quantity_remains");

            do {

                Article article = new Article();
                article.setArticleCode(cursor.getString(articleCodeColIndex));
                article.setCode(cursor.getString(codeColIndex));

                if(cursor.getString(codeColIndex)== null)
                    article.setName("");
                else
                    article.setName(cursor.getString(nameColIndex));

                Log.d("name",article.getName());

                article.setUnitName(cursor.getString(unitNameColIndex));
                article.setPrice(cursor.getFloat(priceColIndex));
                article.setQuantityRemains(cursor.getFloat(quantityRemainsColIndex));

                articleList.add(article);

            }while (cursor.moveToNext());
        }

        ContentValues values = new ContentValues();

        for (int i = 0; i < articleList.size(); i++) {

            values.put("article_code",articleList.get(i).getArticleCode());
            values.put("code",articleList.get(i).getCode());
            values.put("name",articleList.get(i).getName());
            values.put("unit_name",articleList.get(i).getUnitName());
            values.put("price",articleList.get(i).getPrice());
            values.put("quantity_remains",articleList.get(i).getQuantityRemains());
            sqLiteDatabaseResult.insert("articles",null,values);

        }
        Toast.makeText(context,"Успішно", Toast.LENGTH_SHORT).show();
    }

    public static List<BarcodeTamplateInfo> getBarcodeTamplates(Context context){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * " +
                " FROM barcode_templates bt " , null);

        List<BarcodeTamplateInfo> listTamplate = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                BarcodeTamplateInfo tamplateInfo = new BarcodeTamplateInfo();
                tamplateInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
                tamplateInfo.setTamplate(cursor.getString(cursor.getColumnIndex("template")));
                listTamplate.add(tamplateInfo);
            }
            while (cursor.moveToNext());
        }

        return listTamplate;
    }

}
