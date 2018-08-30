package com.eventx.wikiment.network;

import android.content.Context;
import android.util.Log;

import com.eventx.wikiment.util.AppConstants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static ApiInterface apiInterface;

    public static ApiInterface getApiInterface(Context context) {
        if (apiInterface == null) {
            Log.i("cachetest", "getApiInterface: " + context.getCacheDir());
            File httpCacheDirectory = new File(context.getCacheDir(), "httpCache");
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addNetworkInterceptor(chain -> {

                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxAge(2, TimeUnit.MINUTES)
                                .build();
                        Request onlineRequest = chain.request().newBuilder()
                                .header("Cache-Control", cacheControl.toString())
                                .build();
                        return chain.proceed(onlineRequest);

                    })
                    .addInterceptor(chain -> {

                        Log.i("cachetest", "getApiInterface: " + chain.request().url());
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxStale(7, TimeUnit.DAYS)
                                .build();

                        Request offlineRequest = chain.request().newBuilder()
                                .cacheControl(cacheControl)
                                .build();

                        return chain.proceed(offlineRequest);

                    })
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;

    }
}
