package com.example.vitaminjr.mobileacounting.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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
public class ListViewProviderAdapter extends CursorAdapter {
    InvoiceViewHolder viewHolder;
    Cursor cursor;

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public ListViewProviderAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_provider, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("cursor", cursor.toString());

        viewHolder = (InvoiceViewHolder) view.getTag();
        if (viewHolder == null)
            viewHolder = new InvoiceViewHolder(view);
        else
            viewHolder = (InvoiceViewHolder) view.getTag();
        viewHolder.numbTextView.setText(cursor.getString(cursor.getColumnIndex("name")));
    }

    public static class InvoiceViewHolder{
        TextView numbTextView;

        public InvoiceViewHolder(View itemView) {

            numbTextView = (TextView) itemView.findViewById(R.id.text_view_name_provider);

        }
    }
}
