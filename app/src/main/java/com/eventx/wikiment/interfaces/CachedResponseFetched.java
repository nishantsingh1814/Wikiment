package com.eventx.wikiment.interfaces;

import com.eventx.wikiment.networkModels.SearchResult;

public interface CachedResponseFetched {
    void onCachedResponseFetched(SearchResult searchResult);
}
