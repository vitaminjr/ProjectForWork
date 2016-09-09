package com.example.vitaminjr.mobileacounting.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;

/**
 * Created by vitaminjr on 22.07.16.
 */
public class ArticlesPagerAdapter extends FragmentStatePagerAdapter {

    private int idInvoice;
    private int count;
    String numberInvoice;
    public final int CORECTION_TYPE = 4;
    private int created;


    public ArticlesPagerAdapter(FragmentManager fm, int count, int id, String numberInvoice, int created) {
        super(fm);

        this.count = count;
        this.idInvoice = id;
        this.numberInvoice = numberInvoice;
        this.created = created;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Fragment getItem(int position) {
        GainInvoiceEditArticlesFragment editFragment = GainInvoiceEditArticlesFragment.newInstance(numberInvoice,CORECTION_TYPE,idInvoice,position, created);
        return editFragment;
    }

}
