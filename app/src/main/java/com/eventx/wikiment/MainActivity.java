package com.eventx.wikiment;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.eventx.wikiment.adapters.WikiAdapter;
import com.eventx.wikiment.asyncTasks.AddQuery;
import com.eventx.wikiment.asyncTasks.AddResponse;
import com.eventx.wikiment.asyncTasks.GetCachedResponse;
import com.eventx.wikiment.asyncTasks.GetQueries;
import com.eventx.wikiment.database.DatabaseContract;
import com.eventx.wikiment.database.DatabaseOpenHelper;
import com.eventx.wikiment.interfaces.CachedResponseFetched;
import com.eventx.wikiment.interfaces.OnItemClick;
import com.eventx.wikiment.interfaces.QueriesFetched;
import com.eventx.wikiment.network.ApiClient;
import com.eventx.wikiment.network.ApiInterface;
import com.eventx.wikiment.networkModels.SearchResult;
import com.eventx.wikiment.util.AppConstants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements QueriesFetched, OnItemClick, CachedResponseFetched {

    private RecyclerView mResultRv;
    private WikiAdapter mResultAdapter;
    private ArrayList<SearchResult.Query.Pages> mResultList;
    private SQLiteDatabase db;
    private DatabaseOpenHelper dbHelper;
    private SimpleCursorAdapter mAdapter;
    private ArrayList<String> suggestionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    private void initialize() {
        suggestionsList = new ArrayList<>();
        mResultRv = findViewById(R.id.search_result_rv);
        mResultList = new ArrayList<>();
        mResultAdapter = new WikiAdapter(this, this, mResultList);
        mResultRv.setLayoutManager(new LinearLayoutManager(this));
        mResultRv.setAdapter(mResultAdapter);
        dbHelper = DatabaseOpenHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        final String[] from = new String[]{DatabaseContract.SearchTable.QUERY};
        final int[] to = new int[]{R.id.suggestion_tv};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_item,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetQueries(db, this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menu_search);
        final MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor searchCursor = mAdapter.getCursor();
                if (searchCursor.moveToPosition(position)) {
                    String selectedItem = searchCursor.getString(1);
                    searchView.setQuery(selectedItem, true);
                }

                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.trim();
                if (query.length() == 0) {
                    Snackbar.make(searchView, "Please enter a query", Snackbar.LENGTH_SHORT).show();
                    return true;
                }
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                new AddQuery(db).execute(query);
                if (!suggestionsList.contains(query)) {
                    suggestionsList.add(query);
                }
                getSearchResults(query,true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().length()>0) {
                    populateAdapter(newText);
                    getSearchResults(newText,false);
                }
                return true;
            }
        });
        return true;
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{DatabaseContract.SearchTable._ID, DatabaseContract.SearchTable.QUERY});
        for (int i = 0; i < suggestionsList.size(); i++) {
            if (suggestionsList.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, suggestionsList.get(i)});
        }
        mAdapter.changeCursor(c);
    }

    private void getSearchResults(String query, boolean isSubmitted) {

        ApiInterface apiInterface = ApiClient.getApiInterface(this);
        Call<SearchResult> searchCall = apiInterface.search(query);

        searchCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    mResultList.clear();
                    if (response.body().getQuery() != null) {
                        if (response.body().getQuery().getPages() != null) {
                            if(isSubmitted) {
                                new AddResponse(query, response.body(), db).execute();
                            }
                            mResultList.addAll(response.body().getQuery().getPages());
                            mResultAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                new GetCachedResponse(db,MainActivity.this).execute(query);
            }
        });

    }

    @Override
    public void onQueriesFetched(ArrayList<String> queries) {
        suggestionsList.clear();
        suggestionsList.addAll(queries);
    }

    @Override
    public void onItemCLick(View view, long pageId) {
        String transitionName = getString(R.string.transition_string);



        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        view,
                        transitionName
                );
        ActivityCompat.startActivity(this, new Intent(this, WikiActivity.class).putExtra(AppConstants.PAGE_ID, pageId), options.toBundle());

    }

    @Override
    public void onCachedResponseFetched(SearchResult searchResult) {
        if(searchResult!=null) {
            mResultList.clear();
            mResultList.addAll(searchResult.getQuery().getPages());
            mResultAdapter.notifyDataSetChanged();
        }
    }
}

