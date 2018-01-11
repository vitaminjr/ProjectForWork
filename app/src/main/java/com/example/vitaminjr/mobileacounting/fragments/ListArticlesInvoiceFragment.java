package com.example.vitaminjr.mobileacounting.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.InventoryEditArticlesItem;
import com.example.vitaminjr.mobileacounting.activities.InvoiceEditArticlesItem;
import com.example.vitaminjr.mobileacounting.adapters.ListViewArticlesAdapter;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListenerArticles;

/**
 * Created by vitaminjr on 09.08.16.
 */
public class ListArticlesInvoiceFragment extends Fragment {

    private int invoiceId;
    private int correctionType;
    private String numberInvoice;
    private int created;
    OnSomeEventListenerArticles onSomeEventListenerArticles;
    ListViewArticlesAdapter adapter;
    long idTemp;
    ListView listView;
    int invoiceTypeId;

    public static ListArticlesInvoiceFragment newInstance(String number, int invoiceId, int correctionType, int created, int invoiceTypeId){
        ListArticlesInvoiceFragment listArticlesInvoiceFragment = new ListArticlesInvoiceFragment(number, invoiceId, correctionType, created, invoiceTypeId);
        return listArticlesInvoiceFragment;
    }

    public ListArticlesInvoiceFragment(String number, int invoiceId, int correctionType, int created, int invoiceTypeId){
        this.invoiceId = invoiceId;
        this.correctionType = correctionType;
        this.numberInvoice = number;
        this.created = created;
        this.invoiceTypeId = invoiceTypeId;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onSomeEventListenerArticles = (OnSomeEventListenerArticles) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_articles,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = (ListView) getActivity().findViewById(R.id.list_view_articles);
        initAdapter();
        onSomeEventListenerArticles.someEvent(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), InvoiceEditArticlesItem.class);
                intent.putExtra("numberInvoice",numberInvoice);
                intent.putExtra("invoiceId",invoiceId);
                intent.putExtra("created",created);
                intent.putExtra("position",position);
                intent.putExtra("type",invoiceTypeId);
                intent.putExtra("corrType",CorrectionType.ctCollate.ordinal());
                startActivity(intent);


/*                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
                GainInvoiceEditArticlesFragment editFragment = GainInvoiceEditArticlesFragment.newInstance(numberInvoice, correctionType,invoiceId, position,created);
                fragmentTransaction.replace(R.id.article_invoice_container, editFragment);
                fragmentTransaction.commit();
                getActivity().findViewById(R.id.layout_header).setVisibility(View.GONE);*/

            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                idTemp = id;
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete_item, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.delete_item){
                    SqlQuery.deleteInvoiceRow(getContext(),idTemp);
                    Toast.makeText(getContext(),"Успішно видалено!!!",Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    initAdapter();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

            }
        });
    }

    @Override
    public void onResume() {
        getActivity().findViewById(R.id.layout_header).setVisibility(View.VISIBLE);
        super.onResume();
    }

    public void initAdapter(){
        adapter = new ListViewArticlesAdapter(getContext(), SqlQuery.getListArticle(getContext(),invoiceId),true);
        listView.setAdapter(adapter);
        if(created == CreateType.pc.ordinal())
        {
            adapter.setToggle(true);
        }
    }

}
