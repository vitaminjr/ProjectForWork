package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryEditArticlesFragment;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;


/**
 * Created by vitaminjr on 03.10.16.
 */
public class InvoiceEditArticlesItem extends AppCompatActivity {

    String numberInvoice;
    int invoiceId;
    int created;
    int position;
    int correctionType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_articles_item);
        initToolbar();
        Intent intent = getIntent();
        numberInvoice =  intent.getStringExtra("numberInvoice");
        invoiceId =  intent.getIntExtra("invoiceId",0);
        created =  intent.getIntExtra("created",0);
        position  =  intent.getIntExtra("position",0);
        correctionType =  intent.getIntExtra("corrType",0);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        GainInvoiceEditArticlesFragment editFragment = GainInvoiceEditArticlesFragment.newInstance(numberInvoice, correctionType,invoiceId, position,created);
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
            FragmentManager fm = getSupportFragmentManager();
            OnBackPressedListener backPressedListener = null;

            if(fm.getFragments() != null) {
                for (Fragment fragment : fm.getFragments()) {
                    if (fragment instanceof OnBackPressedListener) {
                        backPressedListener = (OnBackPressedListener) fragment;
                        break;
                    }
                }
            }

            if (backPressedListener != null) {
                backPressedListener.onBackPressed();
            } else {
                super.onBackPressed();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;

        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }

    }
}
