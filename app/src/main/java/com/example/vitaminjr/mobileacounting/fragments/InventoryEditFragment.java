package com.example.vitaminjr.mobileacounting.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.ListStoresActivity;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.SetHideNotKeyboard;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;


/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryEditFragment extends Fragment implements OnBackPressedListener {

    EditText textViewNumber;
    Button buttonDate;
    Button buttonStores;
    long inventoryId;
    private Fragment linkFragment;
    private final int DEFAULT_ID_STORE = 0;
    private static final int REQUEST_STORES = 1;
    public static final int REQUEST_DATE_INVOICE= 2;
    public static long inventoryIdForActivity;
    InventoryInvoice inventoryInvoice;
    private InventoryInvoice inventoryInvoiceCopy = null;
    public boolean markSave = false;

    public static InventoryEditFragment newInstance() {

        InventoryEditFragment fragment = new InventoryEditFragment();
        return fragment;
    }

    public static InventoryEditFragment newInstance(long inventoryId) {

        InventoryEditFragment fragment = new InventoryEditFragment(inventoryId);

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

        if(inventoryId != 0)
        {
            inventoryInvoice = SqlQuery.getInventoriesInvoice(SqlQuery.getInventoriesByIdCursor(getContext(),inventoryId));
            inventoryInvoiceCopy = new InventoryInvoice();
            inventoryInvoiceCopy.setNumber(inventoryInvoice.getNumber());
            inventoryInvoiceCopy.setDate(inventoryInvoice.getDate());
            inventoryInvoiceCopy.setStoreId(inventoryInvoice.getStoreId());
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_inventory,container,false);

        initGui(view);
        Listeners();
        showInventory(inventoryInvoice);


        if(inventoryInvoice.getInventoryId() != 0 && inventoryInvoice.getCreated() == 0){
            textViewNumber.setEnabled(false);
            buttonDate.setEnabled(false);
            buttonStores.setEnabled(false);
        }




        return view;
    }

    public void initGui(View view){
        textViewNumber = (EditText) view.findViewById(R.id.text_edit_number_inventory);
        textViewNumber.clearFocus();
        buttonDate = (Button) view.findViewById(R.id.button_edit_date);
        buttonStores = (Button) view.findViewById(R.id.button_choice_stores);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferences.getBoolean("show_keyboard",false) == false){
            SetHideNotKeyboard hideBarcode = new SetHideNotKeyboard(getActivity(),textViewNumber);
            textViewNumber.setOnTouchListener(hideBarcode);
        }
    }

    public void Listeners(){

        textViewNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    inventoryInvoice.setNumber(textViewNumber.getText().toString());
                    textViewNumber.setEnabled(false);
                    return true;
                }
                    return false;
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryInvoice.setNumber(String.valueOf(textViewNumber.getText()));
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setTargetFragment(linkFragment,REQUEST_DATE_INVOICE);
                datePicker.show(getFragmentManager(),"datePicker");
            }
        });
        buttonStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryInvoice.setNumber(String.valueOf(textViewNumber.getText()));
                Intent intent = new Intent(getContext(),ListStoresActivity.class);
                startActivityForResult(intent,REQUEST_STORES);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == AppCompatActivity.RESULT_OK)
        {
            switch (requestCode){
                case REQUEST_STORES:

                    inventoryInvoice.setNameStore(data.getStringExtra(ListStoresActivity.TAG_NAME_STORES));
                    inventoryInvoice.setStoreId(data.getLongExtra(ListStoresActivity.TAG_STORES_ID,0));

                    showInventory(inventoryInvoice);
                    break;
                case REQUEST_DATE_INVOICE:
                    inventoryInvoice.setDate(data.getStringExtra(DatePickerDialogFragment.DATE));
                    showInventory(inventoryInvoice);
                    break;
            }
        }
    }

    public void showInventory(InventoryInvoice inventoryInvoice){

        textViewNumber.setText(inventoryInvoice.getNumber().toString());
        buttonDate.setText(inventoryInvoice.getDate().toString());
        buttonStores.setText(inventoryInvoice.getNameStore().toString());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_clicked, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(!(textViewNumber.getText().toString().equals("")) && !buttonStores.getText().equals("") && !buttonDate.getText().equals(""))
            saveInvoice();
        else
            Toast.makeText(getContext(),"Введіть всі дані перш ніж зберегти",Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }


    public void saveInvoice(){
        if(inventoryId == 0) {
            inventoryInvoice.setCreated(CreateType.device.ordinal());
            SqlQuery.insertInventory(getContext(),inventoryInvoice);

            showToast("створено");
            inventoryIdForActivity  = inventoryInvoice.getInventoryId();
            markSave = true;
            inventoryId = inventoryInvoice.getInventoryId();
            inventoryInvoiceCopy = inventoryInvoice;
        }
        else
        {

            SqlQuery.updateInventory(getContext(),inventoryInvoice);
            showToast("оновлено");
            markSave = true;
        }
    }

    public void showToast(String nameType){
        Toast.makeText(getContext(), "Накладну " + nameType,Toast.LENGTH_SHORT).show();
    }


    public void onCreateDialog(){
        final String title = "Попередження";
        String message = "Ви не зберегли зміни. Зберегти?";
        String button1String = "Ні";
        String button2String = "Так";
        String button3String = "Відміна";

        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setNegativeButton(button1String,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getContext(), "Дані не збережено",
                        Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
        ad.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getContext(), "Дані збережено", Toast.LENGTH_LONG)
                        .show();

                saveInvoice();
                getActivity().finish();
            }
        });
        ad.setCancelable(true);
        ad.setNeutralButton(button3String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        ad.show();
    }

    @Override
    public void onBackPressed() {
        if(inventoryId == 0) {
            if ((!inventoryInvoice.getNumber().equals("")
                    || inventoryInvoice.getStoreId() != DEFAULT_ID_STORE
                    || !inventoryInvoice.getDate().equals("")) && markSave == false)
                onCreateDialog();
            else
                getActivity().finish();
        }
        else
        if ((!inventoryInvoiceCopy.getNumber().equals(inventoryInvoice.getNumber())
                || inventoryInvoiceCopy.getStoreId() != inventoryInvoice.getStoreId()
                || !inventoryInvoiceCopy.getDate().equals(inventoryInvoice.getDate())) && markSave == false)
            onCreateDialog();

        else
            getActivity().finish();
    }

}
