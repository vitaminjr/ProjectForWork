package com.example.vitaminjr.mobileacounting.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.GainInvoiceEditActivity;
import com.example.vitaminjr.mobileacounting.adapters.ListViewCheckPricesAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by vitaminjr on 05.07.16.
 */
public class CheckPricesFragment extends Fragment {

    ListView listCheckPrices;
    public ListViewCheckPricesAdapter adapter;


     public static CheckPricesFragment newInstance() {

        Bundle args = new Bundle();

        CheckPricesFragment fragment = new CheckPricesFragment();
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

        return inflater.inflate(R.layout.fragment_list_price_check, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                CheckPricesEditFragment checkPricesEditFragment = CheckPricesEditFragment.newInstance();
                fragmentTransaction.replace(R.id.fragment_container,checkPricesEditFragment);
                getActivity().findViewById(R.id.layout_header).setVisibility(View.GONE);
                fragmentTransaction.commit();
            }
        });


        listCheckPrices = (ListView) view.findViewById(R.id.list_view_price_check);
        adapter = new ListViewCheckPricesAdapter(getContext(), SqlQuery.getPriceCheckItems(getContext()),false);
        listCheckPrices.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.layout_header).setVisibility(View.VISIBLE);
        adapter.setCursor(SqlQuery.getPriceCheckItems(getContext()));
        adapter.notifyDataSetChanged();
    }
}
