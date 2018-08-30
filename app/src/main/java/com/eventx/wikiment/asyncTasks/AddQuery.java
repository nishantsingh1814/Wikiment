package com.eventx.wikiment.asyncTasks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.eventx.wikiment.database.DatabaseContract;

public class AddQuery extends AsyncTask<String, Void, Void> {
    private SQLiteDatabase db;

    public AddQuery(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String[] selection = new String[1];
        selection[0] = strings[0];
        Cursor cursor=db.query(DatabaseContract.SearchTable.TABLE_NAME,
                null,
                DatabaseContract.SearchTable.QUERY+"=?",
                selection,
                null, null, null
        );
        if(cursor.moveToNext()){
            cursor.close();
            return null;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.SearchTable.QUERY, strings[0]);
        db.insert(DatabaseContract.SearchTable.TABLE_NAME, null, cv);
        return null;
    }
}
