package com.eventx.wikiment.network;

import com.eventx.wikiment.networkModels.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10")
    Call<SearchResult> search(
            @Query("gpssearch") String gpssearch
    );
}
