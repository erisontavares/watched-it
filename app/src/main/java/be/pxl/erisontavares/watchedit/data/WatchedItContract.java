package be.pxl.erisontavares.watchedit.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class WatchedItContract {
    public static final String CONTENT_AUTHORITY = "be.pxl.erisontavares.watchedit";
    public static final String PATH_MOVIES = "movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private WatchedItContract() {}

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        // Table name
        public static final String TABLE_NAME = "movies";
        // Column (field) names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_ADDED_DATE = "added_date";
    }
}
