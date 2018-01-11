package com.example.vitaminjr.mobileacounting.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.Preferences;
import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.GainInvoiceEditActivity;
import com.example.vitaminjr.mobileacounting.adapters.ListViewAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class GainInvoiceFragment extends Fragment {
    public ListViewAdapter adapter;
    ListView listView;
    int invoiceTypeId;
    public Cursor cursor = null;

    long idTemp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static GainInvoiceFragment newInstance(int type)
    {
        GainInvoiceFragment gainInvoiceFragment = new GainInvoiceFragment(type);

        return gainInvoiceFragment;
    }

    public GainInvoiceFragment(){
    }


    public GainInvoiceFragment(int invoiceTypeId){
        this.invoiceTypeId = invoiceTypeId;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list_gain, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GainInvoiceEditActivity.class);
                intent.putExtra("type",invoiceTypeId);

                startActivity(intent);

            }
        });
        listView = (ListView) getActivity().findViewById(R.id.listView);
        adapter = new ListViewAdapter(getContext(), setDataFromDBAdapter(), false);
        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), GainInvoiceEditActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("position", position);
                intent.putExtra("type", invoiceTypeId);
                Invoice invoice = null;
                if(invoiceTypeId == InvoiceType.move.ordinal())
                    invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceMoveById(getContext(), id));
                else
                    invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceById(getContext(), id));
                intent.putExtra("created", invoice.getCreated());
                startActivity(intent);
            }
        });


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                idTemp = id;
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete_item, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.delete_item){
                    SqlQuery.deleteInvoice(getContext(),idTemp);
                    Toast.makeText(getContext(),"Успішно видалено!!!",Toast.LENGTH_SHORT).show();
                    onResume();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

            }
        });
    }


    @Override
    public void onResume() {
       super.onResume();
        updateAdapterWithCursor(cursor);

    }

    public Cursor setDataFromDBAdapter(){
        Cursor cursor;

        if(Preferences.loadBooleanSetting("isHideInvoice",getContext()) == false) {

            if(invoiceTypeId == InvoiceType.move.ordinal())
                cursor = SqlQuery.getCursorListInvoicesMove(getContext(), invoiceTypeId);
            else
                cursor = SqlQuery.getCursorListInvoices(getContext(), invoiceTypeId);

        }else {

            if (invoiceTypeId == InvoiceType.move.ordinal())
                cursor = SqlQuery.getCursorListInvoicesMoveWithoutComplete(getContext(), invoiceTypeId);
            else
                cursor = SqlQuery.getCursorListInvoicesWithoutComplete(getContext(), invoiceTypeId);
        }
        
        return cursor;
    }

    public void updateAdapter(){

        adapter.swapCursor(setDataFromDBAdapter());
        adapter.notifyDataSetChanged();
    }

    public void updateAdapterWithCursor(Cursor cursor){

        if(cursor == null)
            updateAdapter();
        else {
            adapter.swapCursor(cursor);
            adapter.notifyDataSetChanged();
            this.cursor = null;
        }

    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
