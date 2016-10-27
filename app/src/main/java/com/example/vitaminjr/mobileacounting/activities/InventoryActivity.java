package com.example.vitaminjr.mobileacounting.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ListViewAdapter;
import com.example.vitaminjr.mobileacounting.adapters.ListViewInventoryAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.CheckPricesFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryFragment;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.interfaces.OnEventListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by vitaminjr on 06.07.16.
 */
public class InventoryActivity extends AppCompatActivity implements OnEventListener{

    private final int CREATE = 1;
    private final int EXPORT = 2;
    InventoryFragment inventoryFragment;
    SearchView searchView;
    ListViewInventoryAdapter listViewAdapter;

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
        inventoryFragment = InventoryFragment.newInstance();
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
                                SqlQuery.exportInventories(getApplicationContext());
                                SqlQuery.exportInventoryAction(getApplicationContext());
                                SqlQuery.exportInventoryRows(getApplicationContext());
                                SqlQuery.exportStores(getApplicationContext());
                                SqlQuery.exportArticlesInventory(getApplicationContext());
                                try {

                                    Toast.makeText(getApplicationContext(), R.string.export_successfully, Toast.LENGTH_SHORT).show();
                                }catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), R.string.error_export, Toast.LENGTH_SHORT).show();
                                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);


        inventoryFragment.adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint.toString());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                inventoryFragment.adapter.getFilter().filter(query);
                inventoryFragment.adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryFragment.adapter.getFilter().filter(newText);
                inventoryFragment.adapter.notifyDataSetChanged();
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    listViewAdapter.setCursor(SqlQuery.getInventoriesCursor(getApplicationContext()));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        if (str == null  ||  str.length () == 0)  {
            mCursor = SqlQuery.getInventoriesCursor(getApplicationContext());
        }
        else {
            mCursor = SqlQuery.searchInventory(this, str);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        }
        return mCursor;
    }

    @Override
    public void someEvent(ListViewInventoryAdapter adapter) {
        listViewAdapter = adapter;
    }
}
