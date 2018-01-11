package com.example.vitaminjr.mobileacounting.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.ReverseDate;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class ListViewInventoryAdapter extends CursorAdapter {
    Cursor cursor;
    TextView numbTextView;
    TextView storeTextView;
    TextView dateTextView;
    Context context;

    public ListViewInventoryAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
            view = inflater.inflate(R.layout.list_item_inventory, parent, false);
            numbTextView = (TextView) view.findViewById(R.id.text_view_number);
            storeTextView = (TextView) view.findViewById(R.id.text_view_stores);
            dateTextView = (TextView) view.findViewById(R.id.text_view_date);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int idColumnIndex = cursor.getColumnIndex("_id");

        if(cursor.getInt(cursor.getColumnIndex("created")) == CreateType.pc.ordinal()){

            Cursor cursorArticles = SqlQuery.getListInventoryAction(context,cursor.getInt(idColumnIndex));
            if(cursorArticles.moveToFirst()) {
                int quantityColIndex = cursorArticles.getColumnIndex("quantity");
                int quantityAccountColIndex = cursorArticles.getColumnIndex("quantity_account");

                do {
                    if (cursorArticles.getFloat(quantityColIndex) != cursorArticles.getFloat(quantityAccountColIndex)) {
                        view.setBackgroundResource(R.drawable.rect_without_radius_count_is_red);
                        break;
                    } else if(cursorArticles.isLast())
                        view.setBackgroundResource(R.drawable.rect_without_radius_count_is_green);

                } while (cursorArticles.moveToNext());
            }
        } else
            view.setBackgroundResource(R.drawable.rect_without_radius_count_is_white);

        numbTextView.setText(cursor.getString(cursor.getColumnIndex("number")));
        storeTextView.setText(cursor.getString(cursor.getColumnIndex("store_name")));
        dateTextView.setText(ReverseDate.getDate(cursor.getString(cursor.getColumnIndex("date_d"))));
    }


    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

}