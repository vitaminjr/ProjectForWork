package com.example.vitaminjr.mobileacounting.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitaminjr on 22.07.16.
 */
public class ArticlesPagerAdapter extends FragmentPagerAdapter {

    private int idInvoice;
    private int count;
    String numberInvoice;
    public final int CORECTION_TYPE = 4;
    private int created;
    private List<GainInvoiceEditArticlesFragment> listFragment;
    public List<GainInvoiceEditArticlesFragment> getListFragment() {
        return listFragment;
    }

    public ArticlesPagerAdapter(FragmentManager fm, int count, int id, String numberInvoice, int created) {
        super(fm);

        this.count = count;
        this.idInvoice = id;
        this.numberInvoice = numberInvoice;
        this.created = created;
        listFragment = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public GainInvoiceEditArticlesFragment getItem(int position) {
        GainInvoiceEditArticlesFragment editFragment = GainInvoiceEditArticlesFragment.newInstance(numberInvoice,CORECTION_TYPE,idInvoice,position, created);
        listFragment.add(editFragment);
        return editFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
