package com.at.bd_dictionary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ImageView;


import com.at.bd_dictionary.model.Dictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DictionaryDBOpenHelper extends SQLiteOpenHelper {
    private static DictionaryDBOpenHelper helperInstance;

    public static final String DB_NAME = "dictionary";

    public static String DB_PATH;
    private SQLiteDatabase database;
    private Context context;

    public static final String DICTIONARY_TABLE = "words";
    public static final String ID_FIELD = "word_id";
    public static final String TU = "word_tu";
    public static final String NGHIA = "word_nghia";
    public static final String PHIENAM = "word_phienam";
    public static final String DONGNGHIA = "word_dongnghia";
    public static final String TRAINGHIA = "word_trainghia";
    public static final String ANHMINHHOA = "word_anhminhhoa";

    private DictionaryDBOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;

        Log.e("In Constructor", "Contructor of DatabaseHelper");
        String packageName = context.getPackageName();
        DB_PATH = "/data/data/" + packageName + "/databases/";

        Log.w("DB_PATH", "are" + DB_PATH);
        this.database = openDatabase();
    }

    public static DictionaryDBOpenHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new DictionaryDBOpenHelper(context);
        }
        return helperInstance;
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDatabase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    private void createDatabase() {
        boolean dbExists = checkDB();
        if (!dbExists) {
            this.getReadableDatabase();
            Log.e(getClass().getName(), "Database doesn't exist. Copying database from assets...");
            copyDatabase();
        } else {
            Log.e(getClass().getName(), "Database already exists");
        }
    }

    private void copyDatabase() {
        try {
            InputStream dbInputStream = context.getAssets().open(DB_NAME);
            String path = DB_PATH + DB_NAME;
            OutputStream dbOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[4096];
            int readCount = 0;
            while ((readCount = dbInputStream.read(buffer)) > 0) {
                dbOutputStream.write(buffer, 0, readCount);
            }

            dbInputStream.close();
            dbOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkDB() {
        String path = DB_PATH + DB_NAME;
        File file = new File(path);
        if (file.exists()) {
            Log.e(getClass().getName(), "Database already exists");
            return true;
        }
        Log.e(getClass().getName(), "Database does not exists");
        return false;
    }

    public synchronized void close() {
        if (this.database != null) {
            this.database.close();
        }
    }

    // query
    public ArrayList<Dictionary> getAllwords() {
        ArrayList<Dictionary> allwords = new ArrayList<Dictionary>();
        Cursor cursor = this.database.query(DICTIONARY_TABLE, null, null, null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String tu = cursor.getString(cursor.getColumnIndex(TU));
                String nghia = cursor.getString(cursor.getColumnIndex(NGHIA));
                String phienam = cursor.getString(cursor.getColumnIndex(PHIENAM));
                String dongnghia = cursor.getString(cursor.getColumnIndex(DONGNGHIA));
                String trainghia = cursor.getString(cursor.getColumnIndex(TRAINGHIA));
                String anhminhhoa = cursor.getString(cursor.getColumnIndex(ANHMINHHOA));

                Dictionary words = new Dictionary();
                words.setTu(tu);
                words.setNghia(nghia);
                words.setPhienam(phienam);
                words.setDongnghia(dongnghia);
                words.setTrainghia(trainghia);
                words.setAnhminhhoa(anhminhhoa);
                allwords.add(words);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return allwords;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

}