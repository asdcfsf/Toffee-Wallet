package com.Toffee.Wallet.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.HomeResp;
import com.Toffee.Wallet.Responsemodel.SettingResp;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_EXT = ".db";
    private static final String TABLE_HOME = "home";
    private static final String TABLE_CAT = "category";
    private static final String TABLE_NATIVE = "native_ad";
    private static final String KEY_AUTO_ID = "id";
    private static final String ID = "id";
    private static final String ICON = "icon";
    private static final String TITLE = "title";
    private static final String CAT_THEME = "cat_theme";
    private static final String SUBTITLE = "subtitle";
    private static final String BTN_NAME = "btn_name";
    private static final String IMAGE = "image";
    private static final String DESCRIPTION = "description";
    private static final String BTN_ACTION = "btn_action";
    private static final String BTN_THEME = "btn_theme";
    private static final String URL = "url";
    private static final String CAT = "cat";
    private static final String CAT_TITLE = "cat";
    private static final String CAT_ID = "id";
    private static final String COIN = "coin";
    private static final String COLOR = "color";
    private static final String VIEW_MODE = "view_mode";
    private static final String TYPE = "type";
    private static final String BACKGROUND_COLOR = "background_color";
    private static final String BTN_COLOR = "btn_color";
    private static final String CAT_TYPE = "cat_type";
    private static final String BTN_BACKGROUND = "btn_background";
    private static final String TABLE_SPIN = "spin";
    private static final String COIN_ID = "coinid";
    private static final String NATIVE_ID = "nativeid";

    public DatabaseHandler(Context context) {
        super(context, context.getString(R.string.app_name).replace(" ", "") + DATABASE_EXT, null, DATABASE_VERSION);
        getReadableDatabase();
        Fun.log("DB onCreate "+ "onCreate: " + "1");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Fun.log("DB onCreate "+ "onCreate: " + "2");
        String CREATE_TABLE_NAME_FAV_POST = "CREATE TABLE " + TABLE_HOME + "("
                + KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CAT + " TEXT ,"
                + ICON + " TEXT ,"
                + TITLE + " TEXT ,"
                + SUBTITLE + " TEXT ,"
                + BTN_NAME + " TEXT ,"
                + BTN_THEME + " TEXT ,"
                + BACKGROUND_COLOR + " TEXT ,"
                + BTN_COLOR + " TEXT ,"
                + BTN_BACKGROUND + " TEXT ,"
                + BTN_ACTION + " TEXT ,"
                + URL + " TEXT ,"
                + TYPE + "  TEXT " + ")";

        String CREATE_TABLE_NAME_CAT = "CREATE TABLE " + TABLE_CAT + "("
                + KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CAT + " TEXT ,"
                + CAT_THEME + " TEXT ,"
                + VIEW_MODE + " TEXT ,"
                + CAT_TYPE + " TEXT ,"
                + TITLE + " TEXT " + ")";

        String CREATE_TABLE_NAME_SPIN = "CREATE TABLE " + TABLE_SPIN + "("
                + KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COIN_ID + " TEXT ,"
                + COIN + " TEXT ,"
                + COLOR + " TEXT "+ ")";

        db.execSQL(CREATE_TABLE_NAME_CAT);
        db.execSQL(CREATE_TABLE_NAME_FAV_POST);
        db.execSQL(CREATE_TABLE_NAME_SPIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPIN);
        onCreate(db);
    }

    public void insert(List<HomeResp> home,List<HomeResp> cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i<cat.size(); i++){
            ContentValues values = new ContentValues();
            values.put(CAT,cat.get(i).getId());
            values.put(TITLE,cat.get(i).getTitle());
            values.put(CAT_THEME,cat.get(i).getCat_theme());
            values.put(VIEW_MODE,cat.get(i).getView_mode());
            values.put(CAT_TYPE,cat.get(i).getCat_type());
            db.insert(TABLE_CAT, null, values);
        }

        for(int i=0; i<home.size(); i++){
            ContentValues values = new ContentValues();
            values.put(TITLE,home.get(i).getTitle());
            values.put(SUBTITLE,home.get(i).getSubtitle());
            values.put(CAT,home.get(i).getCat());
            values.put(ICON,home.get(i).getIcon());
            values.put(BTN_NAME,home.get(i).getBtn_name());
            values.put(BTN_ACTION,home.get(i).getBtn_action());
            values.put(URL,home.get(i).getUrl());
            values.put(TYPE,home.get(i).getType());
            values.put(BTN_COLOR,home.get(i).getBtn_color());
            values.put(BACKGROUND_COLOR,home.get(i).getBackground_color());
            values.put(BTN_THEME,home.get(i).getBtn_theme());
            values.put(BTN_BACKGROUND,home.get(i).getBtn_background());
            db.insert(TABLE_HOME, null, values);
        }
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<HomeResp> getHomeList(int id) {
        ArrayList<HomeResp> modelDBArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_HOME + " where cat="+id;
        @SuppressLint("Recycle") Cursor cur = db.rawQuery(query, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    @SuppressLint("Range") int MainID = cur.getInt(cur.getColumnIndex(KEY_AUTO_ID));
                    @SuppressLint("Range") HomeResp home = new HomeResp();
                    home.setId(MainID);
                    home.setTitle(cur.getString(cur.getColumnIndex(TITLE)));
                    home.setSubtitle(cur.getString(cur.getColumnIndex(SUBTITLE)));
                    home.setIcon(cur.getString(cur.getColumnIndex(ICON)));
                    home.setBtn_name(cur.getString(cur.getColumnIndex(BTN_NAME)));
                    home.setBtn_action(cur.getString(cur.getColumnIndex(BTN_ACTION)));
                    home.setUrl(cur.getString(cur.getColumnIndex(URL)));
                    home.setType(cur.getString(cur.getColumnIndex(TYPE)));
                    home.setBackground_color(cur.getString(cur.getColumnIndex(BACKGROUND_COLOR)));
                    home.setBtn_color(cur.getString(cur.getColumnIndex(BTN_COLOR)));
                    home.setBtn_background(cur.getInt(cur.getColumnIndex(BTN_BACKGROUND)));
                    home.setCat(cur.getInt(cur.getColumnIndex(CAT)));
                    modelDBArrayList.add(home);
                } while (cur.moveToNext());
            }
        }
        return modelDBArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<HomeResp> getCat() {
        ArrayList<HomeResp> modelDBArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_CAT;
        Cursor cur = db.rawQuery(query, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    @SuppressLint("Range") int MainID = cur.getInt(cur.getColumnIndex(KEY_AUTO_ID));
                    @SuppressLint("Range") HomeResp home = new HomeResp();
                    home.setId(cur.getInt(cur.getColumnIndex(CAT)));
                    home.setTitle(cur.getString(cur.getColumnIndex(TITLE)));
                    home.setCat_theme(cur.getInt(cur.getColumnIndex(CAT_THEME)));
                    home.setView_mode(cur.getInt(cur.getColumnIndex(VIEW_MODE)));
                    home.setCat_type(cur.getString(cur.getColumnIndex(CAT_TYPE)));
                    modelDBArrayList.add(home);
                } while (cur.moveToNext());
            }
        }
        return modelDBArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<SettingResp.SpinItem> getSpin() {
        ArrayList<SettingResp.SpinItem> modelDBArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_SPIN;
        Cursor cur = db.rawQuery(query, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    @SuppressLint("Range") SettingResp.SpinItem spin = new SettingResp.SpinItem();
                    spin.setId(cur.getString(cur.getColumnIndex(COIN_ID)));
                    spin.setCoin(cur.getString(cur.getColumnIndex(COIN)));
                    spin.setColor(cur.getString(cur.getColumnIndex(COLOR)));
                    modelDBArrayList.add(spin);
                } while (cur.moveToNext());
            }
        }
        return modelDBArrayList;
    }

    public void removeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM home"); //delete all rows in a table
        db.execSQL("DELETE FROM category"); //delete all rows in a table
        db.execSQL("DELETE FROM spin"); //delete all rows in a table
        db.close();
    }

    public void insertSpin(List<SettingResp.SpinItem> spin) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i<spin.size(); i++){
            ContentValues values = new ContentValues();
            values.put(COIN_ID,spin.get(i).getId());
            values.put(COIN,spin.get(i).getCoin());
            values.put(COLOR,spin.get(i).getColor());
            db.insert(TABLE_SPIN, null, values);
        }
        db.close();
    }
}
