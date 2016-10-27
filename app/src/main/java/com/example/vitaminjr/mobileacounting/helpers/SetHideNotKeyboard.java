package com.example.vitaminjr.mobileacounting.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by vitaminjr on 08.10.16.
 */
public class SetHideNotKeyboard implements View.OnTouchListener {
    Activity activity;
    TextView textView;

    public SetHideNotKeyboard(Activity activity, TextView textView) {
        this.activity = activity;
        this.textView = textView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        return true;
    }
}
