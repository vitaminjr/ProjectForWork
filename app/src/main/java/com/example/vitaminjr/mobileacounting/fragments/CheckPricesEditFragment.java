package com.example.vitaminjr.mobileacounting.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.models.BarcodeTamplateInfo;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;
import com.example.vitaminjr.mobileacounting.models.ResultTemplate;

import java.util.List;

/**
 * Created by vitaminjr on 09.09.16.
 */
public class CheckPricesEditFragment extends Fragment {


    EditText editTextBarcode;
    EditText editTextPriceTag;
    TextView textViewPrice;
    TextView textViewNameArticle;
    TextView textViewUnitName;
    InvoiceRow article;

    List<BarcodeTamplateInfo> listTemplate;


    public static CheckPricesEditFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CheckPricesEditFragment fragment = new CheckPricesEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view =  inflater.inflate(R.layout.fragment_edit_price_check,container,false);
        initGui(view);
        initListeners();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initGui(View view){
        textViewPrice = (TextView) view.findViewById(R.id.text_view_price_check);
        editTextBarcode = (EditText) view.findViewById(R.id.edit_text_barcode_price_check);
        editTextPriceTag = (EditText) view.findViewById(R.id.text_view_price_tag);
        textViewUnitName = (TextView) view.findViewById(R.id.text_name_article);
        textViewNameArticle = (TextView) view.findViewById(R.id.text_unit_article);
        editTextBarcodeArticleAction(true,true,true);
        editTextPriceTagArticleAction(false,false,false);
    }

    public void initListeners(){
        editTextBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(!String.valueOf(editTextBarcode.getText()).equals("")) {

                        String barcode = String.valueOf(editTextBarcode.getText());

                        listTemplate = SqlQuery.getBarcodeTamplates(getContext());

                        ResultTemplate resultTmp = SqlQuery.getArticleByBarcode(barcode.trim(),
                                getContext(),listTemplate);

                        article =  SqlQuery.getArticleInvoice(resultTmp.getCursor());


                        if(article.getArticleId() == 0)
                        {
                            Toast.makeText(getContext(),"Товар не знайдено", Toast.LENGTH_SHORT).show();

                            editTextBarcode.setText("");
                            editTextBarcode.requestFocus();
                        }
                        else {
                            showArticle();
                            editTextBarcodeArticleAction(false,false,false);
                            editTextPriceTagArticleAction(true,true,true);
                            editTextPriceTag.requestFocus();
                        }
                    }
                    return true;
                }
                return false;
            }
        });


        editTextPriceTag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(article.getPrice() == Float.parseFloat(editTextPriceTag.getText().toString())){
                        clearTextView();
                        editTextBarcodeArticleAction(true,true,true);
                        editTextPriceTagArticleAction(false,false,false);
                        editTextBarcode.requestFocus();
                    }
                    else {
                        onCreateDialogWarningPrice();
                        SqlQuery.insertPriceCheck(getContext(),article);
                        clearTextView();
                        editTextBarcodeArticleAction(true,true,true);
                        editTextPriceTagArticleAction(false,false,false);
                        editTextBarcode.requestFocus();
                    }

                    return true;
                }

                return false;
            }
        });


    }


    public void onCreateDialogWarningPrice(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Попередження!!!")
                .setMessage("Розбіжності ціни в ціннику!!! ")
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showArticle(){
        textViewUnitName.setText(article.getUnitName().toString());
        textViewNameArticle.setText(article.getName().toString());
        textViewPrice.setText(String.valueOf(article.getPrice()));
        editTextPriceTag.setText(String.valueOf(article.getPrice()));
    }

    public void editTextBarcodeArticleAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextBarcode.setEnabled(enabled);
        editTextBarcode.setFocusable(focusable);
        editTextBarcode.setFocusableInTouchMode(focusableInTouch);
    }

    public void editTextPriceTagArticleAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextPriceTag.setEnabled(enabled);
        editTextPriceTag.setFocusable(focusable);
        editTextPriceTag.setFocusableInTouchMode(focusableInTouch);
    }


    public void clearTextView(){
        editTextBarcode.setText("");
        textViewPrice.setText("");
        editTextPriceTag.setText("");
        textViewNameArticle.setText("");
        textViewUnitName.setText("");
    }

}
