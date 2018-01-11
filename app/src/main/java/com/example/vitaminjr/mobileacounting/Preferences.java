package com.example.vitaminjr.mobileacounting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vitaminjr on 17.02.17.
 */

public class Preferences {

    public static void saveBooleanSetting(String key, boolean value, Context context){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(key, value);
        ed.commit();
    }

    public static Boolean loadBooleanSetting(String key,Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean result = preferences.getBoolean(key,false);
        return result;
    }

}
