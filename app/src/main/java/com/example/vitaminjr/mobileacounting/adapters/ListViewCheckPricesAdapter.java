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
 * Created by vitaminjr on 09.09.16.
 */
public class ListViewCheckPricesAdapter extends CursorAdapter {

    InvoiceViewHolder viewHolder;
    Cursor cursor;



    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public ListViewCheckPricesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        viewHolder = (InvoiceViewHolder) view.getTag();
        if (viewHolder == null)
            viewHolder = new InvoiceViewHolder(view);
        else
            viewHolder = (InvoiceViewHolder) view.getTag();

        int invoiceRowId = cursor.getColumnIndex("_id");

        viewHolder.nameTextView.setText(cursor.getString(cursor.getColumnIndex("a.name")));
        viewHolder.codeTextView.setText(cursor.getString(cursor.getColumnIndex("ab.barcode")));
        viewHolder.priceTextView.setText(String.format("%.3f",cursor.getFloat(cursor.getColumnIndex("a.price"))));
        viewHolder.unitTextView.setText(cursor.getString(cursor.getColumnIndex("a.unit_name")));

    }

    public static class InvoiceViewHolder{
        TextView nameTextView;
        TextView codeTextView;
        TextView priceTextView;
        TextView unitTextView;

        public InvoiceViewHolder(View itemView) {

            nameTextView = (TextView) itemView.findViewById(R.id.text_view_name);
            codeTextView = (TextView) itemView.findViewById(R.id.text_view_barcode);
            priceTextView = (TextView) itemView.findViewById(R.id.text_view_count);
            unitTextView = (TextView) itemView.findViewById(R.id.text_view_unit);

        }
    }
}
