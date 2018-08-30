package com.eventx.wikiment.asyncTasks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.eventx.wikiment.database.DatabaseContract;
import com.eventx.wikiment.networkModels.SearchResult;
import com.google.gson.Gson;

public class AddResponse extends AsyncTask {
    private String query;
    private SearchResult response;
    private SQLiteDatabase db;

    public AddResponse(String query, SearchResult response, SQLiteDatabase db) {
        this.query = query;
        this.response = response;
        this.db = db;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ContentValues cv = new ContentValues();
        Gson gson = new Gson();
        cv.put(DatabaseContract.CacheTable.RESPONSE, gson.toJson(response).getBytes());
        Cursor cursor=db.query(DatabaseContract.CacheTable.TABLE_NAME,null,DatabaseContract.CacheTable.QUERY+"=?", new String[]{query},null,null,null);
        if(cursor.moveToNext()){
            db.update(DatabaseContract.CacheTable.TABLE_NAME,cv,DatabaseContract.CacheTable.QUERY+"=?", new String[]{query});

        }else {
            cv.put(DatabaseContract.CacheTable.QUERY, query);

            db.insert(DatabaseContract.CacheTable.TABLE_NAME, null, cv);
        }
        cursor.close();
        Cursor cursor1=db.query(DatabaseContract.CacheTable.TABLE_NAME,null,null,null,null,null,null);
        if(cursor1.getCount()>10){
            cursor1.moveToFirst();
            String rowId = cursor1.getString(cursor1.getColumnIndex(DatabaseContract.CacheTable._ID));

            db.delete(DatabaseContract.CacheTable.TABLE_NAME, DatabaseContract.CacheTable._ID + "=?",  new String[]{rowId});
        }
        return null;
    }
}
