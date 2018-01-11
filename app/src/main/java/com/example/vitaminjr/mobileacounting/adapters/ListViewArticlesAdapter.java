package com.example.vitaminjr.mobileacounting.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class ListViewArticlesAdapter extends CursorAdapter {
    Cursor cursor;
    Context context;
    boolean toggle = false;


    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public ListViewArticlesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        int invoiceRowId = cursor.getColumnIndex("_id");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item_article, parent, false);


        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = new ViewHolder(view);

        if(holder != null) {
            String code = cursor.getString(cursor.getColumnIndex("article_code"));
            holder.nameTextView.setText(code + " - " + cursor.getString(cursor.getColumnIndex("article_name")));
            holder.codeTextView.setText(cursor.getString(cursor.getColumnIndex("barcode")));
            holder.countTextView.setText(String.format("%.3f", cursor.getFloat(cursor.getColumnIndex("quantity_account"))));
            holder.unitTextView.setText(cursor.getString(cursor.getColumnIndex("article_unit_name")));


            if (toggle == true) {
                if (cursor.getFloat(cursor.getColumnIndex("quantity")) == cursor.getFloat(cursor.getColumnIndex("quantity_account")))
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_green);
                else if (cursor.getFloat(cursor.getColumnIndex("quantity_account")) > cursor.getFloat(cursor.getColumnIndex("quantity")))
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_blue);
                else if (cursor.getInt(cursor.getColumnIndex("type")) == CorrectionType.ctNone.ordinal())
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_yellow);
                else
                    view.setBackgroundResource(R.drawable.rect_without_radius_count_is_red);
            }
        }
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView codeTextView;
        TextView countTextView;
        TextView unitTextView;

        public ViewHolder(View itemView) {
            nameTextView = (TextView) itemView.findViewById(R.id.text_view_name);
            codeTextView = (TextView) itemView.findViewById(R.id.text_view_barcode);
            countTextView = (TextView) itemView.findViewById(R.id.text_view_count);
            unitTextView = (TextView) itemView.findViewById(R.id.text_view_unit);
        }

    }
}
