package com.example.vitaminjr.mobileacounting.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vitaminjr.mobileacounting.R;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class ListViewAdapter extends CursorAdapter {
    InvoiceViewHolder viewHolder;
    Cursor cursor;

    public ListViewAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        viewHolder = (InvoiceViewHolder) view.getTag();
        if (viewHolder == null)
            viewHolder = new InvoiceViewHolder(view);
        else
            viewHolder = (InvoiceViewHolder) view.getTag();

        int idColumnIndex = cursor.getColumnIndex("_id");

        viewHolder.numbTextView.setText(cursor.getString(cursor.getColumnIndex("number")));
        viewHolder.provTextView.setText(cursor.getString(cursor.getColumnIndex("provider_name")));
        viewHolder.dateTextView.setText(cursor.getString(cursor.getColumnIndex("date_d")));
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public static class InvoiceViewHolder{
        TextView numbTextView;
        TextView provTextView;
        TextView dateTextView;

        public InvoiceViewHolder(View itemView) {

            numbTextView = (TextView) itemView.findViewById(R.id.text_view_number);
            provTextView = (TextView) itemView.findViewById(R.id.text_view_provider);
            dateTextView = (TextView) itemView.findViewById(R.id.text_view_date);
        }
    }
}