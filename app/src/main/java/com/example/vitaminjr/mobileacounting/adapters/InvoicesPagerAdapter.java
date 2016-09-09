package com.example.vitaminjr.mobileacounting.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditFragment;
import com.example.vitaminjr.mobileacounting.models.Invoice;

import java.util.List;

/**
 * Created by vitaminjr on 22.07.16.
 */
public class InvoicesPagerAdapter extends FragmentStatePagerAdapter {

    private List<Invoice> listInvoice;
    public static final int REQUEST_ID_CODE = 1;

    public InvoicesPagerAdapter(FragmentManager fm, List<Invoice> listInvoice) {
        super(fm);
        this.listInvoice = listInvoice;

    }

    @Override
    public int getCount() {
        return listInvoice.size();
    }

    @Override
    public Fragment getItem(int position) {
        long id =  listInvoice.get(position).getInvoiceId();
        GainInvoiceEditFragment editFragment = GainInvoiceEditFragment.newInstance(id);
        editFragment.setTargetFragment(editFragment,REQUEST_ID_CODE);
        return editFragment;
    }

}
