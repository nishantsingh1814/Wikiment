package com.eventx.wikiment.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Wikiment.db";

    public static final int VERSION = 11;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static DatabaseOpenHelper sInstance;

    public static synchronized DatabaseOpenHelper getInstance(Context context) {


        if (sInstance == null) {
            sInstance = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createSearchTable(sqLiteDatabase);
        createCacheTable(sqLiteDatabase);
    }

    private void createCacheTable(SQLiteDatabase sqLiteDatabase) {
        String createCacheTable = "CREATE TABLE IF NOT EXISTS  " + DatabaseContract.CacheTable.TABLE_NAME + " (" +
                DatabaseContract.CacheTable._ID + " INTEGER PRIMARY KEY, " +
                DatabaseContract.CacheTable.RESPONSE+" BLOB, " +
                DatabaseContract.CacheTable.QUERY + " TEXT);";
        sqLiteDatabase.execSQL(createCacheTable);
    }

    private void createSearchTable(SQLiteDatabase sqLiteDatabase) {
        String createSearchTable = "CREATE TABLE IF NOT EXISTS  " + DatabaseContract.SearchTable.TABLE_NAME + " (" +
                DatabaseContract.SearchTable._ID + " INTEGER PRIMARY KEY, " +
                DatabaseContract.SearchTable.QUERY + " TEXT);";
        sqLiteDatabase.execSQL(createSearchTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
