package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);

//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.movie_search).getActionView();
//
//        ComponentName componentName = new ComponentName(this, MoviesSearchableActivity.class);
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
//        searchView.setIconifiedByDefault(false);

//        searchView.setOnQueryTextListener(
//                new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        Log.i("QUERY", query);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        Log.i("New text", newText);
//                        return false;
//                    }
//                }
//        );

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
}
