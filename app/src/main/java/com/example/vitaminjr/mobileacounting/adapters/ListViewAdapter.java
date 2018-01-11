package com.example.vitaminjr.mobileacounting.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vitaminjr.mobileacounting.Preferences;
import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.helpers.ReverseDate;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class ListViewAdapter extends CursorAdapter{
    Cursor cursor;
    Context context;
    int idColumnIndex;
    int provColIndex;
    int numbColIndex;
    int dateColIndex;
    int createColIndex;

    public ListViewAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);

        idColumnIndex = cursor.getColumnIndex("_id");
        provColIndex = cursor.getColumnIndex("provider_name");
        numbColIndex = cursor.getColumnIndex("number");
        dateColIndex = cursor.getColumnIndex("date_d");
        createColIndex = cursor.getColumnIndex("created");

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = new ViewHolder(view);
        if(holder != null) {

            holder.numbTextView.setText(cursor.getString(numbColIndex));
            holder.provTextView.setText(cursor.getString(provColIndex));
            holder.dateTextView.setText(ReverseDate.getDate(cursor.getString(dateColIndex)));

            if (cursor.getInt(createColIndex) == CreateType.pc.ordinal()) {

                Cursor cursorArticles = SqlQuery.getSumInvoiceItem(context, (int) getItemId(cursor.getPosition()));

                cursorArticles.moveToFirst();

                int quantityColIndex = cursorArticles.getColumnIndex("sum_quantity");
                int quantityAccountColIndex = cursorArticles.getColumnIndex("sum_quantity_account");
                int typeIndex = cursorArticles.getColumnIndex("type");

                if (cursorArticles.getInt(typeIndex) == CorrectionType.ctNone.ordinal()) {
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_yellow);
                }else if (cursorArticles.getFloat(quantityColIndex) != cursorArticles.getFloat(quantityAccountColIndex)) {
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_red);
                }else
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_green);
            } else
                view.setBackgroundResource(R.drawable.rect_without_radius_count_is_white);
        }
    }

    static class ViewHolder {
        TextView numbTextView;
        TextView provTextView;
        TextView dateTextView;

        public ViewHolder(View itemView) {
            numbTextView = (TextView) itemView.findViewById(R.id.text_view_number);
            provTextView = (TextView) itemView.findViewById(R.id.text_view_provider);
            dateTextView = (TextView) itemView.findViewById(R.id.text_view_date);
        }

    }


}