package com.example.vitaminjr.mobileacounting.fragments;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.ListProviderInvoiceActivity;
import com.example.vitaminjr.mobileacounting.activities.ListStoresActivity;
import com.example.vitaminjr.mobileacounting.activities.PartInvoiceActivity;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.helpers.SetHideNotKeyboard;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;

import java.util.List;

/**`
 * Created by vitaminjr on 12.07.16.
 */
public class GainInvoiceEditFragment extends Fragment implements OnBackPressedListener {

    private Button dateButton;
    private Button dateButtonProvider;
    private Button partButton;
    private EditText numberText;
    private EditText numberTextProvider;
    private Button buttonChoiceProvider;
    private TextView textInvoice;
    private TextView textTypeProvider;
    private TextView textViewSumm;
    private TextView textViewSummQuantity;
    private TextView textViewSummQuantityComplete;
    private TextView textViewSummComplete;

    private long invoiceId;
    private long idProvider;
    private int invoiceTypeId;
    private String dateEditInvoice;
    private String dateEditInvoiceProvider;
    Invoice invoice;

    public static int idInvoiceforActivity = 0;

    private static final int REQUEST_PROVIDER = 1;
    public static final int REQUEST_DATE_INVOICE= 2;
    public static final int REQUEST_DATE_INVOICE_PROVIDER= 3;
    private static final int REQUEST_STORES = 4;
    private Fragment linkFragment;
    private final int DEFAULT_ID_PROVIDER = 0;

    public boolean markSave = false;

     Invoice invoiceCopy;

    public GainInvoiceEditFragment(long invoiceId) {

        this.invoiceId = invoiceId;

    }

    public GainInvoiceEditFragment(int invoiceTypeId){
        invoice = new Invoice();
        this.invoiceTypeId = invoiceTypeId;
        invoice.setInvoiceTypeId(invoiceTypeId);
    }

    public static GainInvoiceEditFragment newInstance(long id)
    {
        GainInvoiceEditFragment gainInvoiceEditFragment = new GainInvoiceEditFragment(id);

        return gainInvoiceEditFragment;
    }

    public static GainInvoiceEditFragment newInstance(int type)
    {
        GainInvoiceEditFragment gainInvoiceEditFragment = new GainInvoiceEditFragment(type);

        return gainInvoiceEditFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        linkFragment = this;
        if (invoiceId != 0)
            invoiceCopy = SqlQuery.getInvoice(SqlQuery.getInvoiceById(getContext(),this.invoiceId));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_gain_invoice, container, false);
        initGui(view);
       if(invoiceId != 0) {
           invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceById(getContext(), invoiceId));
           if (invoice.getCreated() == CreateType.pc.ordinal())
           {
               dateButton.setEnabled(false);
               buttonChoiceProvider.setEnabled(false);
               numberText.setEnabled(false);
           }
       }else {
           invoice.setCurrentDate();
       }

        if(invoice.getCreated() == CreateType.device.ordinal() || invoiceId == 0){
            view.findViewById(R.id.partition).setVisibility(View.GONE);
            view.findViewById(R.id.count_position).setVisibility(View.GONE);
            view.findViewById(R.id.summ).setVisibility(View.GONE);
            view.findViewById(R.id.date_layout).setVisibility(View.GONE);
            view.findViewById(R.id.number_layout).setVisibility(View.GONE);
            idInvoiceforActivity = 0;

            dateButtonProvider.setEnabled(false);
            numberTextProvider.setEnabled(false);
        }

        invoiceTypeId = invoice.getInvoiceTypeId();
        if(invoiceTypeId == InvoiceType.profit.ordinal()) {
            textInvoice.setText(R.string.gain);
            textTypeProvider.setText(R.string.provider);
        }
        else if(invoiceTypeId == InvoiceType.expense.ordinal())  {
            textInvoice.setText(R.string.expense);
            textTypeProvider.setText(R.string.customer);
        }
        else if(invoiceTypeId == InvoiceType.move.ordinal())  {
            textInvoice.setText(R.string.move);
            textTypeProvider.setText(R.string.storage);
            view.findViewById(R.id.partition).setVisibility(View.GONE);
            view.findViewById(R.id.number_layout).setVisibility(View.GONE);
            view.findViewById(R.id.date_layout).setVisibility(View.GONE);
            invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceMoveById(getContext(), invoiceId));
        }

        showInvoice(invoice);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_clicked, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
            saveInvoice();
        return super.onOptionsItemSelected(item);
    }

    public void initGui(View view){

        dateButton = (Button) view.findViewById(R.id.button_edit_date);
        dateButtonProvider = (Button) view.findViewById(R.id.button_edit__date_provider);
        numberText = (EditText) view.findViewById(R.id.text_edit_number);
        numberTextProvider = (EditText) view.findViewById(R.id.text_edit_number_provider);
        textInvoice = (TextView) view.findViewById(R.id.text_invoice);
        textTypeProvider = (TextView) view.findViewById(R.id.text_type_provider);
        numberText.clearFocus();
        partButton = (Button) view.findViewById(R.id.partition);
        buttonChoiceProvider = (Button) view.findViewById(R.id.button_choice_stores);
        textViewSumm = (TextView) view.findViewById(R.id.summ);
        textViewSummQuantity = (TextView) view.findViewById(R.id.count_position);
        textViewSummComplete = (TextView) view.findViewById(R.id.sum_complete);
        textViewSummQuantityComplete = (TextView) view.findViewById(R.id.count_complete);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferences.getBoolean("show_keyboard",false) == false){
            SetHideNotKeyboard hideNumber = new SetHideNotKeyboard(getActivity(),numberText);
            SetHideNotKeyboard hideNumberProvider = new SetHideNotKeyboard(getActivity(),numberTextProvider);
            numberText.setOnTouchListener(hideNumber);
            numberTextProvider.setOnTouchListener(hideNumberProvider);
        }

        initListeners();
    }

    public void initListeners(){
        buttonChoiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(invoiceTypeId == InvoiceType.move.ordinal()) {
                    intent = new Intent(getContext(),ListStoresActivity.class);
                    startActivityForResult(intent,REQUEST_STORES);
                }else {
                    intent = new Intent(getContext(),ListProviderInvoiceActivity.class);
                    intent.putExtra("typeAgent",invoice.getInvoiceTypeId());
                    startActivityForResult(intent,REQUEST_PROVIDER);
                }

            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setTargetFragment(linkFragment,REQUEST_DATE_INVOICE);
                datePicker.show(getFragmentManager(),"datePicker");
            }
        });
        numberText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    invoice.setNumberInvoice(numberText.getText().toString());
                    numberText.setEnabled(false);
                    numberTextProvider.setText(invoice.getNumberInvoice());
                    invoice.setNumberInvoiceProvider(invoice.getNumberInvoice());

                    return true;
                }
                return false;
            }
        });

        partButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = SqlQuery.getListArticle(getContext(),invoice.getInvoiceId());
                List<InvoiceRow> list =  SqlQuery.articleListFromCursor(cursor);
                if(list.size()!= 0) {
                    Invoice newInvoice = new Invoice();
                    newInvoice.setCodeEDRPOU(invoice.getCodeEDRPOU());
                    newInvoice.setDateCreateInvoice(invoice.getDateCreateInvoice());
                    newInvoice.setNameProvider(invoice.getNameProvider());
                    newInvoice.setCreated(invoice.getCreated());
                    newInvoice.setProviderId(invoice.getProviderId());
                    newInvoice.setInvoiceTypeId(invoice.getInvoiceTypeId());
                    newInvoice.setNumberInvoice(invoice.getNumberInvoice());
                    newInvoice.setInvoiceId(invoice.getInvoiceId());
                    Intent intent = new Intent(getContext(), PartInvoiceActivity.class);
                    intent.putExtra("invoice", newInvoice);
                    startActivity(intent);
                }else
                    Toast.makeText(getContext(),"Відсутні пораховані позиції",Toast.LENGTH_SHORT).show();
            }
        });

        dateButtonProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setTargetFragment(linkFragment,REQUEST_DATE_INVOICE_PROVIDER);
                datePicker.show(getFragmentManager(),"datePicker");
            }
        });

        numberTextProvider.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    numberTextProvider.setEnabled(false);
                    invoice.setNumberInvoiceProvider(numberTextProvider.getText().toString());

                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == AppCompatActivity.RESULT_OK)
        {
            switch (requestCode){
                case REQUEST_PROVIDER:
                    invoice.setNameProvider(data.getStringExtra(ListProviderInvoiceActivity.TAG_NAME_PROVIDER));

                    invoice.setCodeEDRPOU(data.getStringExtra(ListProviderInvoiceActivity.TAG_CODE_EDRPOU));
                    invoice.setProviderId(Integer.parseInt(String.valueOf(data.getLongExtra(ListProviderInvoiceActivity.TAG_PROVIDER_ID,-1))));
                    idProvider = data.getLongExtra(ListProviderInvoiceActivity.TAG_PROVIDER_ID,-1);
                    saveNumber();
                    showInvoice(invoice);
                    break;
                case REQUEST_DATE_INVOICE:
                    dateEditInvoice = data.getStringExtra(DatePickerDialogFragment.DATE);
                    invoice.setDateCreateInvoice(dateEditInvoice);
                    invoice.setDateCreateInvoiceProvider(dateEditInvoice);
                    saveNumber();
                    showInvoice(invoice);
                    break;
                case REQUEST_DATE_INVOICE_PROVIDER:
                    dateEditInvoiceProvider = data.getStringExtra(DatePickerDialogFragment.DATE);
                    invoice.setDateCreateInvoiceProvider(dateEditInvoiceProvider);
                    saveNumber();
                    showInvoice(invoice);
                    break;
                case REQUEST_STORES:
                    invoice.setNameProvider(data.getStringExtra(ListStoresActivity.TAG_NAME_STORES));
                    invoice.setProviderId((int)data.getLongExtra(ListStoresActivity.TAG_STORES_ID,0));
                    saveNumber();
                    showInvoice(invoice);
            }
        }
    }

    public void showInvoice(Invoice invoice){

        numberText.setText(String.valueOf(invoice.getNumberInvoice()));
        buttonChoiceProvider.setText(invoice.getNameProvider());
        numberTextProvider.setText(invoice.getNumberInvoiceProvider());


        if(dateEditInvoice != null ){
            dateButton.setText(invoice.getDateCreateInvoice());
            dateButtonProvider.setText(invoice.getDateCreateInvoice());

        }else if (dateEditInvoiceProvider != null){
            dateButtonProvider.setText(invoice.getDateCreateInvoiceProvider());
        }
        else {
            dateButton.setText(invoice.getDateCreateInvoice());
            dateButtonProvider.setText(invoice.getDateCreateInvoiceProvider());
        }
    }


    public void showToast(String nameType){
        Toast.makeText(getContext(), "Накладну" + nameType,Toast.LENGTH_SHORT).show();
    }




    public boolean saveInvoice(){
        if(!(numberText.getText().toString().equals(""))
                && invoice.getProviderId() != 0
                && !dateButton.getText().equals("")) {
            if (invoiceId == 0) {
                invoice.setCreated(CreateType.device.ordinal());
                invoice.setNumberInvoice(numberText.getText().toString());
                invoice.setInvoiceTypeId(invoiceTypeId);
                SqlQuery.insertInvoice(getContext(), invoice);

                showToast(" створено");
                idInvoiceforActivity = invoice.getInvoiceId();
                markSave = true;
                invoiceId = invoice.getInvoiceId();
                invoiceCopy = invoice;
            } else {
                invoice.setInvoiceCode(String.valueOf(invoice.getInvoiceId()));
                invoice.setNumberInvoice(numberText.getText().toString());
                invoice.setInvoiceTypeId(invoiceTypeId);
                    invoice.setNumberInvoiceProvider(numberTextProvider.getText().toString());

                SqlQuery.updateInvoice(getContext(), invoice);
                showToast(" оновлено");
                markSave = true;
            }
        }
        else {
            Toast.makeText(getContext(), "Введіть всі дані перш ніж зберегти", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void saveNumber(){
        if(String.valueOf(numberText.getText()).equals(""))
            Toast.makeText(getContext(),"Введіть код", Toast.LENGTH_SHORT).show();
        else
            invoice.setNumberInvoice(String.valueOf(numberText.getText()));
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
                if(saveInvoice()==true)
                    Toast.makeText(getContext(), "Дані збережено", Toast.LENGTH_LONG).show();
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
        if(invoiceId == 0) {
            if ((!invoice.getNumberInvoice().equals("")
                    || invoice.getProviderId() != DEFAULT_ID_PROVIDER
                    || !invoice.getDateCreateInvoice().equals("")) && markSave == false)
                    onCreateDialog();
            else
                getActivity().finish();
        }
        else
            if ((!invoiceCopy.getNumberInvoice().equals(invoice.getNumberInvoice())
                    || invoiceCopy.getProviderId() != invoice.getProviderId()
                    || !invoiceCopy.getDateCreateInvoice().equals(invoice.getDateCreateInvoice()))
                    && markSave == false)
                    onCreateDialog();

            else
                getActivity().finish();
    }

    public void getSummForInvoice(){
        Cursor cursor = SqlQuery.getSumInvoice(getContext(),invoiceId);
        cursor.moveToFirst();
        float suma = cursor.getFloat(cursor.getColumnIndex("suma"));
        float quantity = cursor.getFloat(cursor.getColumnIndex("sum_quantity"));
        float suma_account = cursor.getFloat(cursor.getColumnIndex("suma_account"));
        float quantity_account = cursor.getFloat(cursor.getColumnIndex("sum_quantity_account"));
        textViewSumm.setText(String.format("%.2f",suma).replace(",","."));
        textViewSummQuantity.setText(String.format("%.3f",quantity).replace(",","."));
        textViewSummComplete.setText(String.format("%.2f",suma_account).replace(",","."));
        textViewSummQuantityComplete.setText(String.format("%.3f",quantity_account).replace(",","."));
    }

    @Override
    public void onResume() {
        super.onResume();
        getSummForInvoice();
    }

}

