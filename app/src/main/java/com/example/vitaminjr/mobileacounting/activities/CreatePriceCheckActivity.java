package com.example.vitaminjr.mobileacounting.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.fragments.CheckPricesEditFragment;

/**
 * Created by vitaminjr on 29.09.16.
 */
public class CreatePriceCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_price_check);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_price_check);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        CheckPricesEditFragment checkPricesEditFragment = CheckPricesEditFragment.newInstance();
        fragmentTransaction.replace(R.id.fragment_container,checkPricesEditFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
