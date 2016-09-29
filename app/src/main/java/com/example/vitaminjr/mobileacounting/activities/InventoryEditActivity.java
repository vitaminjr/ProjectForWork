package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.InvertoryPagerAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryEditFragment;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryEditActivity extends AppCompatActivity {


    public final static String NUMBER = "numberInventory";
    public final static String ID_INVENTORY = "inventoryId";
    public final static String CREATED = "created";

    private final int EDIT = 1;
    private final int REVISION = 2;
    Button buttonNextView;
    Button buttonPrevView;
    ViewPager inventoryInvoicePager;
    InvertoryPagerAdapter pagerAdapter;
    List<InventoryInvoice> inventoryInvoiceList;
    Intent editArticlesIntent;


    long inventoryId;
    int created;
    String  numberInventory;
    FragmentTransaction fragmentTransaction;
    InventoryEditFragment inventoryEditFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);
        initGui();
    }

    public void initGui(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inventory_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initDrawer(toolbar);

        Intent intent = getIntent();
        inventoryId =  intent.getLongExtra("id",0);
        int position = intent.getIntExtra("position",0);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        initFooterButton();
        if(inventoryId == 0) {
            inventoryEditFragment = InventoryEditFragment.newInstance();
            fragmentTransaction.replace(R.id.add_inventory_container, inventoryEditFragment);
            fragmentTransaction.commit();
        }else {
            inventoryInvoiceList = SqlQuery.getListInventory(SqlQuery.getInventoriesCursor(this));

            initViewPager(position, inventoryInvoiceList);
        }

;
    }

    public void initDrawer(Toolbar toolbar){
        SectionDrawerItem textItem = new SectionDrawerItem().withName(R.string.inventory);
        PrimaryDrawerItem editItem = new PrimaryDrawerItem().withName(R.string.edit).withIdentifier(EDIT);
        PrimaryDrawerItem revisionItem = new PrimaryDrawerItem().withName(R.string.revision).withIdentifier(REVISION);


        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeaderDivider(true)
                //    .withHeader(R.layout.drawer_header)
                .addDrawerItems(textItem, editItem, revisionItem )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if(inventoryInvoiceList!=null) {
                            numberInventory = inventoryInvoiceList.get(inventoryInvoicePager.getCurrentItem()).getNumber();
                            inventoryId = inventoryInvoiceList.get(inventoryInvoicePager.getCurrentItem()).getInventoryId();
                            created = inventoryInvoiceList.get(inventoryInvoicePager.getCurrentItem()).getCreated();
                        }
                        else
                        {
                            Invoice invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceById(getApplicationContext(), GainInvoiceEditFragment.idInvoiceforActivity));
                            numberInventory = invoice.getNumberInvoice();
                            inventoryId = invoice.getInvoiceId();
                            created = invoice.getCreated();
                        }

                        if (inventoryId != 0) {
                            switch (drawerItem.getIdentifier()) {
                                case EDIT:

                                    initIntent(InventoryEditArticlesActivity.class);

                                    break;
                                case REVISION:
                                    initIntent(ListArticlesInventoryActivity.class);
                                    break;
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Збережіть накладну!!!",Toast.LENGTH_SHORT).show();
                    }

                }).build();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;

        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void initViewPager(int position, List<InventoryInvoice> list){

        inventoryInvoicePager = (ViewPager) findViewById(R.id.fragment_edit_inventory_pager);
        pagerAdapter = new InvertoryPagerAdapter(getSupportFragmentManager(), list);
        inventoryInvoicePager.setAdapter(pagerAdapter);
        inventoryInvoicePager.setCurrentItem(position);


    }


    public void initFooterButton(){
        if(inventoryId != 0) {

            buttonNextView = (Button) findViewById(R.id.button_next_fragment);
            buttonPrevView = (Button) findViewById(R.id.button_prev_fragment);

            buttonPrevView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventoryInvoicePager.setCurrentItem(inventoryInvoicePager.getCurrentItem() - 1);
                }
            });

            buttonNextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventoryInvoicePager.setCurrentItem(inventoryInvoicePager.getCurrentItem() + 1);
                }
            });
        }

        else {
            findViewById(R.id.button_layout).setVisibility(View.GONE);
        }
    }
    public void initIntent(Class typeClass){
        editArticlesIntent = new Intent(getApplicationContext(),typeClass);
        editArticlesIntent.putExtra(NUMBER,numberInventory);
        editArticlesIntent.putExtra(ID_INVENTORY,inventoryId);
        editArticlesIntent.putExtra(CREATED,created);
        startActivity(editArticlesIntent);
    }
}
