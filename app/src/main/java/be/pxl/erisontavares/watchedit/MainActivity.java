package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mMoviesButton;
    private Button mSeriesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        mMoviesButton = findViewById(R.id.button_movies);
        mSeriesButton = findViewById(R.id.button_series);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMoviesButtonClick(View view) {
        goToMoviesActivity();
    }

    private void goToMoviesActivity() {
        Intent moviesIntent = new Intent(MainActivity.this, MovieListActivity.class);
        startActivity(moviesIntent);
    }

    public void onSeriesButtonClick(View view) {
        goToSeriesActivity();
    }

    private void goToSeriesActivity() {
        Intent seriesIntent = new Intent(MainActivity.this, SeriesActivity.class);
        startActivity(seriesIntent);
    }
}
