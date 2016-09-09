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
import android.widget.FilterQueryProvider;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ListViewAdapter;
import com.example.vitaminjr.mobileacounting.adapters.ListViewArticlesAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.ListArticlesInvoiceFragment;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListenerArticles;

/**
 * Created by vitaminjr on 09.08.16.
 */
public class ListArticlesInvoiceActivity extends AppCompatActivity implements OnSomeEventListenerArticles {

    SearchView searchView;
    ListViewArticlesAdapter listViewArticlesAdapter;
    int idInvoice;
    MenuItem searchItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_articles_invoice);
        initToolbar();


        Intent getArticleInvoiceIntent = getIntent();
        int corrType =  getArticleInvoiceIntent.getIntExtra(GainInvoiceEditActivity.TYPE,0);
        idInvoice =  getArticleInvoiceIntent.getIntExtra(GainInvoiceEditActivity.IDINVOICE,0);
        int created = getArticleInvoiceIntent.getIntExtra(GainInvoiceEditActivity.CREATED,-1);
        String numberInvoice = getArticleInvoiceIntent.getStringExtra(GainInvoiceEditActivity.NUMBER);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ListArticlesInvoiceFragment listArticlesInvoiceFragment = ListArticlesInvoiceFragment.newInstance(numberInvoice,idInvoice,corrType,created);
        fragmentTransaction.replace(R.id.article_invoice_container, listArticlesInvoiceFragment);
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
                listViewArticlesAdapter.getFilter().filter(query);
                listViewArticlesAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewArticlesAdapter.getFilter().filter(newText);
                listViewArticlesAdapter.notifyDataSetChanged();
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    listViewArticlesAdapter.setCursor(SqlQuery.getListArticle(getApplicationContext(),idInvoice));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        if (str == null  ||  str.length () == 0)  {
           mCursor = SqlQuery.getListArticle(this,idInvoice);
        }
        else {
            mCursor = SqlQuery.searchArticle(this,idInvoice, str);

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
}
