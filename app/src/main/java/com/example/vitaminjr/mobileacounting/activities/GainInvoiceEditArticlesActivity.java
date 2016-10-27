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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.ArticlesPagerAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.interfaces.GetFragementListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;


/**
 * Created by vitaminjr on 19.07.16.
 */
public class GainInvoiceEditArticlesActivity extends AppCompatActivity {

    private String invoiceNumber;
    private int correctionType;
    private int idInvoice;
    private int created;
    public boolean leftButton = false;
    public boolean rightButton = false;


    public ViewPager articlesPager;
    public ArticlesPagerAdapter pagerAdapter;

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
                startFragment();
                break;
            case update :
                if(created == CreateType.device.ordinal())
                    initViewPager();
                else
                    startFragment();
                break;
            case collate:
                initViewPager();
                break;
        }
    }

    public void startFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        GainInvoiceEditArticlesFragment articlesFragment = GainInvoiceEditArticlesFragment.newInstance(invoiceNumber, correctionType, idInvoice,created);
        fragmentTransaction.add(R.id.edit_invoice_article_container, articlesFragment);
        fragmentTransaction.commit();
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

        }
        return super.onOptionsItemSelected(item);
    }

    public void initViewPager() {

        articlesPager = (ViewPager) findViewById(R.id.activity_edit_articles_invoice_pager);
        pagerAdapter = new ArticlesPagerAdapter(getSupportFragmentManager(), SqlQuery.getInvoiceRowCount(idInvoice,this),idInvoice, invoiceNumber,created);
        articlesPager.setAdapter(pagerAdapter);

    }

    public void initFooterButton(){
        if(correctionType == CorrectionType.ctCollate.ordinal()
                || (created == CreateType.device.ordinal()
                && correctionType == CorrectionType.ctUpdate.ordinal())) {
            final Button buttonNextView = (Button) findViewById(R.id.button_next_fragment);
            final Button buttonPrevView = (Button) findViewById(R.id.button_prev_fragment);
            buttonNextView.clearFocus();
            buttonPrevView.clearFocus();
            buttonPrevView.setEnabled(false);

            buttonPrevView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftButton = true;
                    rightButton = false;
                    pagerAdapter.getListFragment().get(articlesPager.getCurrentItem()).updateInvoiceRowWithCondition();
                    buttonNextView.setEnabled(true);
                    if(pagerAdapter.getListFragment().get(articlesPager.getCurrentItem()).isOnCreateDialog == false)
                        articlesPager.setCurrentItem(articlesPager.getCurrentItem() - 1);
                    pagerAdapter.notifyDataSetChanged();

                    if(articlesPager.getCurrentItem()==0){
                        buttonPrevView.setEnabled(false);
                    }
                }
            });

            buttonNextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightButton = true;
                    leftButton = false;
                    buttonPrevView.setEnabled(true);
                    pagerAdapter.getListFragment().get(articlesPager.getCurrentItem()).updateInvoiceRowWithCondition();

                    if(pagerAdapter.getListFragment().get(articlesPager.getCurrentItem()).isOnCreateDialog == false)
                        articlesPager.setCurrentItem(articlesPager.getCurrentItem() + 1);
                    pagerAdapter.notifyDataSetChanged();
                    Log.d("countItem", String.valueOf(articlesPager.getCurrentItem()));
                    if(pagerAdapter.getCount()==articlesPager.getCurrentItem()+1){
                        buttonNextView.setEnabled(false);
                    }
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
    }

}
