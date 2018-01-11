package com.example.vitaminjr.mobileacounting.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ListViewProviderAdapter;
import com.example.vitaminjr.mobileacounting.adapters.ListViewStoresAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;

/**
 * Created by vitaminjr on 14.07.16.
 */
public class ListStoresActivity extends AppCompatActivity {

    public static final String TAG_NAME_STORES= "nameStores";
    public static final String TAG_STORES_ID = "storesId";
    String name = "";
    SearchView searchView;
    ListViewStoresAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stores);
        initToolbar();

        ListView listView = (ListView) findViewById(R.id.list_view_stores);
        adapter = new ListViewStoresAdapter(this, SqlQuery.getStores(this), true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                editStores(SqlQuery.getStoresById(getApplicationContext(),id));
                intent.putExtra(TAG_STORES_ID,id);
                intent.putExtra(TAG_NAME_STORES,name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void initToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_stores);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void editStores(Cursor cursor){
        if(cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");

            do {

                name = cursor.getString(nameColIndex);

            }while (cursor.moveToNext());
        }
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


        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint.toString());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    adapter.setCursor(SqlQuery.getStores(getApplicationContext()));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        if (str == null  ||  str.length () == 0)  {
            mCursor = SqlQuery.getStores(this);
        }
        else {
            mCursor = SqlQuery.searchStores(this, str);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        }
        return mCursor;
    }
}
