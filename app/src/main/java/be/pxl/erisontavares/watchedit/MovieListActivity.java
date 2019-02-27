package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import be.pxl.erisontavares.watchedit.data.WatchedItContract;
import be.pxl.erisontavares.watchedit.utilities.Helpers;

public class MovieListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private boolean mTwoPane;

    private static final int URL_LOADER = 100;
    private RecyclerView mMoviesList;
    private MoviesAdapter mMoviesAdapter;

    private FrameLayout mRecyclerViewContainer;
    private FrameLayout mEmptyListContainer;
    private FrameLayout mLoadingContainer;

    private String sortOrderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // landscape mode.
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        mRecyclerViewContainer = findViewById(R.id.recycler_view_container);
        mEmptyListContainer = findViewById(R.id.empty_list_container);
        mLoadingContainer = findViewById(R.id.loading_container);
        mLoadingContainer.setVisibility(View.VISIBLE);

        mMoviesList = findViewById(R.id.movies_recycler_view);
        LinearLayoutManager moviesLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mMoviesList.setLayoutManager(moviesLayoutManager);
        mMoviesList.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this, mTwoPane);

        mMoviesList.setAdapter(mMoviesAdapter);
        mMoviesList.addItemDecoration(new DividerItemDecoration(mMoviesList.getContext(), DividerItemDecoration.VERTICAL));
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    private void getSortOrderValuePref() {
        String sortListValue = sharedPref.getString(getString(R.string.settings_sort_key), "1");
        sortOrderValue = Helpers.getListSortTypeBySetting(sortListValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MovieListActivity.this, SettingsActivity.class));
                return true;
            case R.id.movie_search:
                Log.i(MovieListActivity.class.getName(), "Add movie clicked!!");
                startActivity(new Intent(this, MoviesSearchableActivity.class));
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        getSortOrderValuePref();

        String[] projection = {WatchedItContract.MoviesEntry._ID,
                WatchedItContract.MoviesEntry.COLUMN_TITLE,
                WatchedItContract.MoviesEntry.COLUMN_OVERVIEW,
                WatchedItContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
                WatchedItContract.MoviesEntry.COLUMN_RELEASE_DATE,
                WatchedItContract.MoviesEntry.COLUMN_POSTER_PATH,
                WatchedItContract.MoviesEntry.COLUMN_BACKDROP_PATH,
                WatchedItContract.MoviesEntry.COLUMN_ADDED_DATE
        };

        Log.d("List sort setting", sortOrderValue);

        return new CursorLoader(
                this,
                WatchedItContract.MoviesEntry.CONTENT_URI,
                projection,
                null, null, sortOrderValue
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            mLoadingContainer.setVisibility(View.INVISIBLE);
            mEmptyListContainer.setVisibility(View.INVISIBLE);
            mRecyclerViewContainer.setVisibility(View.VISIBLE);
        } else {
            mLoadingContainer.setVisibility(View.INVISIBLE);
            mRecyclerViewContainer.setVisibility(View.INVISIBLE);
            mEmptyListContainer.setVisibility(View.VISIBLE);
        }
        mMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_sort_key))) {
            LoaderManager.getInstance(this).restartLoader(URL_LOADER, null, this);
            Log.d("MovieListActivity", "Sorting list changes");
        }
    }
}
