package be.pxl.erisontavares.watchedit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static be.pxl.erisontavares.watchedit.data.WatchedItContract.CONTENT_AUTHORITY;
import static be.pxl.erisontavares.watchedit.data.WatchedItContract.PATH_MOVIES;

import be.pxl.erisontavares.watchedit.data.WatchedItContract.MoviesEntry;

public class WatchedItProvider extends ContentProvider {
    private static final String TAG = WatchedItProvider.class.getSimpleName();

    // Constants for the operations
    private static final int MOVIES = 1;
    private static final int MOVIES_ID = 2;

    // UriMatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIES, MOVIES);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIES + "/#", MOVIES_ID);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                cursor = db.query(MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case MOVIES_ID:
                selection = MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertRecord(uri, values, MoviesEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e(TAG, "Insert error for URI: " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return deleteRecord(uri, selection, selectionArgs, MoviesEntry.TABLE_NAME);
            case MOVIES_ID:
                return deleteRecord(uri, selection, selectionArgs, MoviesEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("Delete unknown URI: " + uri);
        }

    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e(TAG, "Delete error for URI: " + uri);
            return -1;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return updateRecord(uri, values, selection, selectionArgs, MoviesEntry.TABLE_NAME);

            case MOVIES_ID:
                selection = MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri, values, selection, selectionArgs, MoviesEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e(TAG, "Update error for URI: " + uri);
            return -1;
        }
        return id;
    }
}
