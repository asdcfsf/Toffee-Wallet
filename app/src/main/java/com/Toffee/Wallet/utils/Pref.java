package com.Toffee.Wallet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.Toffee.Wallet.BuildConfig;
import com.Toffee.Wallet.Responsemodel.LoginResp;

import java.util.Locale;

public class Pref {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public static final String SELECTED_LANGUAGE ="SELECTED_LANGUAGE" ;
    private static final String PREF_NAME = BuildConfig.APPLICATION_ID+"reward_";
    public static   final String ENABLE_SESSION="ENABLE_SESSION";
    public static   final String ENABLE_Banner="ENABLE_Banner";
    public   final boolean SESSION=false;
    public   final String LOGIN="login";
    public   final String FIRST_TIME="firstime";
    public   final String USER_ID="user_id";
    public   final String PERSON_ID="person_ID";
    public   final String EMAIL="email";
    public   final String EMAIL_VERIFIED="EMAIL_VERIFIED";
    public   final String PHONE="phone";
    public   final String REFLINK="REFLINK";
    public  final String NAME="name";
    public  final String PROFILE="usersrprofile";
    public  final String ATYPE="atype";
    public  final String COUNTRY="COUNTRY";
    public   final String PASSWORD="password";
    public   final String REFER_ID="referid";
    public   final String FROM_REFER_ID="";
    public   final String TOKEN="token";
    public   final String P_TOKEN="p_token";
    public  final String WALLET="walletbal";
    public  final String PROMO_WALLET="promo_walletbal";

    public final String SPIN_LIMIT = "spin_limits_";
    public final String SCRATCH_LIMIT = "scratch_limits_";
    public final String SCRATCH_COUNTS = "SCCOUNTS";
    public final String SCRATCH_AD_INTER = "SCRATCH_AD_INTER";
    public final String SCRATCH_AD_COUNT = "SCRATCH_AD_COUNT";
    public final String SPIN_AD_COUNT = "SPIN_AD_COUNT";
    public final String QUIZ_AD_COUNT = "QUIZ_AD_COUNT";
    public final String SPIN_AD_INTER = "SPIN_AD_INTER";
    public final String Quiz_AD_INTER = "Quiz_AD_INTER";
    public final String SPIN_COUNTS = "SPCOUNTS";
    public final String Quiz_COUNTS = "QZCOUNTS";
    public final String successRef = "successRef";
    public final String pendingRef = "pendingRef";
    public   final String NOTI="noti";
    public   final String appLanguage="appLanguage";

    @SuppressLint("CommitPrefEdits")
    public Pref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public static String getString(Context context,String id) {
        SharedPreferences  pref = context.getSharedPreferences(PREF_NAME,0);
        return pref.getString(id, "");
    }

    public static String User_id(Context context) {
        SharedPreferences  pref = context.getSharedPreferences(PREF_NAME,0);
        return pref.getString("user_id", "");
    }

    public String getString(String id) {
        return pref.getString(id, "");
    }

    public String Profile() {
        return pref.getString(PROFILE, "");
    }

    public String User_id() {
        return pref.getString(USER_ID, "");
    }

    public int getBalance() {
        return pref.getInt(WALLET, 0);
    }

    public String getPromoBalance() {
        return String.valueOf(pref.getInt(PROMO_WALLET, 0));
    }

    public void setData(String id, String val) {
        editor.putString(id, val);
        editor.commit();
    }

    public void UpdateWallet(int val) {
        editor.putInt(WALLET, val);
        editor.commit();
    }

    public void setIntData(String id, int val) {
        editor.putInt(id, val);
        editor.commit();
    }

    public  boolean isUserLogin(){
        return pref.getBoolean(LOGIN,false);
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key,false);
    }

    public  int getInt(String key){
        return pref.getInt(key,0);
    }

    public int getAdCount(String type) {
        int x=0;
        switch (type){
            case "spin":
                x=pref.getInt(SPIN_AD_COUNT, 0);
                break;

            case "scratch":
                x=pref.getInt(SCRATCH_AD_COUNT, 0);
                break;

            case "quiz":
                x=pref.getInt(QUIZ_AD_COUNT, 0);
                break;
        }

        return x;
    }

    public void setAdCount(String type,int val) {
        switch (type){
            case "spin":
                if (val == 0) {
                    editor.putInt(SPIN_AD_COUNT, 0);
                    editor.commit();
                } else {
                    editor.putInt(SPIN_AD_COUNT, pref.getInt(SPIN_AD_COUNT, 0) + val);
                    editor.commit();
                }
                break;

            case "scratch":
                if (val == 0) {
                    editor.putInt(SCRATCH_AD_COUNT, 0);
                    editor.commit();
                } else {
                    editor.putInt(SCRATCH_AD_COUNT, pref.getInt(SCRATCH_AD_COUNT, 0) + val);
                    editor.commit();
                }
                break;

            case "quiz":
                if (val == 0) {
                    editor.putInt(QUIZ_AD_COUNT, 0);
                    editor.commit();
                } else {
                    editor.putInt(QUIZ_AD_COUNT, pref.getInt(QUIZ_AD_COUNT, 0) + val);
                    editor.commit();
                }
                break;
        }

    }

    public int getAdInterval(String type) {
        int x=0;
        switch (type){
            case "spin":
                x=pref.getInt(SPIN_AD_INTER, 0);
                break;

            case "scratch":
                x=pref.getInt(SCRATCH_AD_INTER, 0);
                break;

            case "quiz":
                x=pref.getInt(Quiz_AD_INTER, 0);
                break;
        }

        return x;
    }

    public void setAdInterval(String type,int val) {
        switch (type){
            case "spin":
                editor.putInt(SPIN_AD_INTER, val);
                editor.commit();
                break;

            case "scratch":
                editor.putInt(SCRATCH_AD_INTER, val);
                editor.commit();
                break;

            case "quiz":
                editor.putInt(Quiz_AD_INTER, val);
                editor.commit();
                break;
        }
    }

    public void setNotiData(String id, int val) {
        editor.putInt(id, val);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void Logout(){
        editor.putString(USER_ID,"");
        editor.putString(EMAIL,"");
        editor.putString(PHONE,"");
        editor.putString(PASSWORD,"");
        editor.putString(NAME,"");
        editor.putString(REFER_ID,"");
        editor.putString(TOKEN,"");
        editor.putString(P_TOKEN,"");
        editor.putString(PERSON_ID,"");
        editor.putString(ATYPE,"");
        editor.putBoolean(LOGIN,false);
        editor.apply();
    }

    public void updateLanguage(String langCode){
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = _context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLanguage(langCode);
    }

    public String getLangCode(){
        return  pref.getString(appLanguage,"en");
    }

    private void setLanguage(String langCode){
        editor.putString(appLanguage,langCode);
        editor.apply();
    }

    public void saveUserData(LoginResp resp,String pass,String Actype) {
        setBoolean(LOGIN,true);
        editor.putString(NAME,resp.getUser().getName());
        editor.putString(EMAIL,resp.getUser().getEmail());
        editor.putString(PERSON_ID,resp.getUser().getPersonId());
        editor.putString(PASSWORD,pass);
        editor.putString(ATYPE,Actype);
        editor.putString(PROFILE,resp.getUser().getProfile());
        editor.putInt(PROMO_WALLET,resp.getUser().getPromo_balance());
        editor.putString(REFER_ID,resp.getUser().getRefferalId());
        editor.putString(USER_ID,resp.getUser().getCustId());
        editor.putInt(WALLET, resp.getUser().getBalance());
        setNotiData(NOTI,resp.getNoti());
        editor.apply();

    }
}
