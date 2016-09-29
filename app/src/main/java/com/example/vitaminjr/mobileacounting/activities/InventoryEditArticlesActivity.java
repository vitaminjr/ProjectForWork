package com.example.vitaminjr.mobileacounting.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ArticlesPagerAdapter;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryEditArticlesFragment;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;

/**
 * Created by vitaminjr on 19.07.16.
 */
public class InventoryEditArticlesActivity extends AppCompatActivity {

    private String inventoryNumber;
    private long idInventory;
    private int created;


    ViewPager articlesPager;
    ArticlesPagerAdapter pagerAdapter;

    public final static int insert = 1;
    public final static int update = 2;
    public final static int collate = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_articles_inventory);
        initToolbar();

        Intent intent = getIntent();
        inventoryNumber = intent.getStringExtra(InventoryEditActivity.NUMBER);
        idInventory = intent.getLongExtra(InventoryEditActivity.ID_INVENTORY,0);
        created = intent.getIntExtra(InventoryEditActivity.CREATED,-1);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        InventoryEditArticlesFragment articlesFragment = InventoryEditArticlesFragment.newInstance(inventoryNumber, idInventory, created);

        fragmentTransaction.add(R.id.edit_inventory_article_container, articlesFragment);
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

            finish();
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

        //findViewById(R.id.fragment_edit_invoice_pager).setVisibility(View.VISIBLE);
    }

}
