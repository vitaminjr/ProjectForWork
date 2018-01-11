package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.fragments.InventoryEditArticlesFragment;



/**
 * Created by vitaminjr on 03.10.16.
 */
public class InventoryEditArticlesItem extends AppCompatActivity {

    String numberInvoice;
    long inventoryId;
    int created;
    int position;
    int corrType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_articles_item);
        initToolbar();
        Intent intent = getIntent();
        numberInvoice =  intent.getStringExtra("numberInvoice");
        inventoryId =  intent.getLongExtra("inventoryId",0);
        created =  intent.getIntExtra("created",0);
        position  =  intent.getIntExtra("position",0);
        corrType =  intent.getIntExtra("corrType",0);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        InventoryEditArticlesFragment editFragment = InventoryEditArticlesFragment.newInstance(numberInvoice,inventoryId,created,position, corrType);
        fragmentTransaction.replace(R.id.fragment_container, editFragment);
        fragmentTransaction.commit();
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_article_inventory);
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
}
