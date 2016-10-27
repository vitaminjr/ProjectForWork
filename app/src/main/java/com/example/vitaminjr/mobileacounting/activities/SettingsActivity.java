package com.example.vitaminjr.mobileacounting.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.example.vitaminjr.mobileacounting.file.OpenFileDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitaminjr on 29.09.16.
 */
public class SettingsActivity extends PreferenceActivity {

    EditTextPreference textPreferenceInput;
    EditTextPreference textPreferenceOutput;
    EditTextPreference textPreferencServerInput;
    EditTextPreference textPreferenceServerOutput;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        addPreferencesFromResource(R.xml.pref);

        context = this;

        Preference preference = findPreference("default_settings");
        Preference loadSettings = findPreference("load_settings");
        textPreferenceInput = (EditTextPreference) findPreference("address_input");
        textPreferenceOutput = (EditTextPreference) findPreference("address_output");
        textPreferencServerInput = (EditTextPreference) findPreference("url_download_db");
        textPreferenceServerOutput = (EditTextPreference) findPreference("url_upload_db");

        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                onCreateDialog();

                return true;
            }
        });
        loadSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                OpenFileDialog dialog = new OpenFileDialog(context);
                dialog.show();
                dialog.setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        try {
                            List<String> list =  readFile(fileName);
                            saveSettings(list);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }
        });
        textPreferenceInput.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Налаштування збережено!!!",Toast.LENGTH_SHORT).show();
    }

    public void saveDefaultSettings() {

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("url_download_db","smb://computerName/filePath/mobile_accounting.odf");
        ed.putString("url_upload_db","smb://computerName/catalog/");
        ed.putString("address_input","/sdcard/mobileAcounting/mobile_accounting.odf");
        ed.putString("address_output","/sdcard/mobileAcounting/");
        ed.commit();

    }

    public void saveSettings(List<String> list) {

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = sPref.edit();

        for(int i = 0; i<list.size(); i++){
        switch (i) {
            case 0:
                ed.putString("url_download_db", list.get(0));
                break;
            case 1:
                ed.putString("url_upload_db", list.get(1));
                break;
            case 2:
                ed.putString("address_input", list.get(2));
                break;
            case 3:
                ed.putString("address_output", list.get(3));
                break;
        }
        }
        ed.commit();
        loadDefaultSettings();
    }


    public void loadDefaultSettings(){

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        textPreferenceInput.setText(preferences.getString("address_input",""));
        textPreferenceOutput.setText(preferences.getString("address_output",""));
        textPreferencServerInput.setText(preferences.getString("url_download_db",""));
        textPreferenceServerOutput.setText(preferences.getString("url_upload_db",""));
    }

    public static List<String> readFile(String fileName) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        File file = exists(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    list.add(s);
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private static File exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
        return file;
    }


    public void onCreateDialog(){
        final String title = "Попередження";
        String message = "Ви дійсно хочете встановити налашутвання за замовучанням?";
        String button1String = "Ні";
        String button2String = "Так";

        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setNegativeButton(button1String,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getApplicationContext(),"Налаштування по замовчуванню",Toast.LENGTH_SHORT).show();
                saveDefaultSettings();
                loadDefaultSettings();
            }
        });
        ad.setCancelable(true);
        ad.show();
    }


}
