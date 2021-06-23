package com.smartlab.wpqrscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

class DatabaseHelper extends SQLiteOpenHelper {
     Context context;
    static final String TAG = "DbHelper";
     static final String DATABASE_NAME = "wpqrscanner.db";
     static final Integer DATABASE_VERSION = 2;

     static final String TABLE_NAME = "qrlibrary";
     static final String COLUMN_ID = "_id";
     static final String COLUMN_CODE = "Code";
     static final String COLUMN_NAME = "Name";
     static final String COLUMN_TYPE = "Type";
     static final String COLUMN_LOC = "Loc";
     static final String COLUMN_LCORDATE = "LcorrDate";
     static final String COLUMN_LPPMDATE = "LPPMDate";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CODE + " TEXT," + COLUMN_NAME + " TEXT," + COLUMN_TYPE + " TEXT," +
                COLUMN_LOC + " TEXT," + COLUMN_LCORDATE + " TEXT," + COLUMN_LPPMDATE + " TEXT)";
        //Toast.makeText(context,query,Toast.LENGTH_SHORT).show();
        db.execSQL(query);
        String sql2 = "create table wpconfigmd (_ID int primary key, username text, password text, server text, active int,version text)"; //
        db.execSQL(sql2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS wpconfigmd");// drops the old database
        Log.i(TAG, "onUpdated table 'wpconfig'");

        onCreate(db);
    }

    public boolean addData(String code, String name, String type, String loc, String LCorrDate, String LPPMDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CODE, code);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_LOC, loc);
        cv.put(COLUMN_LCORDATE, LCorrDate);
        cv.put(COLUMN_LPPMDATE, LPPMDate);
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public void  deleteData(){
        try {
            if (isTableExists()) {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("DROP TABLE " + TABLE_NAME);
                String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_CODE + " TEXT," + COLUMN_NAME + " TEXT," + COLUMN_TYPE + " TEXT," +
                        COLUMN_LOC + " TEXT," + COLUMN_LCORDATE + " TEXT," + COLUMN_LPPMDATE + " TEXT)";
                db.execSQL(query);
                String sql2 = "create table wpconfigmd (_ID int primary key, username text, password text, server text, active int,version text)"; //
                db.execSQL(sql2);
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Boolean isTableExists() {
        SQLiteDatabase checkDB = this.getReadableDatabase();


        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public boolean openDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        File f = new File(db.getPath());
        return f.exists();
    }
}

