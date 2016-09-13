package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.fragments.CheckPricesFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by vitaminjr on 06.07.16.
 */
public class InventoryActivity extends AppCompatActivity {

    private final int CREATE = 1;
    private final int EXPORT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initGui();
        initFragment();
    }

    public void initGui(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inventory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initDrawer(toolbar);
    }

    public void initFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        InventoryFragment inventoryFragment = InventoryFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_container, inventoryFragment);
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
                        new PrimaryDrawerItem().withName(R.string.create).withIdentifier(CREATE),
                        new PrimaryDrawerItem().withName(R.string.export).withIdentifier(EXPORT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case CREATE :
                                Intent intent = new Intent(getApplicationContext(), InventoryEditActivity.class);
                                startActivity(intent);
                                break;
                            case EXPORT :

                                break;
                        }
                    }
                }).build();
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();

    }

}
