package com.example.vitaminjr.mobileacounting.databases;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.activities.RegistrationActivity;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.models.Article;
import com.example.vitaminjr.mobileacounting.models.BarcodeTamplateInfo;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;
import com.example.vitaminjr.mobileacounting.models.InventoryAction;
import com.example.vitaminjr.mobileacounting.models.InventoryRow;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;
import com.example.vitaminjr.mobileacounting.models.Provider;
import com.example.vitaminjr.mobileacounting.models.ResultTemplate;
import com.example.vitaminjr.mobileacounting.models.Store;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitaminjr on 08.08.16.
 */
public class SqlQuery {

    public static Cursor getListArticle(Context context, int invoiceId) {

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT ir.*, ir.invoice_row_id AS _id, " +
                        "   a.name as article_name, " +
                        "   ir.correction_type_id AS type, " +
                        "   a.unit_name as article_unit_name, " +
                        "   a.price as article_price " +
                        " FROM invoice_rows ir " +
                        "   LEFT JOIN articles a ON (a.article_id = ir.article_id) " +
                        " WHERE ir.invoice_id = " + invoiceId

                /*" ORDER BY CASE WHEN ir.datetime_change IS NULL THEN 1 ELSE 0 END, ir.datetime_change "*/, null);

        return c;
    }

    public static List<InvoiceRow> articleListFromCursor(Cursor cursor){
        List<InvoiceRow> list = new ArrayList<>();


        cursor.moveToFirst();
        do {
            if(cursor.getInt(cursor.getColumnIndex("type")) != CorrectionType.ctNone.ordinal()) {
                InvoiceRow invoiceRow = new InvoiceRow();
                invoiceRow.setInvoiceId(cursor.getInt(cursor.getColumnIndex("invoice_id")));
                invoiceRow.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
                invoiceRow.setPriceAccount(cursor.getFloat(cursor.getColumnIndex("price_account")));
                invoiceRow.setQuantity(cursor.getFloat(cursor.getColumnIndex("quantity")));
                invoiceRow.setQuantityAccount(cursor.getFloat(cursor.getColumnIndex("quantity_account")));
                invoiceRow.setSuma(cursor.getFloat(cursor.getColumnIndex("suma")));
                invoiceRow.setSumaAccount(cursor.getFloat(cursor.getColumnIndex("suma_account")));
                invoiceRow.setArticleId(cursor.getInt(cursor.getColumnIndex("article_id")));
                invoiceRow.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
                invoiceRow.setCorrectionTypeId(cursor.getInt(cursor.getColumnIndex("type")));
                invoiceRow.setDateTimeChange(cursor.getString(cursor.getColumnIndex("datetime_change")));
                invoiceRow.setInvoiceRowId(cursor.getInt(cursor.getColumnIndex("_id")));
                list.add(invoiceRow);
            }
        } while (cursor.moveToNext());

        return list;
    }

    public static Cursor searchArticle(Context context, int invoiceId, String filter) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT ir.*, ir.invoice_row_id AS _id, " +
                "   a.name as article_name, " +
                "   ir.correction_type_id AS type, " +
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
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT COUNT(ir.invoice_row_id) AS _id " +
                " FROM invoice_rows ir " +
                " WHERE ir.invoice_id = " + invoiceId, null);

        c.moveToFirst();

        return c.getInt(c.getColumnIndex("_id"));
    }

    public static void updateInvoiceRow(InvoiceRow invoiceRow, Context context)
    {
        String filter = " quantity_account <> 0 AND invoice_id = " + invoiceRow.getInvoiceId();
        if(isCanExecuteQuery(context,"invoice_rows", filter) == true) {
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("barcode", invoiceRow.getBarcode());
            values.put("invoice_id", invoiceRow.getInvoiceId());
            values.put("quantity_account", invoiceRow.getQuantityAccount());
            values.put("price_account", invoiceRow.getPriceAccount());
            values.put("suma_account", invoiceRow.getSumaAccount());
            values.put("correction_type_id", invoiceRow.getCorrectionTypeId());
            values.put("datetime_change", invoiceRow.getDateTimeChange());
            db.update("invoice_rows", values, "invoice_row_id = ?",
                    new String[]{String.valueOf(invoiceRow.getInvoiceRowId())});
            db.close();
        }
    }

    public static ResultTemplate getArticleByBarcode(String barcode, Context context, List<BarcodeTamplateInfo> listTamplate) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();

        Cursor c;
        float quantity = -1;
        float price = -1;
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setQuantity(quantity);
        resultTemplate.setPrice(price);

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
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
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

    public static Cursor getCursorListInvoices(Context context, int invoiceTypeId) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
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
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
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

    public static Cursor searchInvoice(Context context, String filter, int invoiceType) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
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
                " WHERE ( inv.invoice_type_id = " + invoiceType + " ) AND (" +
                "       inv.number LIKE \"%" + filter + "%\" OR " +
                "       p.name LIKE \"%" + filter + "%\" OR " +
                "       p.code_EDRPOU LIKE \"%" + filter + "%\" )" +
                " ORDER BY inv.number ", null);
        return c;
    }

    public static Cursor getInvoiceById(Context context, int invoiceId) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
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
        String filter = " invoice_type_id = " + String.valueOf(invoice.getInvoiceTypeId());
        if(isCanExecuteQuery(context,"invoices",filter) == true) {
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("number", invoice.getNumberInvoice());
            values.put("provider_id", invoice.getProviderId());
            values.put("date_d", invoice.getDateCreateInvoice());
            values.put("invoice_type_id", invoice.getInvoiceTypeId());
            values.put("created", invoice.getCreated());

            invoice.setInvoiceId((int) db.insert("invoices", "", values));

            db.close();
        }
    }

    public static void insertInvoiceRow(InvoiceRow invoiceRow, Context context){
        String filter = " quantity_account <> 0 AND invoice_id = " + String.valueOf(invoiceRow.getInvoiceId());
        if(isCanExecuteQuery(context,"invoice_rows",filter) == true) {
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("invoice_id", invoiceRow.getInvoiceId());
            values.put("barcode", invoiceRow.getBarcode());
            values.put("article_id", invoiceRow.getArticleId());
            values.put("quantity", invoiceRow.getQuantity());
            values.put("price", invoiceRow.getPrice());
            values.put("suma", invoiceRow.getSuma());
            values.put("quantity_account", invoiceRow.getQuantityAccount());
            values.put("price_account", invoiceRow.getPriceAccount());
            values.put("suma_account", invoiceRow.getSumaAccount());
            values.put("correction_type_id", invoiceRow.getCorrectionTypeId());

            invoiceRow.setInvoiceRowId((int) db.insert("invoice_rows", "", values));

            db.close();
        }
    }

    public static void updateInvoice(Context context, Invoice invoice){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("UPDATE invoices SET number = " + invoice.getNumberInvoice() + "," +
                " provider_id = " + invoice.getProviderId()
                + "," + "date_d = '" + invoice.getDateCreateInvoice() + "', " +
                "invoice_code = " + invoice.getInvoiceCode()
                + " WHERE invoice_id = " + invoice.getInvoiceId() + ";");
        db.close();

    }

    public static Cursor getProviderById(Context context, long id){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT provider_id AS _id, name, code_EDRPOU " +
                "FROM providers" +
                " WHERE providers.provider_id = " + String.valueOf(id) + ";", null);

        return cursor;
    }

    public static Cursor getListProvider(Context context, int typeAgent){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT provider_id AS _id, name, code_EDRPOU " +
                "FROM providers " +
                "WHERE type_agent = " + typeAgent +
                " ORDER BY name " + ";", null);
        return cursor;
    }

    public static Cursor searchProvider(Context context, int typeAgent, String filter){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT provider_id AS _id, name, code_EDRPOU " +
                "FROM providers " +
                "WHERE (type_agent = " + typeAgent +  ") AND (name LIKE \"%" + filter + "%\" " +
                "OR code_EDRPOU LIKE \"%" + filter + "%\" )" , null);
        return cursor;
    }

    public static void exportInvoices(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","") + "invoices_result.odf";

        context.deleteDatabase(nameBD);

        DBHelperResult dbHelperResult = new DBHelperResult(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.invoices SELECT * FROM invoices;");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportInvoicesRows(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","") + "invoices_result.odf";
        DBHelperResult dbHelperResult = new DBHelperResult(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.invoice_rows SELECT * FROM invoice_rows;");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportInvoiceProviders(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","") + "invoices_result.odf";
        DBHelperResult dbHelperResult = new DBHelperResult(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.providers SELECT p.* " +
                " FROM invoices inv " +
                "      LEFT JOIN providers p ON (p.provider_id = inv.provider_id) " +
                " GROUP BY inv.provider_id" );
        db.execSQL("DETACH exportBase;");
    }

    public static void exportInvoicesRowTovars(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","") + "invoices_result.odf";
        DBHelperResult dbHelperResult = new DBHelperResult(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.articles SELECT a.* " +
                " FROM invoice_rows inv " +
                "      LEFT JOIN articles a ON (a.article_id = inv.article_id) " +
                " GROUP BY a.article_id" );
        db.execSQL("DETACH exportBase;");
    }

    public static void deleteInvoice(Context context, long id){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("DELETE FROM invoices WHERE invoice_id = " + id + ";");
    }
    public static void deleteInvoiceRow(Context context, long id){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("DELETE FROM invoice_rows WHERE invoice_row_id = " + id + ";");
    }

    public static List<BarcodeTamplateInfo> getBarcodeTemplates(Context context){

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * " +
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

    public static Cursor getPriceCheckItems(Context context){

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        String sqlQuery = " SELECT ab.barcode, a.code, a.name, a.unit_name, a.price, a.article_id AS _id" +
        " FROM price_check pc " +
        "     LEFT JOIN articles a ON (a.article_id = pc.article_id) " +
        "     LEFT JOIN article_barcodes ab ON (ab.article_id = a.article_id) " +
        " GROUP BY pc.article_id ";

        Cursor cursor =  db.rawQuery(sqlQuery,null);

        return cursor;
    }

    public static void clearCheckPrices(Context context){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        String sqlQuery = " DELETE FROM price_check";
        db.execSQL(sqlQuery);
        Toast.makeText(context,"Очищено!!!",Toast.LENGTH_SHORT).show();
    }

    public static void exportPriceCheck(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output", "") + "price_check_result.odf";

        context.deleteDatabase(nameBD);

        DBHelperResultPriceCheck dbHelper = new DBHelperResultPriceCheck(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelper.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.price_check SELECT * FROM price_check;");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportPriceCheckTovars(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","") + "price_check_result.odf";
        DBHelperResultPriceCheck dbHelper = new DBHelperResultPriceCheck(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelper.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL(" INSERT INTO exportBase.articles SELECT a.* " +
                " FROM price_check pc " +
                "     LEFT JOIN articles a ON (a.article_id = pc.article_id) " +
                " GROUP BY pc.article_id " );
        db.execSQL("DETACH exportBase;");
    }

    public static void insertPriceCheck(Context context, InvoiceRow article){
        if(isCanExecuteQuery(context,"price_check") == true) {
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("article_id", article.getArticleId());
            contentValues.put("barcode", article.getBarcode());

            db.insert("price_check", "", contentValues);
        }
    }

    public static Cursor getStores(Context context){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        String sqlQuery = "SELECT store_id AS _id, name FROM stores ORDER BY name";
        Cursor cursor =  db.rawQuery(sqlQuery, null);
        return cursor;
    }

    public static Cursor getStoresById(Context context, long id){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT store_id AS _id, name" +
                " FROM stores" +
                " WHERE store_id = " + String.valueOf(id) + ";", null);

        return cursor;
    }

    public static Cursor searchStores(Context context, String filter){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT store_id AS _id, name " +
                "FROM stores " +
                "WHERE name LIKE \"%" + filter + "%\" ", null);
        return cursor;
    }

    public static Cursor getInventoriesCursor(Context context) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.inventory_id AS _id, " +
                "   inv.number, " +
                "   inv.store_id, " +
                "   s.name as store_name, " +
                "   inv.date_d, " +
                "   inv.created" +
                " FROM inventories inv " +
                "   LEFT JOIN stores s ON (s.store_id = inv.store_id) " +
                " ORDER BY inv.number ", null);
        return c;
    }

    public static InventoryInvoice getInventoriesInvoice(Cursor cursor){
        InventoryInvoice inventoryInvoice = new InventoryInvoice();
        if(cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("_id");
            int numberColIndex = cursor.getColumnIndex("number");
            int storeIdColIndex = cursor.getColumnIndex("store_id");
            int nameColIndex = cursor.getColumnIndex("store_name");
            int dateColIndex = cursor.getColumnIndex("date_d");
            int createdColIndex = cursor.getColumnIndex("created");

            inventoryInvoice.setInventoryId(Integer.parseInt(cursor.getString(idColIndex)));
            inventoryInvoice.setNumber(cursor.getString(numberColIndex));
            inventoryInvoice.setDate(cursor.getString(dateColIndex));
            inventoryInvoice.setStoreId(cursor.getInt(storeIdColIndex));
            inventoryInvoice.setInventoryCode(cursor.getString(idColIndex));
            inventoryInvoice.setCreated(cursor.getInt(createdColIndex));
            inventoryInvoice.setNameStore(cursor.getString(nameColIndex));
        }
        return inventoryInvoice;
    }

    public static Cursor getInventoriesByIdCursor(Context context, long id) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.inventory_id AS _id, " +
                "   inv.number, " +
                "   inv.store_id, " +
                "   s.name as store_name, " +
                "   inv.date_d, " +
                "   inv.created" +
                " FROM inventories inv " +
                "   LEFT JOIN stores s ON (s.store_id = inv.store_id) " +
                "WHERE inv.inventory_id = " + id +
                " ORDER BY inv.number ", null);
        return c;
    }

    public static List getListInventory(Cursor cursor){
        List inventoryList = new ArrayList<>();
        if(cursor.moveToFirst()) {

            int idColIndex = cursor.getColumnIndex("_id");
            int numberColIndex = cursor.getColumnIndex("number");
            int storeIdColIndex = cursor.getColumnIndex("store_id");
            int nameColIndex = cursor.getColumnIndex("store_name");
            int dateColIndex = cursor.getColumnIndex("date_d");
            int createdColIndex = cursor.getColumnIndex("created");

            do {
                InventoryInvoice inventoryInvoice = new InventoryInvoice();
                inventoryInvoice.setInventoryId(Integer.parseInt(cursor.getString(idColIndex)));
                inventoryInvoice.setNumber(cursor.getString(numberColIndex));
                inventoryInvoice.setDate(cursor.getString(dateColIndex));
                inventoryInvoice.setStoreId(cursor.getInt(storeIdColIndex));
                inventoryInvoice.setInventoryCode(cursor.getString(idColIndex));
                inventoryInvoice.setCreated(cursor.getInt(createdColIndex));
                inventoryInvoice.setNameStore(cursor.getString(nameColIndex));

                inventoryList.add(inventoryInvoice);

            }while (cursor.moveToNext());
        }
        return inventoryList;
    }

    public static void insertInventory(Context context, InventoryInvoice inventoryInvoice){
        if(isCanExecuteQuery(context,"inventories") == true) {
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("number", inventoryInvoice.getNumber());
            values.put("date_d", inventoryInvoice.getDate());
            values.put("store_id", inventoryInvoice.getStoreId());
            values.put("created", inventoryInvoice.getCreated());
            inventoryInvoice.setInventoryId(db.insert("inventories", "", values));
        }
    }

    public static void updateInventory(Context context, InventoryInvoice inventoryInvoice){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("inventory_code",inventoryInvoice.getInventoryId());
        values.put("number", inventoryInvoice.getNumber());
        values.put("date_d", inventoryInvoice.getDate());
        values.put("store_id",inventoryInvoice.getStoreId());
        values.put("created",inventoryInvoice.getCreated());
        db.update("inventories", values,"inventory_id = ?",
                new String[] { String.valueOf(inventoryInvoice.getInventoryId())});
    }

    public static InventoryAction getArticle(Cursor cursor){
        InventoryAction inventoryAction = new InventoryAction();
        Article article = new Article();
        if(cursor.moveToFirst()) {
            int articleIdColIndex = cursor.getColumnIndex("_id");
            int nameColIndex = cursor.getColumnIndex("name");
            int unitNameColIndex = cursor.getColumnIndex("unit_name");
            int barcodeColIndex = cursor.getColumnIndex("barcode");

            do {
                article.setArticleId(cursor.getInt(articleIdColIndex));
                article.setName(cursor.getString(nameColIndex));
                article.setUnitName(cursor.getString(unitNameColIndex));
                inventoryAction.setBarcode(cursor.getString(barcodeColIndex));
                inventoryAction.setArticleId(cursor.getInt(articleIdColIndex));
                inventoryAction.setArticle(article);
            }
            while (cursor.moveToNext());
        }
        return inventoryAction;
    }

    public static void insertInventoryAction(InventoryAction inventoryAction, Context context){
        String filter = " inventory_id = " + String.valueOf(inventoryAction.getInventoryId());
        if(isCanExecuteQuery(context,"inventory_action",filter) == true) {
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("inventory_id", inventoryAction.getInventoryId());
            values.put("article_id", inventoryAction.getArticleId());
            values.put("barcode", inventoryAction.getBarcode());
            values.put("quantity", inventoryAction.getQuantity());
            values.put("date_d", inventoryAction.getDate());
            values.put("time_t", inventoryAction.getTime());
            values.put("action_type_id", inventoryAction.getActionTypeId());

            db.insert("inventory_action", "", values);
        }
    }

    public static long getInventoryRowsQuantity(Context context, long inventoryId, long articleId){

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        String query = " SELECT inventory_row_id AS _id, SUM(invr.quantity_account) as quantity_account " +
        " FROM inventory_rows invr " +
        " WHERE invr.inventory_id = " + inventoryId + " AND invr.article_id = " + articleId;
        Cursor cursor = db.rawQuery(query,null);
        long quantityAccount = 0;

        if(cursor.moveToFirst()) {
            int inventoryRowIdColIndex = cursor.getColumnIndex("_id");
            int quantityAccountColIndex = cursor.getColumnIndex("quantity_account");

            quantityAccount = cursor.getLong(quantityAccountColIndex);
            while (cursor.moveToNext());
        }
        return quantityAccount;
    }

    public static long getInventoryActionQuantity(Context context, long inventoryId, long articleId){

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        String query = " SELECT inventory_action_id AS _id, SUM(inva.quantity) as quantity_actual " +
        " FROM inventory_action inva " +
                " WHERE inva.inventory_id = " + inventoryId + " AND inva.article_id = " + articleId;
        Cursor cursor = db.rawQuery(query,null);
        long quantityAccount = 0;

        if(cursor.moveToFirst()) {
            int inventoryRowIdColIndex = cursor.getColumnIndex("_id");
            int quantityAccountColIndex = cursor.getColumnIndex("quantity_actual");

            quantityAccount = cursor.getLong(quantityAccountColIndex);
            while (cursor.moveToNext());
        }
        return quantityAccount;
    }

    public static Cursor getListInventoryAction(Context context, long id) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT inva.inventory_id AS _id, a.article_id, a.code, a.name AS article_name, a.unit_name AS article_unit_name, " +
                " inva.barcode, inva.inventory_action_id, inva.date_d, inva.time_t, inva.action_type_id AS type, " +
                " (SELECT SUM(quantity_account) FROM inventory_rows WHERE inventory_id = inv.inventory_id AND article_id = a.article_id) as quantity, " +
                " (SELECT SUM(quantity) FROM inventory_action WHERE inventory_id = inv.inventory_id AND article_id = a.article_id) as quantity_account " +
                " FROM inventories inv " +
                "    LEFT JOIN inventory_rows invr ON (inv.inventory_id = invr.inventory_id) " +
                "    LEFT JOIN inventory_action inva ON (inv.inventory_id = inva.inventory_id) " +
                "    LEFT JOIN articles a ON (a.article_id = invr.article_id OR a.article_id = inva.article_id) " +
                " WHERE inv.inventory_id = " + id + " AND a.article_id IS NOT NULL " +
                " GROUP BY a.article_id  " +
                " ORDER BY a.name", null);
        return c;
    }

    public static Cursor searchArticleInventory(Context context, long inventoryId, String filter) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT inva.inventory_id AS _id, a.article_id, a.code, a.name AS article_name, a.unit_name AS article_unit_name, " +
                " inva.barcode, inva.inventory_action_id, inva.date_d, inva.time_t, inva.action_type_id AS type, " +
                " (SELECT SUM(quantity_account) FROM inventory_rows WHERE inventory_id = inv.inventory_id AND article_id = a.article_id) as quantity, " +
                " (SELECT SUM(quantity) FROM inventory_action WHERE inventory_id = inv.inventory_id AND article_id = a.article_id) as quantity_account " +
                " FROM inventories inv " +
                "    LEFT JOIN inventory_rows invr ON (inv.inventory_id = invr.inventory_id) " +
                "    LEFT JOIN inventory_action inva ON (inv.inventory_id = inva.inventory_id) " +
                "    LEFT JOIN articles a ON (a.article_id = invr.article_id OR a.article_id = inva.article_id) " +
                " WHERE (inv.inventory_id = " + inventoryId + ") AND (article_name  LIKE \"%" + filter + "%\"" +
                " OR inva.barcode LIKE \"%" + filter + "%\")" +
                " ORDER BY a.name", null);
        return c;
    }

    public static InventoryAction getInventoryActionFromPosition(Cursor cursor, int position){
        InventoryAction action = new InventoryAction();
        if(cursor.moveToPosition(position)) {
            int inventoryIdColIndex = cursor.getColumnIndex("_id");
            int inventoryActionIdIdColIndex = cursor.getColumnIndex("inventory_action_id");
            int articleIdColIndex = cursor.getColumnIndex("article_id");
            int nameColIndex = cursor.getColumnIndex("article_name");
            int unitNameColIndex = cursor.getColumnIndex("article_unit_name");
            int barcodeColIndex = cursor.getColumnIndex("barcode");
            int quantityColIndex = cursor.getColumnIndex("quantity_account");
            int dateColIndex = cursor.getColumnIndex("date_d");
            int timeColIndex = cursor.getColumnIndex("time_t");
            int typeIdColIndex = cursor.getColumnIndex("type");

            action.setInventoryActionId(cursor.getLong(inventoryActionIdIdColIndex));
            action.setInventoryId(cursor.getLong(inventoryIdColIndex));
            action.setArticleId(cursor.getLong(articleIdColIndex));

            Article article = new Article();
            article.setName(cursor.getString(nameColIndex));
            article.setUnitName(cursor.getString(unitNameColIndex));

            action.setBarcode(cursor.getString(barcodeColIndex));
            action.setArticle(article);

            action.setDate(cursor.getString(dateColIndex));
            action.setTime(cursor.getString(timeColIndex));
            action.setActionTypeId(cursor.getInt(typeIdColIndex));

            action.setQuantity(cursor.getFloat(quantityColIndex));
        }
        return action;
    }

    public static void deleteInventoryItem(Context context, long inventoryId, long articleId){
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL(" DELETE FROM inventory_action " +
                " WHERE inventory_id =  " + inventoryId + " AND article_id = " + articleId );
    }

    public static Cursor searchInventory(Context context, String filter) {
        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery("SELECT inv.inventory_id AS _id, " +
                "   inv.number, " +
                "   inv.store_id, " +
                "   s.name as store_name, " +
                "   inv.date_d, " +
                "   inv.created" +
                " FROM inventories inv " +
                "   LEFT JOIN stores s ON (s.store_id = inv.store_id) " +
                " WHERE inv.number LIKE \"%" + filter + "%\" OR " +
                "       s.name  LIKE \"%" + filter + "%\"" +
                " ORDER BY inv.number ", null);

        return c;
    }

    public static void exportInventories(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","")+ "inventory_result.odf";

        context.deleteDatabase(nameBD);

        DBHelperResultInventories dbHelper = new DBHelperResultInventories(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelper.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.inventories SELECT * FROM inventories;");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportInventoryAction(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","")+ "inventory_result.odf";
        DBHelperResultInventories dbHelperResult = new DBHelperResultInventories(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL(" ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.inventory_action SELECT * FROM inventory_action;");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportInventoryRows(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","")+ "inventory_result.odf";
        DBHelperResultInventories dbHelperResult = new DBHelperResultInventories(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL(" ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL("INSERT INTO exportBase.inventory_rows SELECT * FROM inventory_rows;");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportStores(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","")+ "inventory_result.odf";
        DBHelperResultInventories dbHelperResult = new DBHelperResultInventories(context, nameBD);
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL(" ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL(" INSERT INTO exportBase.stores SELECT s.* " +
                " FROM inventories inv " +
                "      LEFT JOIN stores s ON (s.store_id = inv.store_id) " +
                " GROUP BY inv.store_id; ");
        db.execSQL("DETACH exportBase;");
    }

    public static void exportArticlesInventory(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_output","")+ "inventory_result.odf";
        DBHelperResultInventories dbHelperResult = new DBHelperResultInventories(context, nameBD);;
        SQLiteDatabase sqLiteDatabaseResult = dbHelperResult.getWritableDatabase();

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + nameBD + "' AS exportBase;");
        db.execSQL(" INSERT INTO exportBase.articles SELECT a.* " +
                " FROM inventories inv  " +
                "    LEFT JOIN inventory_rows invr ON (inv.inventory_id = invr.inventory_id)  " +
                "    LEFT JOIN inventory_action inva ON (inv.inventory_id = inva.inventory_id)  " +
                "    LEFT JOIN articles a ON (a.article_id = invr.article_id OR a.article_id = inva.article_id)  " +
                " WHERE a.article_id IS NOT NULL " +
                " GROUP BY a.article_id ");
        db.execSQL("DETACH exportBase;");
    }

    public static DBHelper newDBHelper(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nameBD = preferences.getString("address_input","");
        DBHelper dbHelper = new DBHelper(context, nameBD);
        return dbHelper;
    }

    public static Cursor getSumInvoiceItem(Context context, int invoiceId) {

        SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
        Cursor c = db.rawQuery("Select SUM(quantity) as sum_quantity, SUM(quantity_account) AS sum_quantity_account " +
                " FROM invoice_rows ir " +
                " WHERE ir.invoice_id = " + invoiceId , null);

        return c;
    }

    public static boolean isCanExecuteQuery(Context context, String tableName){
        return isCanExecuteQuery(context, tableName, "");
    }
    public static boolean isCanExecuteQuery(Context context, String tableName, String filter){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String codeRegister = preferences.getString(RegistrationActivity.CODE_REGISTER,"");
        if(codeRegister.equals("")){
            String query = "SELECT COUNT(*) AS count FROM " + tableName ;
            if(!filter.equals("")){
                query += " WHERE " + filter;
            }
            SQLiteDatabase db = newDBHelper(context).getWritableDatabase();
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            int count = c.getInt(c.getColumnIndex("count"));
            if(count > 4) {
                Toast.makeText(context,"Демонстраційна версія",Toast.LENGTH_SHORT).show();
                return false;
            }else
                return true;
        }else
        return true;
    }
}