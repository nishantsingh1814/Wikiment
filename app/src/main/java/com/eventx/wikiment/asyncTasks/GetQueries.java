package com.eventx.wikiment.asyncTasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.eventx.wikiment.interfaces.QueriesFetched;
import com.eventx.wikiment.database.DatabaseContract;

import java.util.ArrayList;

public class GetQueries extends AsyncTask<Void, Void, ArrayList<String>> {
    private SQLiteDatabase db;
    private QueriesFetched queriesFetched;

    public GetQueries(SQLiteDatabase db, QueriesFetched queriesFetched) {
        this.db = db;
        this.queriesFetched = queriesFetched;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> searchResults = new ArrayList<>();

        Cursor cursor = db.query(DatabaseContract.SearchTable.TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            searchResults.add(cursor.getString(cursor.getColumnIndex(DatabaseContract.SearchTable.QUERY)));
        }
        cursor.close();
        return searchResults;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        queriesFetched.onQueriesFetched(strings);
    }
}
