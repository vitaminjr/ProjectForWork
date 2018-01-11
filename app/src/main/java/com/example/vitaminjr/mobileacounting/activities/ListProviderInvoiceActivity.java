package com.example.vitaminjr.mobileacounting.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.vitaminjr.mobileacounting.databases.DBHelper;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.models.Invoice;

/**
 * Created by vitaminjr on 14.07.16.
 */
public class ListProviderInvoiceActivity extends AppCompatActivity {

    public static final String TAG_NAME_PROVIDER= "nameProvider";
    public static final String TAG_CODE_EDRPOU = "codeEDRPOU";
    public static final String TAG_PROVIDER_ID = "providerId";
    String name = "";
    String code = "";
    SearchView searchView;
    ListViewProviderAdapter adapter;
    int type;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_provider);
        initToolbar();
        Intent intent = getIntent();
        type =  intent.getIntExtra("typeAgent",-1);
        if(type == 0){
            this.setTitle("Постачальник");
        }
        else if(type == 1)
            this.setTitle("Покупець");

        ListView listView = (ListView) findViewById(R.id.list_view_provider);
        adapter = new ListViewProviderAdapter(this, SqlQuery.getListProvider(this,type), true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                editProvider(SqlQuery.getProviderById(getApplicationContext(),id));
                intent.putExtra(TAG_PROVIDER_ID,id);
                intent.putExtra(TAG_NAME_PROVIDER,name);
                intent.putExtra(TAG_CODE_EDRPOU,code);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void initToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_provider);
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

    public void editProvider(Cursor cursor){
        if(cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int codeColIndex = cursor.getColumnIndex("code_EDRPOU");

            do {

                name = cursor.getString(nameColIndex);
                code =  cursor.getString(codeColIndex);

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
                    adapter.setCursor(SqlQuery.getListProvider(getApplicationContext(),type));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private Cursor getCursor(String str) {
        Cursor mCursor = null;
        if (str == null  ||  str.length () == 0)  {
            mCursor = SqlQuery.getListProvider(this,type);
        }
        else {
            mCursor = SqlQuery.searchProvider(this,type, str);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        }
        return mCursor;
    }
}
