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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.Preferences;
import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceFragment;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import static com.example.vitaminjr.mobileacounting.Preferences.loadBooleanSetting;
import static com.example.vitaminjr.mobileacounting.Preferences.saveBooleanSetting;

/**
 * Created by vitaminjr on 07.07.16.
 */
public class MoveInvoiceActivity extends AppCompatActivity {

    private final int CREATE_MOVE = 1;
    private final int EXPORT = 2;
    SearchView searchView;
    GainInvoiceFragment gainInvoiceFragment;
    public Toolbar toolbar;
    private static final int REQUEST_PROVIDER = 1;
    MenuItem itemCheck = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain);
        initGui();
    }

    public void initGui() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_gain);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
        FragmentTransaction fragmentTransaction;
        gainInvoiceFragment = GainInvoiceFragment.newInstance(InvoiceType.move.ordinal());
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, gainInvoiceFragment);
        fragmentTransaction.commit();

    }

    public void initDrawer(Toolbar toolbar) {
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeaderDivider(false)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.move),
                        new PrimaryDrawerItem().withName(R.string.create).withIdentifier(CREATE_MOVE),
                        new SecondaryDrawerItem().withName(R.string.export).withIdentifier(EXPORT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case CREATE_MOVE:
                                    initEditActivity();
                                    break;
                                case EXPORT:
                                    try {
                                        SqlQuery.exportInvoices(getApplicationContext());
                                        SqlQuery.exportInvoicesRows(getApplicationContext());
                                        SqlQuery.exportInvoicesRowTovars(getApplicationContext());
                                        SqlQuery.exportInvoiceProviders(getApplicationContext());
                                        Toast.makeText(getApplicationContext(),R.string.export_successfully,Toast.LENGTH_SHORT).show();
                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(),R.string.error_export,Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                            }
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

    public void initEditActivity() {
        Intent intent = new Intent(getApplicationContext(), GainInvoiceEditActivity.class);
        intent.putExtra("type", InvoiceType.move.ordinal());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_check, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        setSearchView();
        MenuItem checkBoxItem = menu.findItem(R.id.hide_invoice);
        checkBoxItem.setChecked(loadBooleanSetting("isHideInvoice",getApplicationContext()));

        MenuItem searchPrpvider = menu.findItem(R.id.search_provider_invoice);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.hide_invoice){
            if(item.isChecked()==false) {
                itemCheck = item;
                item.setChecked(true);
                item.setTitle("Показати зібрані");
            }
            else {
                item.setTitle("Приховати зібрані");
                item.setChecked(false);
            }

            saveBooleanSetting("isHideInvoice",item.isChecked(),getApplicationContext());
            gainInvoiceFragment.updateAdapter();

        }

        if(item.getItemId()==R.id.search_provider_invoice) {
            Intent intent = new Intent(this,ListStoresActivity.class);
            intent.putExtra("typeAgent",0);
            if(itemCheck != null) {
                itemCheck.setChecked(false);
                itemCheck.setTitle("Приховати зібрані");
            }
            startActivityForResult(intent,REQUEST_PROVIDER);
        }
        if(item.getItemId() == R.id.clear_filter){
            gainInvoiceFragment.updateAdapter();
            if(itemCheck != null) {
                itemCheck.setChecked(false);
                itemCheck.setTitle("Приховати зібрані");
                saveBooleanSetting("isHideInvoice", itemCheck.isChecked(), getApplicationContext());
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        if (str == null  ||  str.length () == 0)  {
            if(Preferences.loadBooleanSetting("isHideInvoice",getApplicationContext())==false)
                mCursor = SqlQuery.getCursorListInvoicesMove(getApplicationContext(), InvoiceType.move.ordinal());
            else
                mCursor = SqlQuery.getCursorListInvoicesMoveWithoutComplete(getApplicationContext(), InvoiceType.move.ordinal());
        }
        else {
            if(Preferences.loadBooleanSetting("isHideInvoice",getApplicationContext())==false)
                mCursor = SqlQuery.searchInvoiceMove(this, str, InvoiceType.move.ordinal());
            else
                mCursor = SqlQuery.searchInvoiceMoveWithoutComplete(this, str, InvoiceType.move.ordinal());
        }
        Log.d("counter", "Кількість" + mCursor.getCount());
        return mCursor;
    }

    public void setSearchView(){

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        gainInvoiceFragment.adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint.toString());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                gainInvoiceFragment.adapter.getFilter().filter(checkFilter(query,searchView));
                return true;
            }

            public String checkFilter(String filter, SearchView search){
                String s = filter;
                if(s.length() != 0)
                    if(s.charAt(0)=='*') {
                        if(s.length()== 1)
                            s = String.valueOf("");
                        else
                            s = String.valueOf(s.subSequence(1, s.length() - 1));
                        search.setQuery(s, false);
                    }
                return s;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                gainInvoiceFragment.adapter.getFilter().filter(checkFilter(newText,searchView));
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                        gainInvoiceFragment.adapter.swapCursor(gainInvoiceFragment.setDataFromDBAdapter());
                }
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
                    long idProvider = data.getLongExtra(ListStoresActivity.TAG_STORES_ID,-1);
                    Cursor cursor = SqlQuery.getCursorListInvoicesMoveByProvider(getApplicationContext(),idProvider,InvoiceType.move.ordinal());
                    gainInvoiceFragment.setCursor(cursor);
                    break;
            }
        }
    }


}
