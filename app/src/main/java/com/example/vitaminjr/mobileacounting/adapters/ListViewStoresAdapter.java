package com.example.vitaminjr.mobileacounting.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vitaminjr.mobileacounting.R;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class ListViewStoresAdapter extends CursorAdapter {
    InventoryViewHolder viewHolder;
    Cursor cursor;

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public ListViewStoresAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stores, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("cursor", cursor.toString());

        viewHolder = (InventoryViewHolder) view.getTag();
        if (viewHolder == null)
            viewHolder = new InventoryViewHolder(view);
        else
            viewHolder = (InventoryViewHolder) view.getTag();
        viewHolder.numbTextView.setText(cursor.getString(cursor.getColumnIndex("name")));
    }

    public static class InventoryViewHolder{
        TextView numbTextView;

        public InventoryViewHolder(View itemView) {

            numbTextView = (TextView) itemView.findViewById(R.id.text_view_name_store);

        }
    }
}
