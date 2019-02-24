package be.pxl.erisontavares.watchedit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.settings_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        setAppTheme(isDarkTheme);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setAppTheme(boolean isDarkTheme) {
        if (isDarkTheme) {
            setTheme(R.style.AppThemeDark_ActionBar);
        } else {
            setTheme(R.style.AppTheme_ActionBar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_theme_key))) {
            Log.d("Settings", "Theme changed");
            recreate();
//            Bundle savedInstanceState = new Bundle();
//            onSaveInstanceState(savedInstanceState);
//            super.onDestroy();
//
//            onCreate(savedInstanceState);
        }
    }
}
