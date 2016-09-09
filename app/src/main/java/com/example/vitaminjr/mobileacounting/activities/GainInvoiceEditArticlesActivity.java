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
import android.view.View;
import android.widget.Button;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ArticlesPagerAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;

/**
 * Created by vitaminjr on 19.07.16.
 */
public class GainInvoiceEditArticlesActivity extends AppCompatActivity {

    private String invoiceNumber;
    private int correctionType;
    private int idInvoice;
    private int created;


    ViewPager articlesPager;
    ArticlesPagerAdapter pagerAdapter;

    public final static int insert = 1;
    public final static int update = 2;
    public final static int collate = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain_edit_articles_invoice);
        initToolbar();

        Intent intent = getIntent();
        invoiceNumber = intent.getStringExtra(GainInvoiceEditActivity.NUMBER);
        correctionType = intent.getIntExtra(GainInvoiceEditActivity.TYPE, 0);
        idInvoice = intent.getIntExtra(GainInvoiceEditActivity.IDINVOICE,0);
        created = intent.getIntExtra(GainInvoiceEditActivity.CREATED,-1);
        initFooterButton();
        switch (correctionType) {
            case insert :
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                GainInvoiceEditArticlesFragment articlesFragment = GainInvoiceEditArticlesFragment.newInstance(invoiceNumber, correctionType, idInvoice,created);
                fragmentTransaction.add(R.id.edit_invoice_article_container, articlesFragment);
                fragmentTransaction.commit();
                break;
            case collate:
                initViewPager();
                break;
        }
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gain_edit_article);
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

    public void initViewPager() {

       articlesPager = (ViewPager) findViewById(R.id.activity_edit_articles_invoice_pager);
       pagerAdapter = new ArticlesPagerAdapter(getSupportFragmentManager(), SqlQuery.getInvoiceRowCount(idInvoice,this),idInvoice, invoiceNumber,created);
       articlesPager.setAdapter(pagerAdapter);

    }

    public void initFooterButton(){
        if(correctionType == CorrectionType.ctCollate.ordinal()) {
            Button buttonNextView = (Button) findViewById(R.id.button_next_fragment);
            Button buttonPrevView = (Button) findViewById(R.id.button_prev_fragment);
            buttonNextView.clearFocus();
            buttonPrevView.clearFocus();
            buttonPrevView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GainInvoiceEditArticlesFragment) pagerAdapter.getItem(articlesPager.getCurrentItem())).saveInvoiceRows();
                    articlesPager.setCurrentItem(articlesPager.getCurrentItem() - 1);

                }
            });

            buttonNextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GainInvoiceEditArticlesFragment) pagerAdapter.getItem(articlesPager.getCurrentItem())).saveInvoiceRows();
                    articlesPager.setCurrentItem(articlesPager.getCurrentItem() + 1);

                }
            });
        }
        else {
            findViewById(R.id.button_layout).setVisibility(View.GONE);
        }
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
