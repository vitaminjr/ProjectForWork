package com.example.vitaminjr.mobileacounting;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import com.example.vitaminjr.mobileacounting.interfaces.ReturnEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jcifs.smb.SmbFile;

/**
 * Created by vitaminjr on 23.09.16.
 */

public class ExportFile extends AsyncTask<Void,Integer,Boolean> {

    ProgressDialog pd;
    Context context;
    String address;
    String addressFile;
    ReturnEventListener listener;

    public ExportFile(Context context, String address, String addressFile) {

        this.context = context;
        this.address = address;
        this.addressFile = addressFile;
        createProgressBarImportFile();
        listener = (ReturnEventListener) context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setProgress(0);
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        return exportFile(addressFile,address);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pd.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        pd.dismiss();

        listener.returnResult(aBoolean);
    }

    public void createProgressBarImportFile(){

        pd = new ProgressDialog(context,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);

        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        pd.setMessage("Вивантаження");

        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));

        pd.setIndeterminate(false);

        pd.setCancelable(false);

    }

    boolean exportFile(String fileAddress, String urlAddress)
    {
        try
        {
            SmbFile localAuditPCFile = new SmbFile(urlAddress);

            InputStream  in  = new FileInputStream(fileAddress);

            OutputStream out = localAuditPCFile.getOutputStream();

            byte[] buf = new byte[1024];
            int len;
            float sum = 0;

            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
