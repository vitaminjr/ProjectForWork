package com.example.vitaminjr.mobileacounting.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.ExportFile;
import com.example.vitaminjr.mobileacounting.ImportFile;
import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.interfaces.ReturnEventListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MainActivity extends AppCompatActivity implements ReturnEventListener {

    public static final String NAME_INVENTORY_FILE = "inventory_result.odf";
    public static final String NAME_INVOICE_FILE = "invoices_result.odf";
    public static final String NAME_CHECK_PRICES_FILE = "price_check_result.odf";

    private final int GAIN= 1;
    private final int EXPENSE= 2;
    private final int CHECK_PRICES = 3;
    private final int INVENTORY = 4;
    private String urlAddressDownloadFile;
    private String urlAddressUploadFile;
    private String urlInputFile;
    private String urlOutputFile;

    TextView editProfitInvoice;
    TextView editExpenseInvoice;
    TextView editInventory;
    TextView editPriceCheck;
    ImageView exportInvoice;
    ImageView exportInventory;
    ImageView exportPriceCheck;
    FloatingActionButton actionDownloadDB;
    FloatingActionButton actionSettings;
    Context context;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initGui();
    }

    public void initGui(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);

        editProfitInvoice = (TextView) findViewById(R.id.edit_profit_invoice);
        editExpenseInvoice = (TextView) findViewById(R.id.edit_expense_invoice);
        editPriceCheck = (TextView) findViewById(R.id.edit_price_check);
        editInventory = (TextView) findViewById(R.id.review_inventory);
        actionDownloadDB = (FloatingActionButton) findViewById(R.id.button_download);
        actionSettings = (FloatingActionButton) findViewById(R.id.button_settings);
        exportInvoice = (ImageView) findViewById(R.id.button_export_invoice);
        exportInventory = (ImageView) findViewById(R.id.button_export_inventory);
        exportPriceCheck = (ImageView) findViewById(R.id.button_export_price_check);
        initEditClickListeners();
    }

    public void initEditClickListeners(){

        editProfitInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gainIntent = new Intent(getApplicationContext(), GainInvoiceActivity.class);
                startActivity(gainIntent);
            }
        });
        editExpenseInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent expenseIntent = new Intent(getApplicationContext(), ExpenseInvoiceActivity.class);
                startActivity(expenseIntent);
            }
        });
        editPriceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkPricesIntent = new Intent(getApplicationContext(), CheckPricesActivity.class);
                startActivity(checkPricesIntent);
            }
        });
        editInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inventoryIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(inventoryIntent);
            }
        });

        actionDownloadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImportFile importFile = new ImportFile(context, urlAddressDownloadFile,urlInputFile);
                importFile.execute();
            }
        });

        actionSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });

        exportInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExportFile exportFile = new ExportFile(context, urlAddressUploadFile + NAME_INVOICE_FILE , urlOutputFile + NAME_INVOICE_FILE );
                exportFile.execute();
            }
        });
        exportInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExportFile exportFile = new ExportFile(context, urlAddressUploadFile  + NAME_INVENTORY_FILE, urlOutputFile + NAME_INVENTORY_FILE);
                exportFile.execute();
            }
        });
        exportPriceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExportFile exportFile = new ExportFile(context, urlAddressUploadFile + NAME_CHECK_PRICES_FILE, urlOutputFile + NAME_CHECK_PRICES_FILE);
                exportFile.execute();
            }
        });
    }

    public void initDrawer(Toolbar toolbar){
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                //.withHeader(R.drawable.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.gain).withIdentifier(GAIN),
                        new PrimaryDrawerItem().withName(R.string.expense).withIdentifier(EXPENSE),
                        new PrimaryDrawerItem().withName(R.string.check_prices).withIdentifier(CHECK_PRICES),
                        new PrimaryDrawerItem().withName(R.string.inventory).withIdentifier(INVENTORY)
                )

                .withOnDrawerItemClickListener( new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }

                        if(drawerItem != null) {

                            switch (drawerItem.getIdentifier()) {

                                case GAIN:
                                    Intent gainIntent = new Intent(getApplicationContext(), GainInvoiceActivity.class);
                                    startActivity(gainIntent);
                                    break;
                                case EXPENSE:
                                    Intent expenseIntent = new Intent(getApplicationContext(), ExpenseInvoiceActivity.class);
                                    startActivity(expenseIntent);
                                    break;
                                case CHECK_PRICES:
                                    Intent checkPricesIntent = new Intent(getApplicationContext(), CheckPricesActivity.class);
                                    startActivity(checkPricesIntent);
                                    break;
                                case INVENTORY:
                                    Intent inventoryIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                                    startActivity(inventoryIntent);
                                    break;
                                default:
                            }
                        }
                    }
                }).build();
    }

    @Override
    public void returnResult(Boolean result) {
        showToastResult(result);
    }

    public void showToastResult(Boolean result){
        if(result == false){
            Toast.makeText(context,"Невдале завантаження",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context,"Успішно!!!",Toast.LENGTH_SHORT).show();
    }

    public void loadSettings(){

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        urlAddressDownloadFile = preferences.getString("url_download_db","");
        urlAddressUploadFile = preferences.getString("url_upload_db","");
        urlInputFile = preferences.getString("address_input","");
        urlOutputFile = preferences.getString("address_output","");

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }
}
