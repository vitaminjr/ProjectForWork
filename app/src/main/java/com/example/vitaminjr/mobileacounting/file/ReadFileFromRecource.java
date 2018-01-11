package com.example.vitaminjr.mobileacounting.file;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.example.vitaminjr.mobileacounting.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vitaminjr on 26.12.16.
 */
public class ReadFileFromRecource {
    public String  getStringFromRawFile(Context context) {
        Resources r = context.getResources();
        InputStream is = r.openRawResource(R.raw.mobile_accounting);
        String myText = null;
        try {
            myText = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  myText;
    }

    private String  convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            baos.write(i);
            i = is.read();
        }
        return baos.toString();
    }
}
