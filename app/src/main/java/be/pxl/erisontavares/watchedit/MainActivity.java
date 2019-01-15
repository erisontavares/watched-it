package be.pxl.erisontavares.watchedit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mMoviesButton;
    private Button mSeriesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesButton = findViewById(R.id.button_movies);
        mSeriesButton = findViewById(R.id.button_series);
    }

    public void onMoviesButtonClick(View view) {
        Intent moviesIntent = new Intent(MainActivity.this, MoviesActivity.class);
        startActivity(moviesIntent);
    }

    public void onSeriesButtonClick(View view) {
        Intent seriesIntent = new Intent(MainActivity.this, SeriesActivity.class);
        startActivity(seriesIntent);
    }
}
