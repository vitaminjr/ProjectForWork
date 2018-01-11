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
import com.example.vitaminjr.mobileacounting.activities.InventoryEditActivity;
import com.example.vitaminjr.mobileacounting.adapters.ListViewInventoryAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.interfaces.OnEventListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryFragment extends Fragment {


    ListView listView;
    public ListViewInventoryAdapter adapter;
    OnEventListener onSomeEventListener;
    FloatingActionButton floatingActionButton;
    long idTemp;

    public static InventoryFragment newInstance() {

        Bundle args = new Bundle();

        InventoryFragment fragment = new InventoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            onSomeEventListener= (OnEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_inventory, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InventoryEditActivity.class);
                startActivity(intent);

            }
        });

        listView = (ListView) getActivity().findViewById(R.id.listView);
        adapter = new ListViewInventoryAdapter(getContext(), SqlQuery.getInventoriesCursor(getContext()),true);
        onSomeEventListener.someEvent(adapter);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), InventoryEditActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        floatingActionButton.attachToListView(listView);
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
                    SqlQuery.deleteInventories(getContext(),idTemp);
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
        adapter = new ListViewInventoryAdapter(getContext(), SqlQuery.getInventoriesCursor(getContext()),true);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public Cursor setDataFromDBAdapter(){
        Cursor cursor;

        if(Preferences.loadBooleanSetting("isHideInvoice",getContext()) == false)
            cursor = SqlQuery.getInventoriesCursor(getContext());
        else
            cursor = SqlQuery.getInventoriesCursorWithoutComplete(getContext());
        return cursor;
    }
}
