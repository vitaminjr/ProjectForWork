package com.example.vitaminjr.mobileacounting.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.activities.GainInvoiceEditArticlesActivity;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.SetHideNotKeyboard;
import com.example.vitaminjr.mobileacounting.interfaces.GetFragementListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnEventListener;
import com.example.vitaminjr.mobileacounting.models.BarcodeTamplateInfo;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;
import com.example.vitaminjr.mobileacounting.models.ResultTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by vitaminjr on 19.07.16.
 */
public class GainInvoiceEditArticlesFragment extends Fragment  implements OnBackPressedListener {

    private String invoiceNumber;
    private int correctionType;
    private int idInvoice;
    private int positionArticle;
    private int created;
    private int corrTypeStart;

    private final int pcType = 0;
    private final int deviceType = 1;
    List<BarcodeTamplateInfo> listTemplate;


    InvoiceRow invoiceRow;
    InvoiceRow article;
    InvoiceRow invoiceRowCopy;

    Button buttonCountIncrement;
    Button buttonCountDecrement;
    TextView textNumberInvoice;
    TextView textNameArticle;
    TextView textUnitArticle;
    EditText editTextCountArticle;
    EditText editTextBarcodeInvoice;
    EditText editTextPriceArticle;
    TextView textViewSumaArticle;
    TextView textViewPlannedCount;
    boolean cancelButton = false;
    public boolean isOnCreateDialog = false;

    public static GainInvoiceEditArticlesFragment newInstance(String invoiceNumber, int typeFragment, int idInvoice, int created){

        GainInvoiceEditArticlesFragment gainInvoiceEditArticlesFragment = new GainInvoiceEditArticlesFragment(invoiceNumber, typeFragment, idInvoice,created);

        return gainInvoiceEditArticlesFragment;
    }

    public static GainInvoiceEditArticlesFragment newInstance(String invoiceNumber, int typeFragment, int idInvoice, int position, int created){

        GainInvoiceEditArticlesFragment gainInvoiceEditArticlesFragment = new GainInvoiceEditArticlesFragment(invoiceNumber, typeFragment, idInvoice, position,created);

        return gainInvoiceEditArticlesFragment;
    }

    public static GainInvoiceEditArticlesFragment newInstance() {

        Bundle args = new Bundle();

        GainInvoiceEditArticlesFragment fragment = new GainInvoiceEditArticlesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public GainInvoiceEditArticlesFragment() {
    }

    public GainInvoiceEditArticlesFragment(String number, int typeFragment, int idInvoice, int created)
    {
        invoiceRow = new InvoiceRow();
        article = new InvoiceRow();
        this.invoiceNumber = number;
        this.correctionType = typeFragment;
        this.idInvoice = idInvoice;
        this.created = created;

    }

    public GainInvoiceEditArticlesFragment(String number, int typeFragment, int idInvoice, int position, int created)
    {
        invoiceRow = new InvoiceRow();
        article = new InvoiceRow();
        this.invoiceNumber = number;
        this.correctionType = typeFragment;
        this.idInvoice = idInvoice;
        this.positionArticle = position;
        this.created = created;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invoiceRowCopy = new InvoiceRow();
        Log.d("Log", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_edit_articles_invoice,container,false);
        isOnCreateDialog = false;
        initGui(view);

        listTemplate = SqlQuery.getBarcodeTemplates(getContext());
        corrTypeStart = correctionType;
        if((correctionType == CorrectionType.ctUpdate.ordinal()
                && created == CreateType.device.ordinal()) ||
                correctionType == CorrectionType.ctCollate.ordinal()) {
            ShowListInvoice();
            stateElements(false,true,false,true,true);
            editTextCountArticle.selectAll();
        }else
            stateElements(true,false,false,false,false);

        Listeners();
        Log.d("Log", "onCreateView");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Log", "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Log", "onStart");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            cancelButton = true;
            if(invoiceRow.getArticleId() == 0){
                if(!editTextBarcodeInvoice.getText().toString().equals("")){
                    saveInvoiceRow();
                    getActivity().finish();
                }
            }else
            if(invoiceRow.getQuantityAccount() != invoiceRowCopy.getQuantityAccount()
                    || invoiceRow.getPriceAccount() != Float.parseFloat(editTextPriceArticle.getText().toString()) ) {
                if(created == CreateType.pc.ordinal()){
                    updateInvoiceRowWithCondition();
                }else {
                    saveInvoiceRow();
                    getActivity().finish();
                }
            }
            else
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cancelButton = true;
        if(invoiceRow.getArticleId() == 0){
            if(!editTextBarcodeInvoice.getText().toString().equals("")){
                saveInvoiceRow();
                getActivity().finish();
            } else
                getActivity().finish();
        }else
        if(invoiceRow.getQuantityAccount() != invoiceRowCopy.getQuantityAccount()
                || invoiceRow.getPriceAccount() != Float.parseFloat(editTextPriceArticle.getText().toString()) ) {
            if(created == CreateType.pc.ordinal()){
                updateInvoiceRowWithCondition();
            }else {
                saveInvoiceRow();
                getActivity().finish();
            }
        }
        else
            getActivity().finish();

    }

    public void initGui(View view){

        buttonCountIncrement = (Button) view.findViewById(R.id.button_count_plus);
        buttonCountDecrement = (Button) view.findViewById(R.id.button_count_minus);
        textNumberInvoice = (TextView) view.findViewById(R.id.text_number_invoice);
        textNameArticle = (TextView) view.findViewById(R.id.text_name_article);
        textUnitArticle = (TextView) view.findViewById(R.id.text_unit_article);
        editTextCountArticle = (EditText) view.findViewById(R.id.edit_text_count_article);
        editTextBarcodeInvoice = (EditText) view.findViewById(R.id.edit_text_barcode_invoice);
        editTextPriceArticle = (EditText) view.findViewById(R.id.text_view_price_article);
        textViewSumaArticle = (TextView) view.findViewById(R.id.text_view_suma_article);
        textViewPlannedCount = (TextView) view.findViewById(R.id.planned_count);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferences.getBoolean("show_keyboard",false) == false){
        SetHideNotKeyboard hideBarcode = new SetHideNotKeyboard(getActivity(),editTextBarcodeInvoice);
        SetHideNotKeyboard hideCount = new SetHideNotKeyboard(getActivity(),editTextCountArticle);
        editTextBarcodeInvoice.setOnTouchListener(hideBarcode);
        editTextCountArticle.setOnTouchListener(hideCount);
        }




        textNumberInvoice.setText(String.valueOf(invoiceNumber));

        if (created == CreateType.device.ordinal()){
            view.findViewById(R.id.layout_planned_count).setVisibility(View.GONE);
        }
    }

    public void Listeners() {

        editTextBarcodeInvoice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(!String.valueOf(editTextBarcodeInvoice.getText()).equals("")) {

                        String tempBarcode = String.valueOf(editTextBarcodeInvoice.getText());

                        String barcode = tempBarcode.replace('*','\n');


                        ResultTemplate resultTmp = SqlQuery.getArticleByBarcode(barcode.trim(),
                                getContext(),listTemplate);

                        article =  SqlQuery.getArticleInvoice(resultTmp.getCursor());


                        if(resultTmp.getQuantity()!= -1) {
                            editTextCountArticle.setText(String.valueOf(resultTmp.getQuantity()));
                            article.setQuantityAccount(Float.parseFloat(String.valueOf(resultTmp.getQuantity())));
                        }
                        if (resultTmp.getPrice() != -1) {
                            editTextPriceArticle.setText(String.valueOf(resultTmp.getPrice()));
                            article.setPriceAccount(Float.parseFloat(String.valueOf(resultTmp.getPrice())));
                        }

                        if(article.getArticleId() == 0)
                        {
                            Toast.makeText(getContext(),"Товар не знайдено", Toast.LENGTH_SHORT).show();
                            editTextCountArticleAction(false,false,false);
                            editTextPriceArticleAction(false,false,false);
                            editTextBarcodeInvoice.setText("");
                            editTextBarcodeInvoice.requestFocus();
                        }
                        else
                        {
                            invoiceRow = SqlQuery.getInvoiceRowByArticleId((int) article.getArticleId(), idInvoice, getContext());
                            if(resultTmp.getQuantity()!= -1) {
                                editTextCountArticle.setText(String.valueOf(resultTmp.getQuantity()));
                                invoiceRow.setQuantityAccount(Float.parseFloat(String.valueOf(resultTmp.getQuantity())));
                                article.setQuantityAccount(Float.parseFloat(String.valueOf(resultTmp.getQuantity())));

                            }
                            if (resultTmp.getPrice() != -1) {
                                editTextPriceArticle.setText(String.valueOf(resultTmp.getPrice()));
                                invoiceRow.setPriceAccount(Float.parseFloat(String.valueOf(resultTmp.getPrice())));
                                article.setPriceAccount(Float.parseFloat(String.valueOf(resultTmp.getPrice())));
                            }

                            switch (created) {

                                case deviceType:
                                    if (invoiceRow.getInvoiceRowId() != 0) {
                                        correctionType = CorrectionType.ctUpdate.ordinal();
                                        stateElements(false,true,false,true,true);
                                        editTextCountArticle.requestFocus();
                                    } else {
                                        invoiceRow = article;
                                        stateElements(false,true,false,true,true);
                                        editTextCountArticle.requestFocus();
                                    }
                                    showArticles(invoiceRow, correctionType);
                                    break;

                                case pcType:
                                    if (invoiceRow.getInvoiceRowId() != 0) {

                                        invoiceRow.setBarcode(article.getBarcode());
                                        showArticles(invoiceRow, correctionType);
                                        textViewPlannedCount.setText(String.valueOf(invoiceRow.getQuantity()));
                                        stateElements(false,true,false,true,true);
                                        editTextCountArticle.requestFocus();
                                        editTextCountArticle.selectAll();
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Товар " + article.getName()
                                                + " у накладній \n Не знайдено!!! ", Toast.LENGTH_SHORT).show();
                                        clearTextView();
                                        editTextBarcodeInvoice.requestFocus();
                                    }
                                    break;
                            }
                        }
                    }
                    editTextCountArticle.selectAll();
                    return true;
                }
                return false;
            }
        });

        editTextCountArticle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        invoiceRow.setQuantityAccount(Float.parseFloat(String.valueOf(editTextCountArticle.getText())));

                        float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
                        invoiceRow.setSumaAccount(suma);
                        textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));

                        if(created == CreateType.pc.ordinal()) {
                            updateInvoiceRowWithCondition();
                        }

                        if(created == CreateType.device.ordinal()){
                            stateElements(false,false,true,false,false);
                            editTextPriceArticle.requestFocus();
                        }
                    }
                    catch (NumberFormatException ex){
                        Toast.makeText(getContext(),"Невірна кількість",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_UP)){
                    countIncrement();
                    return true;
                }
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)){
                    countDecrement();
                    return true;
                }
                return false;
            }
        });

        buttonCountIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countIncrement();
            }
        });

        buttonCountDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDecrement();
            }
        });


        editTextPriceArticle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    invoiceRow.setPriceAccount(Float.parseFloat(editTextPriceArticle.getText().toString()));

                    float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
                    invoiceRow.setSumaAccount(suma);
                    textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));

                    saveInvoiceRow();

                    if(invoiceRow.getInvoiceRowId() == 0){
                        clearTextView();
                        invoiceRow.clear();
                        article.clear();
                        stateElements(true,false,false,false,false);
                        editTextBarcodeInvoice.requestFocus();
                    }else if(corrTypeStart == CorrectionType.ctUpdate.ordinal()) {
                        stateElements(false,true,false,true,true);
                        editTextCountArticle.requestFocus();
                    }else {
                        clearTextView();
                        invoiceRow.clear();
                        article.clear();
                        stateElements(true,false,false,false,false);
                        editTextBarcodeInvoice.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void ShowListInvoice()
    {
        Cursor c =  SqlQuery.getListArticle(getContext(),idInvoice);
        invoiceRow = SqlQuery.getArticleFromPosition(c, positionArticle);
        invoiceRow.setInvoiceId(idInvoice);
        invoiceRowCopy.setQuantityAccount(invoiceRow.getQuantityAccount());
        invoiceRowCopy.setPriceAccount(invoiceRow.getPriceAccount());
        invoiceRowCopy.setInvoiceId(idInvoice);

        showArticles(invoiceRow, correctionType);
    }

    public void showArticles(InvoiceRow invoiceRow, int corType) {

        if (corType == CorrectionType.ctInsert.ordinal() || corType == CorrectionType.ctNone.ordinal()) {
            invoiceRow.setQuantity((float) 1.000);
            invoiceRow.setQuantityAccount((float) 1.000);
            editTextCountArticle.setText(String.valueOf(invoiceRow.getQuantity()));
            float suma = invoiceRow.getPrice() * invoiceRow.getQuantity();
            invoiceRow.setPriceAccount(invoiceRow.getPrice());
            invoiceRow.setSumaAccount(suma);
        }

        if(created == CreateType.pc.ordinal()) {
            textViewPlannedCount.setText(String.format("%.3f", invoiceRow.getQuantity()).replace(",", "."));
            invoiceRow.setPriceAccount(invoiceRow.getPrice());
        }
        editTextPriceArticle.setText(String.format("%.2f",invoiceRow.getPriceAccount()).replace(",","."));
        editTextCountArticle.setText(String.format("%.3f", invoiceRow.getQuantityAccount()).replace(",","."));
        textViewSumaArticle.setText(String.format("%.2f",invoiceRow.getSumaAccount()).replace(",","."));

        editTextBarcodeInvoice.setText(invoiceRow.getBarcode());
        textNameArticle.setText(invoiceRow.getName());
        textUnitArticle.setText(invoiceRow.getUnitName());

    }

    public void saveInvoiceRow(){
        invoiceRow.setCorrectionTypeId(correctionType);
        invoiceRow.setQuantityAccount(Float.parseFloat(editTextCountArticle.getText().toString()));
        invoiceRow.setPriceAccount(Float.parseFloat(String.valueOf(editTextPriceArticle.getText())));
        invoiceRow.setSumaAccount(invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount());

        if (correctionType == CorrectionType.ctInsert.ordinal())
        {
            invoiceRow.setQuantity(invoiceRow.getQuantityAccount());
            invoiceRow.setPrice(invoiceRow.getPriceAccount());
            invoiceRow.setSuma(invoiceRow.getSumaAccount());
            invoiceRow.setInvoiceId(idInvoice);
            SqlQuery.insertInvoiceRow(invoiceRow, getContext());
            Toast.makeText(getContext(),R.string.saved,Toast.LENGTH_SHORT).show();
        }
        else
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            String strDate = simpleDateFormat.format(new Date());
            invoiceRow.setDateTimeChange(strDate);
            SqlQuery.updateInvoiceRow(invoiceRow, getContext());
            Toast.makeText(getContext(),R.string.updated,Toast.LENGTH_SHORT).show();
        }
    }

    public void updateInvoiceRowWithCondition(){

        if(isArticlePager() == true){
            try {
                invoiceRow.setQuantityAccount(Float.parseFloat(editTextCountArticle.getText().toString()));
                invoiceRow.setSumaAccount(invoiceRow.getQuantityAccount()*invoiceRow.getPriceAccount());
            }catch (Exception ex){
                Toast.makeText(getContext(),"невірна кількість",Toast.LENGTH_SHORT).show();
            }

            if(invoiceRow.getQuantityAccount() != invoiceRowCopy.getQuantityAccount()
                    || invoiceRow.getPriceAccount() != Float.parseFloat(editTextPriceArticle.getText().toString()) ) {
                if(invoiceRow.getQuantityAccount() > invoiceRow.getQuantity() ) {
                    isOnCreateDialog = true;
                    onCreateDialog();
                }
                else {
                    saveInvoiceRow();
                    clearForm();
                    if(cancelButton == true)
                        getActivity().finish();
                }
            }
        }else {

            if (invoiceRow.getQuantityAccount() > invoiceRow.getQuantity()) {
                isOnCreateDialog = true;
                onCreateDialog();
            } else {
                saveInvoiceRow();
                clearForm();
                if (cancelButton == true)
                    getActivity().finish();
            }
        }
    }

    public void onCreateDialog(){
        final String title = "Попередження";
        String message = "Фактична кількість перевищує планову. Зберегти? ";
        String button1String = "Ні";
        String button2String = "Так";

        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                transitionViewPager();
            }
        });
        ad.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                saveInvoiceRow();
                if(cancelButton == true)
                    getActivity().finish();
                clearForm();
                transitionViewPager();
            }
        });
        ad.setCancelable(false);
        ad.show();
    }

    public void countIncrement(){
        editTextCountArticle.requestFocus();
        float countPlusPlus = Float.parseFloat(String.valueOf(editTextCountArticle.getText()));
        countPlusPlus++;
        invoiceRow.setQuantityAccount(countPlusPlus);
        editTextCountArticle.setText(String.format("%.3f",countPlusPlus).replace(',', '.'));

        float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
        invoiceRow.setSumaAccount(suma);
        textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));
        editTextCountArticle.selectAll();
    }

    public void countDecrement(){
        if((Float.parseFloat(String.valueOf(editTextCountArticle.getText())))>=1) {

            float countMinusMinus = Float.parseFloat(String.valueOf(editTextCountArticle.getText()));
            countMinusMinus--;
            invoiceRow.setQuantityAccount(countMinusMinus);
            editTextCountArticle.setText(String.format("%.3f",countMinusMinus).replace(',', '.'));

            float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
            invoiceRow.setSumaAccount(suma);
            textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));
            editTextCountArticle.selectAll();
        }
    }

    public void clearTextView(){
        textNameArticle.setText("");
        editTextCountArticle.setText("");
        editTextPriceArticle.setText("");
        textUnitArticle.setText("");
        editTextBarcodeInvoice.setText("");
        textViewSumaArticle.setText("");
        textViewPlannedCount.setText("");
    }

    public void clearForm(){
        if(correctionType == CorrectionType.ctUpdate.ordinal()) {
            clearTextView();
            stateElements(true,false,false,false,false);
            editTextBarcodeInvoice.requestFocus();
            invoiceRow.clear();
            invoiceRowCopy.clear();
        }
    };

    public void editTextBarcodeInvoiceAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextBarcodeInvoice.setEnabled(enabled);
        editTextBarcodeInvoice.setFocusable(focusable);
        editTextBarcodeInvoice.setFocusableInTouchMode(focusableInTouch);
    }

    public void editTextCountArticleAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextCountArticle.setEnabled(enabled);
        editTextCountArticle.setFocusable(focusable);
        editTextCountArticle.setFocusableInTouchMode(focusableInTouch);
    }

    public  void  editTextPriceArticleAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextPriceArticle.setEnabled(enabled);
        editTextPriceArticle.setFocusable(focusable);
        editTextPriceArticle.setFocusableInTouchMode(focusableInTouch);
    }

    public void buttonCountMinusAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        buttonCountDecrement.setEnabled(enabled);
        buttonCountDecrement.setFocusable(focusable);
        buttonCountDecrement.setFocusableInTouchMode(focusableInTouch);
    }

    public void buttonCountPlusAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        buttonCountIncrement.setEnabled(enabled);
        buttonCountIncrement.setFocusable(focusable);
        buttonCountIncrement.setFocusableInTouchMode(focusableInTouch);
    }

    public void stateElements(boolean editTextBarcode, boolean editTextCount, boolean editTextPrice,
                              boolean buttonCountIncrement, boolean buttonCountDecrement){

        editTextBarcodeInvoiceAction(editTextBarcode,editTextBarcode,editTextBarcode);
        editTextCountArticleAction(editTextCount,editTextCount,editTextCount);
        editTextPriceArticleAction(editTextPrice,editTextPrice,editTextPrice);
        buttonCountMinusAction(buttonCountDecrement,false,false);
        buttonCountPlusAction(buttonCountIncrement,false,false);
    }


    public void transitionViewPager()
    {
        GainInvoiceEditArticlesActivity activity = null;
        try {
            activity = (GainInvoiceEditArticlesActivity) getActivity();
            if(activity.articlesPager != null){
                if(activity.leftButton == true) {
                    activity.pagerAdapter.notifyDataSetChanged();
                    Log.d("item_count", String.valueOf(activity.articlesPager.getCurrentItem()));
                    activity.articlesPager.setCurrentItem(activity.articlesPager.getCurrentItem() - 1);

                }

                else if(activity.rightButton == true) {
                    activity.pagerAdapter.notifyDataSetChanged();
                    Log.d("item_count", String.valueOf(activity.articlesPager.getCurrentItem()));
                    activity.articlesPager.setCurrentItem(activity.articlesPager.getCurrentItem() + 1);
                }
            }
        }catch (Exception ex){
        }

    }
    public Boolean isArticlePager() {

        ViewPager pager = null;
        GainInvoiceEditArticlesActivity activity = null;
        try {
            activity = (GainInvoiceEditArticlesActivity) getActivity();
            pager = activity.articlesPager;
            return true;
        }catch (Exception ex){
            return false;
        }
    }

}
