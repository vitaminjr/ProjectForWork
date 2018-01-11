package com.example.vitaminjr.mobileacounting.registration;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Registration {
    private Activity activity;

    //
    public Registration(Activity activity) {
        this.activity = activity;
    }

    //-----------------------------------------------------------------------------------------------------------------
    public byte[] encrypt(String ivStr, String keyStr, byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ivStr.getBytes());
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes());
        byte[] keyBytes = sha.digest();

        return encrypt(ivBytes, keyBytes, bytes);
    }

    //-----------------------------------------------------------------------------------------------------------------
    public byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    //-----------------------------------------------------------------------------------------------------------------
    public byte[] decrypt(String ivStr, String keyStr, byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ivStr.getBytes());
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes());
        byte[] keyBytes = sha.digest();

        return decrypt(ivBytes, keyBytes, bytes);
    }

    //-----------------------------------------------------------------------------------------------------------------
    public byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String encryptStrAndToBase64(String ivStr, String keyStr, String enStr) throws Exception {
        byte[] bytes = encrypt(keyStr, keyStr, enStr.getBytes("UTF-8"));
        return new String(Base64.encode(bytes, Base64.DEFAULT), "UTF-8");
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String decryptStrAndFromBase64(String ivStr, String keyStr, String deStr) throws Exception {
        byte[] bytes = decrypt(keyStr, keyStr, Base64.decode(deStr.getBytes("UTF-8"), Base64.DEFAULT));
        return new String(bytes, "UTF-8");
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String convertToIntString(String value) {
        String retString = "";
        //
        for (int i = 0; i < value.length(); i++) {
            retString += String.valueOf((int) value.charAt(i));
            if (i != value.length() - 1) retString += "-";
        }
        //
        return retString;
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String convertFromIntString(String value) throws UnsupportedEncodingException {
        String retString = "";
        int i = 0, start = 0;
        int number = 0;
        while (i < value.length()) {
            start = i;
            while ((i < value.length()) && (value.charAt(i) != '-')) i++;
            //
            number = Integer.valueOf(value.substring(start, i).trim());
            //
            retString += (char) number;
            i++;
        }
        //
        retString = new String(retString.getBytes(), "Cp1251");
        return retString;
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String getImei() {
        String retString = "";
        //
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        retString = telephonyManager.getDeviceId();
        //
        return retString;
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String getMacAddress() {
        String retString = "";
        //
        WifiManager wifiMan = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        //
        String mac = wifiInf.getMacAddress();
        for (int i = 0; i < mac.length(); i++) {
            if (mac.charAt(i) != ':') {
                retString += mac.charAt(i);
            }
        }
        //
        return retString;
    }

    //-----------------------------------------------------------------------------------------------------------------
    public String getStrictMacAddress() {
        String retString = "";
        //
        WifiManager wifiMan = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        //
        String mac = wifiInf.getMacAddress();
        String oneChar = "";
        for (int i = 0; i < mac.length(); i++) {
            if (mac.charAt(i) == ':') {
                retString += (char) Integer.parseInt(oneChar, 16);
                oneChar = "";
            } else {
                oneChar += mac.charAt(i);
            }
        }
        retString += (char) Integer.parseInt(oneChar, 16);
        //
        return retString;
    }

    //----------------------------------------------------------------------------------------------------------------
    public String getRandomKey() {
        String retString = "";
        //
        int min = 0;
        int max = 9;
        //
        Random r = new Random();
        retString += String.valueOf(r.nextInt(max - min + 1) + min);
        retString += String.valueOf(r.nextInt(max - min + 1) + min);
        retString += String.valueOf(r.nextInt(max - min + 1) + min);
        //
        return retString;
    }
    //----------------------------------------------------------------------------------------------------------------
    public String getActivationCode(int key) {
        String retString = "";
        //
        try {
            retString = String.valueOf(Long.parseLong(getMacAddress(),16) * key);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        return retString;
    }
    //----------------------------------------------------------------------------------------------------------------
    public String generateKey(int key, String codeActivation) {
        String retString = "";
        //
        String serial = "";
        try {
            long iCodeActivation = Long.parseLong(codeActivation);
            iCodeActivation /= key;
            //
            String newKey = getRandomKey();
            retString = convertToIntString(encryptStrAndToBase64("",
                    newKey,
                    String.valueOf(iCodeActivation))) + "-" + newKey;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        return retString;
    }
    //----------------------------------------------------------------------------------------------------------------
    public String generateKey(String key, String codeActivation) {
        String retString = "";
        //
        String serial = "";
        try {
            serial = decryptStrAndFromBase64("", key, convertFromIntString(codeActivation));
            //
            String newKey = getRandomKey();
            retString = convertToIntString(encryptStrAndToBase64("", newKey, serial)) + "-" + newKey;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        return retString;
    }
    //----------------------------------------------------------------------------------------------------------------
    public boolean checkRegistration(String serialCode) {
        try {
            String key = serialCode.substring(serialCode.length() - 3, serialCode.length());
            String fromIntString = convertFromIntString(serialCode.substring(0, serialCode.length() - 3));
            String decryptRegistrationKey = decryptStrAndFromBase64("", key, fromIntString);
            //
            if (Long.parseLong(getMacAddress(),16) == Long.parseLong(decryptRegistrationKey.trim())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
