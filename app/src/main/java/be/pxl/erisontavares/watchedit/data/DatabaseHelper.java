package be.pxl.erisontavares.watchedit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.pxl.erisontavares.watchedit.data.WatchedItContract.MoviesEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "watchedit.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MOVIES_CREATE =
            "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                    MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                    MoviesEntry.COLUMN_TITLE + " TEXT, " +
                    MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                    MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                    MoviesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                    MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                    MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT, " +
                    MoviesEntry.COLUMN_ADDED_DATE + " TEXT default CURRENT_TIMESTAMP " +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_MOVIES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
