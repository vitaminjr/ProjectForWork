package com.example.vitaminjr.mobileacounting.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;


import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ListViewAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceFragment;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by vitaminjr on 06.07.16.
 */
public class ExpenseInvoiceActivity extends AppCompatActivity implements OnSomeEventListener {

    private final int CREATE_GAIN = 1;
    private final int EXPORT = 2;
    GainInvoiceFragment gainInvoiceFragment;
    SearchView searchView;
    ListViewAdapter listViewAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain);
        initGui();
    }


    public void initGui(){


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gain);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
        FragmentTransaction fragmentTransaction;
        gainInvoiceFragment = GainInvoiceFragment.newInstance(InvoiceType.expense.ordinal());
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,gainInvoiceFragment);
        fragmentTransaction.commit();

    }


    public void initDrawer(Toolbar toolbar){
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
              //  .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.expense),
                        new PrimaryDrawerItem().withName(R.string.create).withIdentifier(CREATE_GAIN),
                        new SecondaryDrawerItem().withName(R.string.export).withIdentifier(EXPORT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if(drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case CREATE_GAIN:
                                    initEditFragment();
                                    break;
                                case EXPORT:
                                    SqlQuery.exportInvoices(getApplicationContext());
                                    SqlQuery.exportInvoicesRows(getApplicationContext());
                                    SqlQuery.exportInvoicesRowTovars(getApplicationContext());
                                    SqlQuery.exportInvoiceProviders(getApplicationContext());
                                    break;
                            }
                        }
                    }
                }).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();

    }
    public void initEditFragment(){

        Intent intent = new Intent(getApplicationContext(), GainInvoiceEditActivity.class);
        intent.putExtra("type", InvoiceType.expense.ordinal());
        startActivity(intent);
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


        gainInvoiceFragment.adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint.toString());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                gainInvoiceFragment.adapter.getFilter().filter(query);
                gainInvoiceFragment.adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                gainInvoiceFragment.adapter.getFilter().filter(newText);
                gainInvoiceFragment.adapter.notifyDataSetChanged();
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    listViewAdapter.setCursor(SqlQuery.exportInvoices(getApplicationContext(),InvoiceType.expense.ordinal()));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        if (str == null  ||  str.length () == 0)  {
            mCursor = SqlQuery.exportInvoices(getApplicationContext(),InvoiceType.expense.ordinal());
        }
        else {
            mCursor = SqlQuery.searchInvoice(this, str);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        }
        return mCursor;
    }

    @Override
    public void someEvent(ListViewAdapter adapter) {
        listViewAdapter = adapter;
    }
}
