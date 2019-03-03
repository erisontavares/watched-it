package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import be.pxl.erisontavares.watchedit.model.Movie;
import be.pxl.erisontavares.watchedit.model.SearchResult;
import be.pxl.erisontavares.watchedit.utilities.NetworkUtils;

public class MoviesSearchableActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private TextView mErrorTextView;
    private ProgressBar mLoadingProgress;
    private RecyclerView mMoviesRecyclerView;

    public final static String RECYCLER_STATE_KEY = "recycler_view_state";
    public final static String SEARCH_RESULT_KEY = "search_result_key";

    private MoviesSearchAdapter mMoviesSearchAdapter;
    private SearchResult<Movie> mMoviesSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_searchable);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.movies_search_hint));
        setSupportActionBar(toolbar);

        mErrorTextView = findViewById(R.id.movie_search_error);
        mLoadingProgress = findViewById(R.id.movie_search_loading);
        mMoviesRecyclerView = findViewById(R.id.search_movies_recycler_view);

        LinearLayoutManager moviesLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mMoviesRecyclerView.setLayoutManager(moviesLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMoviesSearchAdapter = new MoviesSearchAdapter(isDarkTheme);
        mMoviesRecyclerView.setAdapter(mMoviesSearchAdapter);

        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(RECYCLER_STATE_KEY);
            mMoviesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            SearchResult<Movie> savedSearchResults = savedInstanceState.getParcelable(SEARCH_RESULT_KEY);

            if (savedSearchResults != null) {
                mMoviesSearchResult = savedSearchResults;
                mMoviesSearchAdapter.movies = mMoviesSearchResult.getResults();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listState = mMoviesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_STATE_KEY, listState);
        outState.putParcelable(SEARCH_RESULT_KEY, mMoviesSearchResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.movies_action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL moviesUrl = NetworkUtils.buildSearchMovieUrlWithQuery(query, 1);
            new MoviesQueryTask().execute(moviesUrl);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);

            if (result == null) {
                mMoviesRecyclerView.setVisibility(View.INVISIBLE);
                mErrorTextView.setVisibility(View.VISIBLE);
            } else {
                mMoviesRecyclerView.setVisibility(View.VISIBLE);
                mErrorTextView.setVisibility(View.INVISIBLE);

                SearchResult searchResult = NetworkUtils.getMoviesSearchResultFromJson(result);

                mMoviesSearchAdapter.movies = searchResult.getResults();
                mMoviesSearchResult = searchResult;
                mMoviesSearchAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
