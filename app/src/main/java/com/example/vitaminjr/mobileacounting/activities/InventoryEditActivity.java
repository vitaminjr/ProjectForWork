package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryEditFragment;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by vitaminjr on 12.09.16.
 */
public class InventoryEditActivity extends AppCompatActivity {

    private final int EDIT = 1;
    private final int REVISION = 2;


    long inventoryId;
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

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(inventoryId == 0) {
            inventoryEditFragment = InventoryEditFragment.newInstance();
        }else {
            inventoryEditFragment = InventoryEditFragment.newInstance(inventoryId);
        }

        fragmentTransaction.replace(R.id.add_inventory_container, inventoryEditFragment);
        fragmentTransaction.commit();
    }



    public void initDrawer(Toolbar toolbar){
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeaderDivider(true)
                //    .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.inventory),
                        new PrimaryDrawerItem().withName(R.string.edit).withIdentifier(EDIT),
                        new PrimaryDrawerItem().withName(R.string.revision).withIdentifier(REVISION)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case EDIT :

                                break;
                            case REVISION :

                                break;
                        }
                    }
                }).build();
    }



}
