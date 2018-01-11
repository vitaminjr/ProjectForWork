package com.example.vitaminjr.mobileacounting.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.databases.SqlQuery;
import com.example.vitaminjr.mobileacounting.file.OpenFileDialog;
import com.example.vitaminjr.mobileacounting.registration.Registration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by vitaminjr on 18.10.16.
 */

public class RegistrationActivity extends AppCompatActivity{

    public static final String CODE_REGISTER = "codeRegister";
    public static final int RESULT_EXIT = 1;
    public static final int RESULT_UNREGISTER = 2;
    public static final int REGISTER_REGISTER = 3;
    EditText textKey;
    EditText textCodeRegister;
    Button buttonRegister;
    Button unregisterVersion;
    Button exit;
    Registration registration;
    String activationCode;
    Activity activity;
    OpenFileDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        activity = this;
        initGui();
    }

    public void initGui(){
        textKey = (EditText) findViewById(R.id.key_activation);
        textCodeRegister = (EditText) findViewById(R.id.code_register);
        buttonRegister = (Button) findViewById(R.id.button_register);
        unregisterVersion = (Button) findViewById(R.id.button_unregister);
        exit = (Button) findViewById(R.id.exit_program);
        getKeyRegister();
        initListeners();
    }

    public void getKeyRegister(){
        registration = new Registration(activity);
        activationCode = registration.getActivationCode(0x54);
        textKey.setText(activationCode);
    }

    private void initListeners() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRegister()){
                    Toast.makeText(getApplicationContext(),"Зареєстровано",Toast.LENGTH_SHORT).show();
                    saveText();
                    finish();
                }else
                    Toast.makeText(getApplicationContext(),"Реєстрація невдала",Toast.LENGTH_SHORT).show();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("exit",true);
                setResult(RESULT_EXIT, intent);
                finish();
            }
        });

        unregisterVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("unregister",true);
                setResult(RESULT_UNREGISTER, intent);
                finish();
            }
        });

        textCodeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textCodeRegister.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                dialog = new OpenFileDialog(activity);
                dialog.show();
                dialog.setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        try {
                            textCodeRegister.setText(readFile(fileName).toString());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        textKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("TAG",textKey.getText());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getBaseContext(), "Скопійовано в буфер", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void saveText() {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(CODE_REGISTER, textCodeRegister.getText().toString());
        ed.commit();
    }

    public boolean checkRegister(){

        if (registration.checkRegistration(textCodeRegister.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }

    public static String readFile(String fileName) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        File file = exists(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString().trim();
    }

    private static File exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
        return file;
    }
}
