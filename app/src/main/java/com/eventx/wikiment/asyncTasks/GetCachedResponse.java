package com.eventx.wikiment.asyncTasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.eventx.wikiment.database.DatabaseContract;
import com.eventx.wikiment.interfaces.CachedResponseFetched;
import com.eventx.wikiment.networkModels.SearchResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetCachedResponse extends AsyncTask<String,Void,SearchResult> {
    private SQLiteDatabase db;

    public GetCachedResponse(SQLiteDatabase db, CachedResponseFetched cachedResponseFetched) {
        this.db = db;
        this.cachedResponseFetched = cachedResponseFetched;
    }

    private CachedResponseFetched cachedResponseFetched;

    @Override
    protected SearchResult doInBackground(String... strings) {
        Cursor cursor=db.query(DatabaseContract.CacheTable.TABLE_NAME,null,DatabaseContract.CacheTable.QUERY+"=?", new String[]{strings[0]},null,null,null,null);
        if(cursor.moveToNext()) {
            byte[] responsebytes = cursor.getBlob(cursor.getColumnIndex(DatabaseContract.CacheTable.RESPONSE));
            cursor.close();
            String responseString = new String(responsebytes);
            Gson gson = new Gson();
            return gson.fromJson(responseString, new TypeToken<SearchResult>() {

            }.getType());
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(SearchResult searchResult) {
        super.onPostExecute(searchResult);
        cachedResponseFetched.onCachedResponseFetched(searchResult);
    }
}
