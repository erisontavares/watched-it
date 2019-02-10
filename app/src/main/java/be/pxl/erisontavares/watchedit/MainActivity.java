package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mMoviesButton;
    private Button mSeriesButton;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mMoviesButton = findViewById(R.id.button_movies);
        mSeriesButton = findViewById(R.id.button_series);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_movies:
                                goToMoviesActivity();
                                return true;

                            case R.id.nav_series:
                                goToSeriesActivity();
                                return true;

                            case R.id.nav_settings:
                                Log.d("MainActivity", "Setting button clicked");
                                return true;

                            default:
                                return false;
                        }
                    }
                }
        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
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
