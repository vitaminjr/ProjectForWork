package com.example.vitaminjr.mobileacounting.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.CheckPricesEditFragment;
import com.example.vitaminjr.mobileacounting.fragments.CheckPricesFragment;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by vitaminjr on 06.07.16.
 */
public class CheckPricesActivity extends AppCompatActivity {

    private final int EXPORT = 1;
    private final int CLEAN = 2;
    private final int INPUT = 3;
    private final int REVISION = 4;
    FragmentTransaction fragmentTransaction;
    CheckPricesEditFragment checkPricesEditFragment;
    CheckPricesFragment checkPricesFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_prices);
        initGui();
        initFragment();
    }

    private void initFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        checkPricesFragment = CheckPricesFragment.newInstance();
        fragmentTransaction.replace(R.id.fragment_container, checkPricesFragment);
        fragmentTransaction.commit();



    }

    public void initGui(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_check_prices);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initDrawer(toolbar);



    }

    public void initDrawer(Toolbar toolbar){
            new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeaderDivider(true)
              //  .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.check_prices),
                        new PrimaryDrawerItem().withName(R.string.input).withIdentifier(INPUT),
                        new PrimaryDrawerItem().withName(R.string.revision).withIdentifier(REVISION),
                        new PrimaryDrawerItem().withName(R.string.clean).withIdentifier(CLEAN),
                        new PrimaryDrawerItem().withName(R.string.export).withIdentifier(EXPORT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case INPUT :
                                fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                                checkPricesEditFragment = CheckPricesEditFragment.newInstance();
                                fragmentTransaction.replace(R.id.fragment_container,checkPricesEditFragment);
                                findViewById(R.id.layout_header).setVisibility(View.GONE);
                                fragmentTransaction.commit();
                                break;
                            case REVISION :
                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                checkPricesFragment = CheckPricesFragment.newInstance();
                                fragmentTransaction.replace(R.id.fragment_container, checkPricesFragment);
                                findViewById(R.id.layout_header).setVisibility(View.VISIBLE);
                                fragmentTransaction.commit();
                                break;
                            case CLEAN :
                                SqlQuery.clearCheckPrices(getApplicationContext());
                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                checkPricesFragment = CheckPricesFragment.newInstance();
                                fragmentTransaction.replace(R.id.fragment_container, checkPricesFragment);
                                fragmentTransaction.commit();
                                break;

                            case EXPORT :
                                try {
                                    SqlQuery.exportPriceCheck(getApplicationContext());
                                    Toast.makeText(getApplicationContext(),"Успішно", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception ex){
                                    Toast.makeText(getApplicationContext(),"Помилка Експорту", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                }).build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

}
