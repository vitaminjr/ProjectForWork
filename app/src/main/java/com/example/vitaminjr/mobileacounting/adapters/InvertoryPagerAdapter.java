package com.example.vitaminjr.mobileacounting.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditFragment;
import com.example.vitaminjr.mobileacounting.fragments.InventoryEditFragment;
import com.example.vitaminjr.mobileacounting.models.InventoryInvoice;
import com.example.vitaminjr.mobileacounting.models.Invoice;

import java.util.List;

/**
 * Created by vitaminjr on 22.07.16.
 */
public class InvertoryPagerAdapter extends FragmentStatePagerAdapter {

    private List<InventoryInvoice> list;
    public static final int REQUEST_ID_CODE = 1;

    public InvertoryPagerAdapter(FragmentManager fm, List<InventoryInvoice> list) {
        super(fm);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        long id =  list.get(position).getInventoryId();
        InventoryEditFragment editFragment = InventoryEditFragment.newInstance(id);
        editFragment.setTargetFragment(editFragment,REQUEST_ID_CODE);
        return editFragment;
    }

}
