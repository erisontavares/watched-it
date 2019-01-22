package be.pxl.erisontavares.watchedit;

import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import be.pxl.erisontavares.watchedit.model.SearchResult;
import be.pxl.erisontavares.watchedit.utilities.NetworkUtils;

public class MoviesSearchableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private TextView mErrorTextView;
    private ProgressBar mLoadingProgress;
    private RecyclerView rvMovies;

    private MoviesSearchAdapter mMoviesSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_searchable);

        mErrorTextView = findViewById(R.id.movie_search_error);
        mLoadingProgress = findViewById(R.id.movie_search_loading);
        rvMovies = findViewById(R.id.search_movies_recycler_view);

        LinearLayoutManager moviesLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        rvMovies.setLayoutManager(moviesLayoutManager);
        rvMovies.setHasFixedSize(true);

        mMoviesSearchAdapter = new MoviesSearchAdapter();
        rvMovies.setAdapter(mMoviesSearchAdapter);
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
    public boolean onQueryTextSubmit(String query) {
        try {
            URL moviesUrl = NetworkUtils.buildSearchMovieUrlwithQuery(query, 1);
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
                rvMovies.setVisibility(View.INVISIBLE);
                mErrorTextView.setVisibility(View.VISIBLE);
            } else {
                rvMovies.setVisibility(View.VISIBLE);
                mErrorTextView.setVisibility(View.INVISIBLE);
            }

            SearchResult searchResult = NetworkUtils.getMoviesSearchResultFromJson(result);

            mMoviesSearchAdapter.movies = searchResult.getResults();
            mMoviesSearchAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
