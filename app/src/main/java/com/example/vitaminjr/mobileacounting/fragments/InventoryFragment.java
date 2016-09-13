package com.example.vitaminjr.mobileacounting.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.GainInvoiceEditActivity;
import com.example.vitaminjr.mobileacounting.activities.InventoryEditActivity;
import com.example.vitaminjr.mobileacounting.adapters.ListViewInventoryAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryFragment extends Fragment {


    ListView listView;
    ListViewInventoryAdapter adapter;


    public static InventoryFragment newInstance() {

        Bundle args = new Bundle();

        InventoryFragment fragment = new InventoryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InventoryEditActivity.class);
                startActivity(intent);

            }
        });

        listView = (ListView) getActivity().findViewById(R.id.listView);
        adapter = new ListViewInventoryAdapter(getContext(), SqlQuery.getInventoriesCursor(getContext()),true);
        //onSomaEventListener.someEvent(adapter);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), InventoryEditActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("position", position);
                InventoryInvoice inventoryInvoice = SqlQuery.getInventoriesInvoice(SqlQuery.getInventoriesByIdCursor(getContext(), id));
                intent.putExtra("created", inventoryInvoice.getCreated());
                startActivity(intent);
            }
        });
    }
}
