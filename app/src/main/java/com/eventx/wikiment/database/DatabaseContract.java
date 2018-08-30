package com.eventx.wikiment.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static final class SearchTable implements BaseColumns {
        public static final String TABLE_NAME = "search_table";
        public static final String QUERY = "query";
    }
    public static final class CacheTable implements BaseColumns{
        public static final String TABLE_NAME = "cache_table";
        public static final String QUERY = "query";
        public static final String RESPONSE = "response";
    }


}
