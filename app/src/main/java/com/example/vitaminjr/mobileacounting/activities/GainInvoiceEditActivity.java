package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.adapters.InvoicesPagerAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.fragments.GainInvoiceEditFragment;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.InvoiceType;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.models.Invoice;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

/**
 * Created by vitaminjr on 15.07.16.
 */
public class GainInvoiceEditActivity extends AppCompatActivity {

    private final int EDIT = 1;
    private final int REVISION = 2;
    private final int REVISIONLIST = 3;
    private final int EXPORT = 4;
    public final static String NUMBER = "numberInvoice";
    public final static String TYPE = "CorrectionType";
    public final static String INVOICE_TYPE = "invoiceType";
    public final static String IDINVOICE = "InvoiceId";
    public final static String CREATED = "created";
    FragmentTransaction fragmentTransaction;
    private String numberInvoiceGain;
    private int invoiceId = 0;
    private long longInvoiceId;
    ViewPager articlesPager;
    List<Invoice> invoiceList;
    InvoicesPagerAdapter pagerAdapter;
    Intent editArticlesIntent;
    Button buttonNextView;
    Button buttonPrevView;
    ImageView buttonEdit;
    int invoiceTypeId;
    private int created;
    int position;
    GainInvoiceEditFragment gainInvoiceEditFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain_edit_invoice);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initGui();
    }

    public void initGui(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gain_edit);
        buttonEdit  = (ImageView) findViewById(R.id.button_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        longInvoiceId =  intent.getLongExtra("id", 0);
        invoiceTypeId = intent.getIntExtra("type",-1);
        initDrawer(toolbar);


        initFooterButton();
        if(longInvoiceId == 0) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            gainInvoiceEditFragment = GainInvoiceEditFragment.newInstance(invoiceTypeId);
            fragmentTransaction.replace(R.id.add_invoice_container, gainInvoiceEditFragment);
            fragmentTransaction.commit();

        }else {
            invoiceList = SqlQuery.getListInvoices(SqlQuery.getCursorListInvoices(this, invoiceTypeId));
            for (int i = 0; i < invoiceList.size(); i++) {
                if(invoiceList.get(i).getInvoiceId()==longInvoiceId)
                    position = i;
            }

            initViewPager(position, invoiceList);
        }
        clickEditButton();
    }

    public void initViewPager(int position, List<Invoice> list){

        articlesPager = (ViewPager) findViewById(R.id.fragment_edit_invoice_pager);
        pagerAdapter = new InvoicesPagerAdapter(getSupportFragmentManager(), list);
        articlesPager.setAdapter(pagerAdapter);
        articlesPager.setCurrentItem(position);


    }

    public void initDrawer(Toolbar toolbar){
        SectionDrawerItem itemGain;
        if (invoiceTypeId == InvoiceType.profit.ordinal())
            itemGain = new SectionDrawerItem().withName(R.string.gain);
        else
            itemGain = new SectionDrawerItem().withName(R.string.expense);

        PrimaryDrawerItem itemEdit = new PrimaryDrawerItem().withName(R.string.edit).withIdentifier(EDIT);
        PrimaryDrawerItem itemRevision = new PrimaryDrawerItem().withName(R.string.revision).withIdentifier(REVISION);
        PrimaryDrawerItem itemRevisionList = new PrimaryDrawerItem().withName(R.string.revision_list).withIdentifier(REVISIONLIST);
        PrimaryDrawerItem itemExport = new PrimaryDrawerItem().withName(R.string.export).withIdentifier(EXPORT);
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
            //    .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(itemGain,itemEdit,itemRevision,itemRevisionList,itemExport)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if(drawerItem != null) {
                            getInvoice();
                            switch (drawerItem.getIdentifier()) {

                                case EDIT:

                                    if(invoiceId == 0 ) {
                                        if(gainInvoiceEditFragment.saveInvoice()==true){
                                            getInvoice();
                                            initIntent(CorrectionType.ctInsert.ordinal(),GainInvoiceEditArticlesActivity.class);
                                        }
                                    }
                                    else if(created == CreateType.pc.ordinal())
                                        initIntent(CorrectionType.ctUpdate.ordinal(),GainInvoiceEditArticlesActivity.class);
                                    else
                                        initIntent(CorrectionType.ctInsert.ordinal(),GainInvoiceEditArticlesActivity.class);
                                    break;

                                case REVISION:

                                    if(invoiceId == 0 ) {
                                        if(gainInvoiceEditFragment.saveInvoice()==true){
                                            getInvoice();
                                            initIntent(CorrectionType.ctUpdate.ordinal(),GainInvoiceEditArticlesActivity.class);
                                        }
                                    }
                                    else if(created == CreateType.pc.ordinal())
                                            initIntent(CorrectionType.ctCollate.ordinal(),GainInvoiceEditArticlesActivity.class);
                                         else
                                            initIntent(CorrectionType.ctUpdate.ordinal(),GainInvoiceEditArticlesActivity.class);
                                    break;

                                case REVISIONLIST:

                                    if(invoiceId == 0 ) {
                                        if(gainInvoiceEditFragment.saveInvoice()==true){
                                            getInvoice();
                                            initIntent(CorrectionType.ctUpdate.ordinal(),ListArticlesInvoiceActivity.class);
                                        }
                                    }
                                    else if(created == CreateType.pc.ordinal())
                                            initIntent(CorrectionType.ctCollate.ordinal(),ListArticlesInvoiceActivity.class);
                                         else
                                            initIntent(CorrectionType.ctUpdate.ordinal(),ListArticlesInvoiceActivity.class);
                                    break;

                                case EXPORT:
                                    SqlQuery.exportInvoices(getApplicationContext());
                                    SqlQuery.exportInvoicesRows(getApplicationContext());
                                    SqlQuery.exportInvoicesRowTovars(getApplicationContext());
                                    SqlQuery.exportInvoiceProviders(getApplicationContext());
                                    break;
                            }
                        }
                    }
                }).build();
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

        findViewById(R.id.fragment_edit_invoice_pager).setVisibility(View.VISIBLE);
    }

    public void initFooterButton(){
        if(longInvoiceId != 0) {

            buttonNextView = (Button) findViewById(R.id.button_next_fragment);
            buttonPrevView = (Button) findViewById(R.id.button_prev_fragment);

            buttonPrevView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    articlesPager.setCurrentItem(articlesPager.getCurrentItem() - 1);
                }
            });

            buttonNextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    articlesPager.setCurrentItem(articlesPager.getCurrentItem() + 1);
                }
            });
        }

        else {
            findViewById(R.id.button_prev_fragment).setVisibility(View.GONE);
            findViewById(R.id.button_next_fragment).setVisibility(View.GONE);
        }
    }
     public void initIntent(int type, Class typeClass){
         editArticlesIntent = new Intent(getApplicationContext(),typeClass);
         editArticlesIntent.putExtra(INVOICE_TYPE,invoiceTypeId);
         editArticlesIntent.putExtra(NUMBER,numberInvoiceGain);
         editArticlesIntent.putExtra(TYPE, type);
         editArticlesIntent.putExtra(IDINVOICE,invoiceId);
         editArticlesIntent.putExtra(CREATED,created);
         startActivity(editArticlesIntent);
     }

    public void clickEditButton(){
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInvoice();
                if(invoiceId == 0 ) {
                    if(gainInvoiceEditFragment.saveInvoice()==true){
                        getInvoice();
                        initIntent(CorrectionType.ctInsert.ordinal(),GainInvoiceEditArticlesActivity.class);
                    }
                }
                else if(created == CreateType.pc.ordinal())
                    initIntent(CorrectionType.ctUpdate.ordinal(),GainInvoiceEditArticlesActivity.class);
                else
                    initIntent(CorrectionType.ctInsert.ordinal(),GainInvoiceEditArticlesActivity.class);
            }
        });
    }

    public void getInvoice(){

        if(invoiceList!=null) {
            numberInvoiceGain = invoiceList.get(articlesPager.getCurrentItem()).getNumberInvoice();
            invoiceId = invoiceList.get(articlesPager.getCurrentItem()).getInvoiceId();
            created = invoiceList.get(articlesPager.getCurrentItem()).getCreated();
        }
        else
        {
            Invoice invoice = SqlQuery.getInvoice(SqlQuery.getInvoiceById(getApplicationContext(), GainInvoiceEditFragment.idInvoiceforActivity));
            numberInvoiceGain = invoice.getNumberInvoice();
            invoiceId = invoice.getInvoiceId();
            created = invoice.getCreated();
        }
    }

}
