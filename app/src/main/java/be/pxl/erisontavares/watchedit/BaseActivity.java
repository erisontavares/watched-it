package be.pxl.erisontavares.watchedit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected boolean isDarkTheme;
    protected SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkTheme = sharedPref.getBoolean(getString(R.string.settings_theme_key), false);
        setAppTheme(isDarkTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean currentTheme = isDarkTheme;
        isDarkTheme = sharedPref.getBoolean(getString(R.string.settings_theme_key), false);
        if (currentTheme != isDarkTheme)
            recreate();
    }

    protected void setAppTheme(boolean isDarkTheme) {
        if (isDarkTheme) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }
}
