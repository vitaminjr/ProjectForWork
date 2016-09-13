package com.example.vitaminjr.mobileacounting.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryEditFragment extends Fragment {

    TextView textViewNumber;
    Button buttonDate;
    Button buttonStores;
    long inventoryId;
    private Fragment linkFragment;
    private static final int REQUEST_STORES = 1;
    public static final int REQUEST_DATE_INVOICE= 2;
    InventoryInvoice inventoryInvoice;


    public static InventoryEditFragment newInstance() {

        Bundle args = new Bundle();

        InventoryEditFragment fragment = new InventoryEditFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static InventoryEditFragment newInstance(long inventoryId) {

        Bundle args = new Bundle();

        InventoryEditFragment fragment = new InventoryEditFragment(inventoryId);
        fragment.setArguments(args);
        return fragment;
    }


    public InventoryEditFragment(){
        inventoryInvoice = new InventoryInvoice();
    }


    public InventoryEditFragment(long inventoryId){
        this.inventoryId = inventoryId;
        inventoryInvoice = new InventoryInvoice();
        inventoryInvoice.setInventoryId(inventoryId);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        linkFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_inventory,null);
        initGui(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Listeners();
    }

    public void initGui(View view){
        textViewNumber = (TextView) view.findViewById(R.id.text_edit_number);
        buttonDate = (Button) view.findViewById(R.id.button_edit_date);
        buttonStores = (Button) view.findViewById(R.id.button_choice_stores);
    }

    public void Listeners(){

        textViewNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setTargetFragment(linkFragment,REQUEST_DATE_INVOICE);
                datePicker.show(getFragmentManager(),"datePicker");
            }
        });
        buttonStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
