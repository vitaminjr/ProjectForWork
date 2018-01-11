package com.example.vitaminjr.mobileacounting.fragments;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.BaseKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.helpers.CorrectionType;
import com.example.vitaminjr.mobileacounting.helpers.CreateType;
import com.example.vitaminjr.mobileacounting.helpers.SetHideNotKeyboard;
import com.example.vitaminjr.mobileacounting.interfaces.OnBackPressedListener;
import com.example.vitaminjr.mobileacounting.models.Article;
import com.example.vitaminjr.mobileacounting.models.BarcodeTamplateInfo;
import com.example.vitaminjr.mobileacounting.models.InventoryAction;
import com.example.vitaminjr.mobileacounting.models.ResultTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by vitaminjr on 19.07.16.
 */
public class InventoryEditArticlesFragment extends Fragment  implements OnBackPressedListener{

    private String invoiceNumber;
    private long idInventory;
    private int created;
    InventoryAction inventoryAction;
    private int correctionType;
    private int position;
    Article article;

    private float countTemp = 0;
    boolean markSave = false;

    List<BarcodeTamplateInfo> listTemplate;
    View.OnKeyListener onKeyListener;


    Button buttonCountPlus;
    Button buttonCountMinus;
    TextView textNumberInvoice;
    TextView textNameArticle;
    TextView textUnitArticle;
    EditText editTextCountArticle;
    EditText editTextBarcodeInventory;
    TextView textViewFactCountArticle;
    TextView textViewPlannedCount;

    public static InventoryEditArticlesFragment newInstance(String invoiceNumber, long idInventory, int created){

        InventoryEditArticlesFragment inventoryEditArticlesFragment = new InventoryEditArticlesFragment(invoiceNumber, idInventory, created);

        return inventoryEditArticlesFragment;
    }

    public InventoryEditArticlesFragment(String number, long idInventory, int created)
    {
        inventoryAction = new InventoryAction();
        this.invoiceNumber = number;
        this.idInventory = idInventory;
        this.created = created;

    }

    public static InventoryEditArticlesFragment newInstance(String invoiceNumber, long idInventory, int created, int position, int corType){

        InventoryEditArticlesFragment inventoryEditArticlesFragment = new InventoryEditArticlesFragment(invoiceNumber, idInventory, created, position, corType);

        return inventoryEditArticlesFragment;
    }

    public InventoryEditArticlesFragment(String number, long idInventory, int created, int position, int corType)
    {
        inventoryAction = new InventoryAction();
        this.invoiceNumber = number;
        this.idInventory = idInventory;
        this.created = created;
        this.position = position;
        this.correctionType = corType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_edit_articles_inventory,container,false);
        initGui(view);

        if(correctionType == CorrectionType.ctUpdate.ordinal())
        {
            getInventoryActionFromPosition();
            showArticles(inventoryAction,CorrectionType.ctUpdate.ordinal());
            view.findViewById(R.id.linear_layout_fact).setVisibility(LinearLayout.GONE);
            editTextBarcodeInventoryAction(false,false,false);
            editTextCountArticleAction(true,true,true);
            buttonCountMinus.setEnabled(true);
            buttonCountPlus.setEnabled(true);
            editTextCountArticle.requestFocus();
            editTextCountArticle.selectAll();
            countTemp = inventoryAction.getQuantity();

        }else {
            listTemplate = SqlQuery.getBarcodeTemplates(getContext());
            editTextCountArticleAction(false,false,false);
            buttonCountMinus.setEnabled(false);
            buttonCountPlus.setEnabled(false);
        }



        Listeners();
        return view;
    }

    public void initGui(View view){

        buttonCountPlus = (Button) view.findViewById(R.id.button_count_plus);
        buttonCountMinus = (Button) view.findViewById(R.id.button_count_minus);
        textNumberInvoice = (TextView) view.findViewById(R.id.text_number_inventory);
        textNameArticle = (TextView) view.findViewById(R.id.text_name_article);
        textUnitArticle = (TextView) view.findViewById(R.id.text_unit_article);
        editTextCountArticle = (EditText) view.findViewById(R.id.edit_text_count_article);
        editTextBarcodeInventory = (EditText) view.findViewById(R.id.edit_text_barcode_inventory);
        textViewFactCountArticle = (TextView) view.findViewById(R.id.text_view_fact_count_article);
        textViewPlannedCount = (TextView) view.findViewById(R.id.planned_count);

        textNumberInvoice.setText(invoiceNumber.toString());

        SharedPreferences preferencesKeyBoard = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferencesKeyBoard.getBoolean("show_keyboard",false) == false){
            SetHideNotKeyboard hideBarcode = new SetHideNotKeyboard(getActivity(),editTextBarcodeInventory);
            SetHideNotKeyboard hideCount = new SetHideNotKeyboard(getActivity(),textViewFactCountArticle);
            editTextBarcodeInventory.setOnTouchListener(hideBarcode);
            textViewFactCountArticle.setOnTouchListener(hideCount);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (created == CreateType.device.ordinal() || preferences.getBoolean("count",false) == false) {
            view.findViewById(R.id.layout_planned_count).setVisibility(View.GONE);
        }
    }

    public void getInventoryActionFromPosition(){
         inventoryAction = SqlQuery.getInventoryActionFromPosition(SqlQuery.getListInventoryAction(getContext(),idInventory),position);
    }

    public void Listeners() {

        editTextBarcodeInventory.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == 59)
                {
                    editTextBarcodeInventory.setText("");
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(!String.valueOf(editTextBarcodeInventory.getText()).equals("")) {

                        String tempBarcode = String.valueOf(editTextBarcodeInventory.getText());

                        String barcode = tempBarcode.replace('*','\n');

                        ResultTemplate resultTmp = SqlQuery.getArticleByBarcode(barcode.trim(),
                                getContext(),listTemplate);

                        inventoryAction  =  SqlQuery.getArticle(resultTmp.getCursor());


                        if(resultTmp.getQuantity()!= -1) {
                            editTextCountArticle.setText(String.valueOf(resultTmp.getQuantity()));
                            inventoryAction.setQuantity(Float.parseFloat(String.valueOf(resultTmp.getQuantity())));
                        }
                        else if(inventoryAction.getArticleId()!=0)
                        {
                            editTextCountArticle.setText(String.valueOf(resultTmp.getQuantity()));
                            inventoryAction.setQuantity(Float.parseFloat(String.valueOf(resultTmp.getQuantity())));
                        }

                        if(inventoryAction.getArticleId() == 0)
                        {
                            Toast.makeText(getContext(),"Товар не знайдено", Toast.LENGTH_SHORT).show();
                            editTextCountArticleAction(false,false,false);
                            editTextBarcodeInventory.setText("");
                            editTextBarcodeInventory.requestFocus();
                            buttonCountMinus.setEnabled(false);
                            buttonCountPlus.setEnabled(false);
                        }
                        else {
                            editTextBarcodeInventoryAction(false, false, false);
                            editTextCountArticleAction(true, true, true);
                            editTextCountArticle.requestFocus();
                            buttonCountMinus.setEnabled(true);
                            buttonCountPlus.setEnabled(true);

                            showArticles(inventoryAction,CorrectionType.ctInsert.ordinal());
                            editTextCountArticle.selectAll();
                        }
                    }

                    return true;
                }
                markSave = true;
                return false;
            }
        });


                onKeyListener = new View.OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == 59) {

                            actionCountSaveInventory();

                            return false;
                        }
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            actionCountSaveInventory();

                        }
                        return false;
                    }
                };

        editTextCountArticle.setOnKeyListener(onKeyListener);



        buttonCountPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextCountArticle.requestFocus();
                float countPlusPlus = Float.parseFloat(String.valueOf(editTextCountArticle.getText()));
                countPlusPlus++;
                inventoryAction.setQuantity(countPlusPlus);
                editTextCountArticle.setText(String.format("%.3f",countPlusPlus).replace(',', '.'));

            }
        });

        buttonCountMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((Float.parseFloat(String.valueOf(editTextCountArticle.getText())))>=1) {

                    float countMinusMinus = Float.parseFloat(String.valueOf(editTextCountArticle.getText()));
                    countMinusMinus--;
                    inventoryAction.setQuantity(countMinusMinus);
                    editTextCountArticle.setText(String.format("%.3f",countMinusMinus).replace(',', '.'));
                }
            }
        });
    }

    public void clearTextView(){
        textNameArticle.setText("");
        editTextCountArticle.setText("");
        textUnitArticle.setText("");
        editTextBarcodeInventory.setText("");
        textViewFactCountArticle.setText("");
        textViewPlannedCount.setText("");

    }

    public void showArticles(InventoryAction inventoryAction, int corType) {

        if (corType == CorrectionType.ctInsert.ordinal() || corType == CorrectionType.ctNone.ordinal()) {
            inventoryAction.setQuantity((float) 1.000);
            editTextCountArticle.setText(String.valueOf(inventoryAction.getQuantity()));
            if(inventoryAction.getBarcode() != null)
                editTextBarcodeInventory.setText(inventoryAction.getBarcode().toString());

        }
        if(corType == CorrectionType.ctUpdate.ordinal()){
            try {
                editTextBarcodeInventory.setText(inventoryAction.getBarcode().toString());
                if(created == CreateType.pc.ordinal() && inventoryAction.getArticleId() != 0){
                    float quantity = SqlQuery.getInventoryRowsQuantity(getContext(),idInventory,inventoryAction.getArticleId());
                    textViewPlannedCount.setText(String.format("%.3f",quantity));
                }
                float factCount =  SqlQuery.getInventoryActionQuantity(getContext(),idInventory,inventoryAction.getArticleId());
                textViewFactCountArticle.setText(String.format("%.3f",factCount));
            }catch (NullPointerException ex){}
        }

        editTextCountArticle.setText(String.format("%.3f", inventoryAction.getQuantity()).replace(",","."));
        article = inventoryAction.getArticle();
        textNameArticle.setText(article.getName());
        textUnitArticle.setText(article.getUnitName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (inventoryAction.getArticleId() != 0) {
                onCreateDialog();
            } else {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);


    }

    public void editTextBarcodeInventoryAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextBarcodeInventory.setEnabled(enabled);
        editTextBarcodeInventory.setFocusable(focusable);
        editTextBarcodeInventory.setFocusableInTouchMode(focusableInTouch);
    }

    public void editTextCountArticleAction(boolean enabled, boolean focusable, boolean focusableInTouch){
        editTextCountArticle.setEnabled(enabled);
        editTextCountArticle.setFocusable(focusable);
        editTextCountArticle.setFocusableInTouchMode(focusableInTouch);
    }

    public void onCreateDialog(){
        final String title = "Попередження";
        String message = " Зберегти введений товар? ";
        String button1String = "Ні";
        String button2String = "Так";

        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setNegativeButton(button1String,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });
        ad.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                saveInventoryAction();
                clearTextView();
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
                Toast.makeText(getContext(), "Дані збережено", Toast.LENGTH_LONG)
                        .show();
            }
        });

        ad.setCancelable(true);

        ad.show();
    }

    @Override
    public void onBackPressed() {

        if (inventoryAction.getArticleId() != 0 && (correctionType == CorrectionType.ctInsert.ordinal()
                || correctionType == CorrectionType.ctNone.ordinal()))
            onCreateDialog();
        else if(correctionType == CorrectionType.ctUpdate.ordinal()
                && inventoryAction.getQuantity() != Float.parseFloat(editTextCountArticle.getText().toString()))
            onCreateDialog();
        else {
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().finish();
        }
    }

    public void saveInventoryAction(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        String strDate = simpleDateFormat.format(new Date());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String strTime = simpleTimeFormat.format(new Date());
        inventoryAction.setDate(strDate);
        inventoryAction.setTime(strTime);
        inventoryAction.setActionTypeId(CorrectionType.ctInsert.ordinal());
        inventoryAction.setInventoryId(idInventory);
        inventoryAction.setBarcode(editTextBarcodeInventory.getText().toString());
        SqlQuery.insertInventoryAction(inventoryAction,getContext());
    }

    public void actionCountSaveInventory(){
        try {
            inventoryAction.setQuantity(Float.parseFloat(String.valueOf(editTextCountArticle.getText())));
        } catch (NumberFormatException ex) {
            Toast.makeText(getContext(), "Невірна кількість", Toast.LENGTH_SHORT).show();
        }
        if(correctionType == CorrectionType.ctUpdate.ordinal())
        {
            if(inventoryAction.getQuantity() > countTemp){
                inventoryAction.setQuantity(inventoryAction.getQuantity() - countTemp);
            }
            else
                inventoryAction.setQuantity(inventoryAction.getQuantity() - countTemp);
        }
        saveInventoryAction();

        if(correctionType == CorrectionType.ctInsert.ordinal() || correctionType == CorrectionType.ctNone.ordinal()) {
            clearTextView();
            editTextBarcodeInventoryAction(true, true, true);
            editTextCountArticleAction(false, false, false);
            buttonCountMinus.setEnabled(false);
            buttonCountPlus.setEnabled(false);
            editTextBarcodeInventory.requestFocus();
            inventoryAction.clear();
        }
        if(correctionType == CorrectionType.ctUpdate.ordinal()){
            Toast.makeText(getContext(),"Збережено", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }


    }

}
