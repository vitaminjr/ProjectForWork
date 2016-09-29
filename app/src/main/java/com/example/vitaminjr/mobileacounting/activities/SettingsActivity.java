package com.example.vitaminjr.mobileacounting.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;

/**
 * Created by vitaminjr on 29.09.16.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Налаштування збережено!!!",Toast.LENGTH_SHORT).show();

    }

}
