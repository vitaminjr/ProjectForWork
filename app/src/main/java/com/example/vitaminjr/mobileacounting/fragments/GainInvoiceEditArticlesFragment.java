package com.example.vitaminjr.mobileacounting.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListener;
import com.example.vitaminjr.mobileacounting.interfaces.OnSomeEventListenerArticles;
import com.example.vitaminjr.mobileacounting.models.Article;
import com.example.vitaminjr.mobileacounting.models.BarcodeTamplateInfo;
import com.example.vitaminjr.mobileacounting.models.InvoiceRow;
import com.example.vitaminjr.mobileacounting.models.ResultTemplate;

import java.util.Calendar;
import java.util.List;

/**
 * Created by vitaminjr on 19.07.16.
 */
public class GainInvoiceEditArticlesFragment extends Fragment  implements OnBackPressedListener{

    private String invoiceNumber;
    private int correctionType;
    private int idInvoice;
    private int positionArticle;
    private int created;
    private float countCopy;

    boolean priceDevice = false;
    boolean countPc = false;
    boolean markSave = false;

    private final int pcType = 0;
    private final int deviceType = 1;
    List<BarcodeTamplateInfo> listTemplate;

    InvoiceRow invoiceRow;
    InvoiceRow article;

    Button buttonCountPlus;
    Button buttonCountMinus;
    TextView textNumberInvoice;
    TextView textNameArticle;
    TextView textUnitArticle;
    EditText editTextCountArticle;
    EditText editTextBarcodeInvoice;
    EditText editTextPriceArticle;
    TextView textViewSumaArticle;
    TextView textViewPlannedCount;

    public static GainInvoiceEditArticlesFragment newInstance(String invoiceNumber, int typeFragment, int idInvoice, int created){

        GainInvoiceEditArticlesFragment gainInvoiceEditArticlesFragment = new GainInvoiceEditArticlesFragment(invoiceNumber, typeFragment, idInvoice,created);

        return gainInvoiceEditArticlesFragment;
    }

    public static GainInvoiceEditArticlesFragment newInstance(String invoiceNumber, int typeFragment, int idInvoice, int position, int created){

        GainInvoiceEditArticlesFragment gainInvoiceEditArticlesFragment = new GainInvoiceEditArticlesFragment(invoiceNumber, typeFragment, idInvoice, position,created);

        return gainInvoiceEditArticlesFragment;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_edit_articles_invoice,container,false);
        initGui(view);

        listTemplate = SqlQuery.getBarcodeTamplates(getContext());

        if(correctionType == CorrectionType.ctUpdate.ordinal() ||
                correctionType == CorrectionType.ctCollate.ordinal())
            ShowListInvoice();

        Listeners();
        return view;
    }

    public void initGui(View view){

        buttonCountPlus = (Button) view.findViewById(R.id.button_count_plus);
        buttonCountMinus = (Button) view.findViewById(R.id.button_count_minus);
        textNumberInvoice = (TextView) view.findViewById(R.id.text_number_invoice);
        textNameArticle = (TextView) view.findViewById(R.id.text_name_article);
        textUnitArticle = (TextView) view.findViewById(R.id.text_unit_article);
        editTextCountArticle = (EditText) view.findViewById(R.id.edit_text_count_article);
        editTextBarcodeInvoice = (EditText) view.findViewById(R.id.edit_text_barcode_invoice);
        editTextPriceArticle = (EditText) view.findViewById(R.id.text_view_price_article);
        textViewSumaArticle = (TextView) view.findViewById(R.id.text_view_suma_article);
        textViewPlannedCount = (TextView) view.findViewById(R.id.planned_count);

        if (created == CreateType.device.ordinal()){
            view.findViewById(R.id.layout_planned_count).setVisibility(View.GONE);
        }

        if(correctionType == CorrectionType.ctInsert.ordinal() || correctionType == CorrectionType.ctUpdate.ordinal()){
            editTextCountArticleAction(false,false,false);
            buttonCountPlusAction(false,false,false);
            buttonCountMinusAction(false,false,false);
            editTextPriceArticleAction(false,false,false);
            editTextBarcodeInvoiceActiom(true,true,true);
        }

        if(created == CreateType.pc.ordinal())
            editTextPriceArticleAction(false,false,false);

        textNumberInvoice.setText(String.valueOf(invoiceNumber));

        if(created == CreateType.pc.ordinal() && correctionType == CorrectionType.ctCollate.ordinal())
        {
            editTextBarcodeInvoiceActiom(false,false,false);
            editTextCountArticleAction(true,true,true);
            editTextCountArticle.requestFocus();
        }

        if(created == CreateType.device.ordinal() && correctionType == CorrectionType.ctCollate.ordinal())
        {
            editTextBarcodeInvoiceActiom(false,false,false);
            editTextPriceArticleAction(false,false,false);
            editTextCountArticleAction(true,true,true);
            editTextCountArticle.requestFocus();
        }

    }

    public void Listeners() {

        editTextBarcodeInvoice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(!String.valueOf(editTextBarcodeInvoice.getText()).equals("")) {

                        String barcode = String.valueOf(editTextBarcodeInvoice.getText());


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
                                        editTextBarcodeInvoiceActiom(false,false,false);
                                        editTextPriceArticleAction(false,false,false);
                                        buttonCountPlusAction(true,false,false);
                                        buttonCountMinusAction(true,false,false);
                                        editTextCountArticleAction(true,true,true);
                                        editTextCountArticle.requestFocus();

                                    }
                                    else {
                                        invoiceRow = article;
                                        editTextBarcodeInvoiceActiom(false,false,false);
                                        editTextPriceArticleAction(false,false,false);
                                        editTextCountArticleAction(true,true,true);
                                        buttonCountPlusAction(true,false,false);
                                        buttonCountMinusAction(true,false,false);
                                        editTextCountArticle.requestFocus();

                                    }
                                    showArticles(invoiceRow, correctionType);

                                    break;
                                case pcType:
                                    if (invoiceRow.getInvoiceRowId() != 0) {
                                        correctionType = CorrectionType.ctUpdate.ordinal();
                                        showArticles(invoiceRow, correctionType);
                                        textViewPlannedCount.setText(String.valueOf(invoiceRow.getQuantity()));
                                        editTextBarcodeInvoiceActiom(false,false,false);
                                        editTextCountArticleAction(true,true,true);
                                        buttonCountPlusAction(true,false,false);
                                        buttonCountMinusAction(true,false,false);
                                        editTextCountArticle.requestFocus();
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
                    return true;
                }
                markSave = true;
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
                    }
                    catch (NumberFormatException ex){
                        Toast.makeText(getContext(),"Невірна кількість",Toast.LENGTH_SHORT).show();
                    }

                    float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
                    invoiceRow.setSumaAccount(suma);
                    textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));
                    if(created == CreateType.pc.ordinal() && correctionType == CorrectionType.ctUpdate.ordinal()){
                        countPc = true;
                    }

                    updateInvoiceRowWithCondition();

                    if(created == CreateType.device.ordinal()){

                        editTextCountArticleAction(false,false,false);
                        buttonCountPlusAction(false,false,false);
                        buttonCountMinusAction(false,false,false);
                        editTextPriceArticleAction(true,true,true);
                        editTextPriceArticle.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        buttonCountPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextCountArticle.requestFocus();
                float countPlusPlus = Float.parseFloat(String.valueOf(editTextCountArticle.getText()));
                countPlusPlus++;
                invoiceRow.setQuantityAccount(countPlusPlus);
                editTextCountArticle.setText(String.format("%.3f",countPlusPlus).replace(',', '.'));

                float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
                invoiceRow.setSumaAccount(suma);
                textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));

                //updateInvoiceRowWithCondition();

            }
        });

        buttonCountMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((Float.parseFloat(String.valueOf(editTextCountArticle.getText())))>=1) {

                    float countMinusMinus = Float.parseFloat(String.valueOf(editTextCountArticle.getText()));
                    countMinusMinus--;
                    invoiceRow.setQuantityAccount(countMinusMinus);
                    editTextCountArticle.setText(String.format("%.3f",countMinusMinus).replace(',', '.'));

                    float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
                    invoiceRow.setSumaAccount(suma);
                    textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));

                    //updateInvoiceRowWithCondition();

                }

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


                    priceDevice = true;
                    addOrUpdateInvoiceRows(correctionType);
                    if((created == CreateType.device.ordinal())
                            && (correctionType == CorrectionType.ctInsert.ordinal())
                            || (correctionType == CorrectionType.ctUpdate.ordinal())){
                        editTextPriceArticleAction(false,false,false);
                        editTextBarcodeInvoiceActiom(true,true,true);
                        editTextBarcodeInvoice.requestFocus();
                    }
                    if(correctionType == CorrectionType.ctCollate.ordinal())
                    {
                        editTextPriceArticleAction(false,false,false);
                        editTextCountArticleAction(true,true,true);
                        editTextCountArticle.requestFocus();

                    }

                    return true;
                }
                return false;
            }
        });


        editTextPriceArticle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextPriceArticle.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        });

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

    public void ShowListInvoice()
    {

        Cursor c =  SqlQuery.getListArticle(getContext(),idInvoice);
        invoiceRow = SqlQuery.getArticleFromPosition(c, positionArticle);
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

        if(created == CreateType.pc.ordinal())
        {
            textViewPlannedCount.setText(String.format("%.3f", invoiceRow.getQuantity()).replace(",","."));
        }

        editTextCountArticle.setText(String.format("%.3f", invoiceRow.getQuantityAccount()).replace(",","."));
        editTextBarcodeInvoice.setText(invoiceRow.getBarcode());
        textNameArticle.setText(invoiceRow.getName());

            editTextPriceArticle.setText(String.format("%.2f",invoiceRow.getPriceAccount()).replace(",","."));
            textViewSumaArticle.setText(String.format("%.2f",invoiceRow.getSumaAccount()).replace(",","."));

        textUnitArticle.setText(invoiceRow.getUnitName());
        countCopy = invoiceRow.getQuantityAccount();
    }

    public void addOrUpdateInvoiceRows(int correctionType){


        invoiceRow.setCorrectionTypeId(correctionType);
        invoiceRow.setPriceAccount(Float.parseFloat(String.valueOf(editTextPriceArticle.getText())));
        invoiceRow.setQuantityAccount(Float.parseFloat(String.valueOf(editTextCountArticle.getText())));
        invoiceRow.setSumaAccount(invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount());


        if (correctionType == CorrectionType.ctInsert.ordinal())
        {
            invoiceRow.setQuantity(invoiceRow.getQuantityAccount());
            invoiceRow.setPrice(invoiceRow.getPriceAccount());
            invoiceRow.setSuma(invoiceRow.getSumaAccount());
            invoiceRow.setInvoiceId(idInvoice);
            SqlQuery.insertInvoiceRow(invoiceRow, getContext());
            clearTextView();
            Toast.makeText(getContext(),"Збережено",Toast.LENGTH_SHORT).show();
        }
        else
        {
            invoiceRow.setDateTimeChange(String.valueOf(Calendar.DATE));
            SqlQuery.updateInvoiceRow(invoiceRow, getContext());
            Toast.makeText(getContext(),"Оновлено",Toast.LENGTH_SHORT).show();

            if(created == CreateType.pc.ordinal() && correctionType == CorrectionType.ctCollate.ordinal())
            {
                editTextCountArticleAction(true,true,true);
                editTextCountArticle.requestFocus();
            }
            else if(priceDevice == true
                    && (correctionType ==  CorrectionType.ctInsert.ordinal()
                    ||  correctionType == CorrectionType.ctUpdate.ordinal())) {
                clearTextView();
                invoiceRow.clear();
                article.clear();
                priceDevice = false;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            saveInvoiceRows();

            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    public void editTextBarcodeInvoiceActiom(boolean enabled, boolean focusable, boolean focusableInTouch){
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
        buttonCountMinus.setEnabled(enabled);
        buttonCountMinus.setFocusable(focusable);
        buttonCountMinus.setFocusableInTouchMode(focusableInTouch);
    }

    public void buttonCountPlusAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        buttonCountPlus.setEnabled(enabled);
        buttonCountPlus.setFocusable(focusable);
        buttonCountPlus.setFocusableInTouchMode(focusableInTouch);
    }


    public void onCreateDialog(){
        final String title = "Попередження";
        String message2 = "Ви не зберегли зміни. Зберегти?";
        String message = "Фактична кількість перевищує планову. Зберегти? ";
        String button1String = "Ні";
        String button2String = "Так";
        String button3String = "Відміна";

        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setNegativeButton(button1String,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                addOrUpdateInvoiceRows(correctionType);
                if(countPc == true){
                    clearTextView();
                    editTextCountArticleAction(false,false,false);
                    editTextBarcodeInvoiceActiom(true,true,true);
                    editTextBarcodeInvoice.requestFocus();
                    countPc = false;
                }
                Toast.makeText(getContext(), "Дані збережено", Toast.LENGTH_LONG)
                        .show();


            }
        });

        ad.setCancelable(true);

        ad.show();
    }


     public void onCreateDialogBackPressed(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Попередження!!!")
                    .setMessage("Введіть кількість")
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    @Override
    public void onBackPressed() {

            saveInvoiceRows();
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().finish();

    }

    public void updateInvoiceRowWithCondition(){
        markSave = false;

        if(created == CreateType.pc.ordinal() && invoiceRow.getQuantityAccount() > invoiceRow.getQuantity() )
        {
            onCreateDialog();
        }
        else if(correctionType == CorrectionType.ctUpdate.ordinal() || created == CreateType.pc.ordinal() && correctionType == CorrectionType.ctCollate.ordinal())
            addOrUpdateInvoiceRows(correctionType);
    }

    public void saveInvoiceRows(){
        if(invoiceRow.getInvoiceRowId() != 0 || invoiceRow.getArticleId() != 0){

            try{
            invoiceRow.setQuantityAccount(Float.parseFloat(editTextCountArticle.getText().toString()));
            invoiceRow.setPriceAccount(Float.parseFloat(editTextPriceArticle.getText().toString()));

            float suma = invoiceRow.getPriceAccount()*invoiceRow.getQuantityAccount();
            invoiceRow.setSumaAccount(suma);
            invoiceRow.setCorrectionTypeId(correctionType);
            textViewSumaArticle.setText(String.format("%.2f",suma).replace(",","."));

            if(created == CreateType.device.ordinal())
                addOrUpdateInvoiceRows(correctionType);
            else
                updateInvoiceRowWithCondition();
            }
            catch (Exception ex){}
        }

    }
}
