package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.models.InventoryRow;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;

import java.util.List;

/**
 * Created by vitaminjr on 25.10.16.
 */

public class PartInvoiceActivity extends AppCompatActivity {

    private Button dateButton;
    private EditText numberText;
    private TextView codeEDRPOUText;
    private Button buttonChoiceProvider;
    private TextView textInvoice;
    private TextView textTypeProvider;
    Invoice invoice;
    String numberinvoice;
    List<InvoiceRow> invoiceRowsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);

        invoice = getIntent().getParcelableExtra("invoice");

        initGui();

    }
    public void initGui(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        dateButton = (Button) findViewById(R.id.button_edit_date);
        numberText = (EditText) findViewById(R.id.text_edit_number);
        textInvoice = (TextView) findViewById(R.id.text_invoice);
        textTypeProvider = (TextView) findViewById(R.id.text_type_provider);
        codeEDRPOUText = (TextView) findViewById(R.id.text_view_code_edrpou);
        buttonChoiceProvider = (Button) findViewById(R.id.name_provider);



        numberinvoice = invoice.getNumberInvoice();
        dateButton.setText(invoice.getDateCreateInvoice());
        codeEDRPOUText.setText(invoice.getCodeEDRPOU());
        buttonChoiceProvider.setText(invoice.getNameProvider());


        Cursor cursor = SqlQuery.getListArticle(getApplicationContext(),invoice.getInvoiceId());
        invoiceRowsList = SqlQuery.articleListFromCursor(cursor);




        if(invoice.getInvoiceTypeId() == InvoiceType.profit.ordinal()) {
            textInvoice.setText(R.string.gain);
            textTypeProvider.setText(R.string.provider);
        }
        else {
            textInvoice.setText(R.string.expense);
            textTypeProvider.setText(R.string.customer);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_clicked, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if(item.getItemId() == R.id.action_done) {
            if (!(numberText.getText().toString().equals(""))) {
                try {
                    invoice.setNumberInvoice(String.valueOf(numberText.getText()));
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Введіть корректний номер!!!", Toast.LENGTH_SHORT);
                }
                SqlQuery.insertInvoice(this, invoice);

                for (int i = 0; i < invoiceRowsList.size(); i++) {
                    InvoiceRow row = invoiceRowsList.get(i);
                    row.setInvoiceId(invoice.getInvoiceId());
                    SqlQuery.updateInvoiceRow(row, getApplicationContext());
                }
                finish();
            } else
                Toast.makeText(this, "Введіть всі дані перш ніж зберегти", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
