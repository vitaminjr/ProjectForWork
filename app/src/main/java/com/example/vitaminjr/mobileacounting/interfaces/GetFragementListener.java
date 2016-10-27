package com.example.vitaminjr.mobileacounting.interfaces;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditArticlesFragment;

/**
 * Created by vitaminjr on 13.10.16.
 */

public interface GetFragementListener {
    void getFragment(GainInvoiceEditArticlesFragment fragment);
}
