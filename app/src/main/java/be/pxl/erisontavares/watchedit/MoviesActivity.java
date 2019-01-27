package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import be.pxl.erisontavares.watchedit.data.WatchedItContract.MoviesEntry;

public class MoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int URL_LOADER = 100;
    private RecyclerView rvMovies;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        rvMovies = findViewById(R.id.movies_recycler_view);
        LinearLayoutManager moviesLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        rvMovies.setLayoutManager(moviesLayoutManager);
        rvMovies.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        rvMovies.setAdapter(mMoviesAdapter);
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
            case R.id.movie_search:
                Log.i(MoviesActivity.class.getName(), "Add movie clicked!!");
                startActivity(new Intent(this, MoviesSearchableActivity.class));
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {MoviesEntry._ID,
                MoviesEntry.COLUMN_TITLE,
                MoviesEntry.COLUMN_OVERVIEW,
                MoviesEntry.COLUMN_VOTE_AVERAGE,
                MoviesEntry.COLUMN_RELEASE_DATE,
                MoviesEntry.COLUMN_POSTER_PATH,
                MoviesEntry.COLUMN_ADDED_DATE
        };
        return new CursorLoader(
                this,
                MoviesEntry.CONTENT_URI,
                projection,
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
