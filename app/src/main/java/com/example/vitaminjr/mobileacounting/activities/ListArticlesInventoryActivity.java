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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FilterQueryProvider;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ListViewArticlesAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.ListArticlesInventoryFragment;
import com.example.vitaminjr.mobileacounting.fragments.ListArticlesInvoiceFragment;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListenerArticles;

/**
 * Created by vitaminjr on 09.08.16.
 */
public class ListArticlesInventoryActivity extends AppCompatActivity implements OnSomeEventListenerArticles {

    SearchView searchView;
    ListViewArticlesAdapter listViewArticlesAdapter;
    long idInventory;
    MenuItem searchItem;
    int created;
    String numberInvoice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_articles_invoice);
        initToolbar();


        Intent getArticleInvoiceIntent = getIntent();
        idInventory =  getArticleInvoiceIntent.getLongExtra(InventoryEditActivity.ID_INVENTORY,0);
        created = getArticleInvoiceIntent.getIntExtra(InventoryEditActivity.CREATED,-1);
        numberInvoice = getArticleInvoiceIntent.getStringExtra(InventoryEditActivity.NUMBER);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ListArticlesInventoryFragment listArticlesInventoryFragment = ListArticlesInventoryFragment.newInstance(numberInvoice,idInventory,created);
        fragmentTransaction.replace(R.id.article_invoice_container, listArticlesInventoryFragment);
        fragmentTransaction.commit();

    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_articles_invoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }

            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        setSearchView();
        return super.onCreateOptionsMenu(menu);
    }


    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        str = str.replace("*", "");
        if (str == null  ||  str.length () == 0)  {
           mCursor = SqlQuery.getListInventoryAction(getApplicationContext(), idInventory);
        }
        else {
            mCursor = SqlQuery.searchArticleInventory(this,idInventory, str);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        }
        return mCursor;
    }

    @Override
    public void someEvent(ListViewArticlesAdapter adapter) {
        listViewArticlesAdapter = adapter;
    }

    public void setSearchView(){
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);



        listViewArticlesAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                return getCursor(constraint.toString());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listViewArticlesAdapter.getFilter().filter(checkFilter(query,searchView));
                listViewArticlesAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewArticlesAdapter.getFilter().filter(checkFilter(newText,searchView));
                listViewArticlesAdapter.notifyDataSetChanged();
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    listViewArticlesAdapter = new ListViewArticlesAdapter(getApplicationContext(),SqlQuery.getListInventoryAction(getApplicationContext(), idInventory),true);
                    //listViewArticlesAdapter.setCursor(SqlQuery.getListInventoryAction(getApplicationContext(), idInventory));
                    listViewArticlesAdapter.notifyDataSetChanged();
            }
        });

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
    protected void onResume() {
        super.onResume();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ListArticlesInventoryFragment listArticlesInventoryFragment = ListArticlesInventoryFragment.newInstance(numberInvoice,idInventory,created);
        fragmentTransaction.replace(R.id.article_invoice_container, listArticlesInventoryFragment);
        fragmentTransaction.commit();
    }
}
