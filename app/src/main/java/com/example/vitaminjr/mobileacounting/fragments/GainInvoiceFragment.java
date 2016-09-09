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

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.GainInvoiceEditActivity;
import com.example.vitaminjr.mobileacounting.adapters.ListViewAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
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
     OnSomeEventListener onSomaEventListener;
    public Cursor cursor;
    AppCompatActivity activity;
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (AppCompatActivity) activity;
            onSomaEventListener = (OnSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
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
        cursor = SqlQuery.getInvoices(getContext(), invoiceTypeId);
        adapter = new ListViewAdapter(getContext(), cursor,true);
        onSomaEventListener.someEvent(adapter);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getContext(), GainInvoiceEditActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("position", position);
                    intent.putExtra("type", invoiceTypeId);
                    Invoice invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceById(getContext(), id));
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
                    adapter.notifyDataSetChanged();
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
        adapter = new ListViewAdapter(getContext(),SqlQuery.getInvoices(getContext(), invoiceTypeId),true);
        listView.setAdapter(adapter);
    }


}
