package be.pxl.erisontavares.watchedit.utilities;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import be.pxl.erisontavares.watchedit.model.Movie;
import be.pxl.erisontavares.watchedit.model.SearchResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_API_URL = "https://api.themoviedb.org/3/";
    public static final String SEARCH_PATH = "search";
    public static final String MOVIE_SEARCH_PATH = "movie";
    public static final String SERIES_SEARCH_PATH = "search/tv";

    public static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY = "7b7b0862e22e5f11b28dd69785478a0e";

    public static final String LANGUAGE_PARAM = "language";
    public static final String LANGUAGE = "en-US";

    public static final String QUERY_PARAM = "query";

    public static final String PAGE_PARAM = "page";

    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_154 = "w154";
    public static final String POSTER_SIZE_185 = "w185";
    public static final String POSTER_SIZE_342 = "w342";
    public static final String POSTER_SIZE_500 = "w500";

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static final Type MOVIES_SEARCH_RESULT = new TypeToken<SearchResult<Movie>>() {
    }.getType();

    public static URL buildSearchMovieUrlWithQuery(String query, int pageNumber) {
        Uri searchMovieQueryUri = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(SEARCH_PATH)
                .appendPath(MOVIE_SEARCH_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(QUERY_PARAM, query)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(pageNumber))
                .build();

        try {
            URL searchMovieQueryUrl = new URL(searchMovieQueryUri.toString());
            Log.d(TAG, "URL: " + searchMovieQueryUrl);
            return searchMovieQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static URL buildImageUrl(String imageSize, String imagePath) {
        Uri imageUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(imageSize)
                .appendEncodedPath(imagePath)
                .build();

        try {
            URL imageUrl = new URL(imageUri.toString());
            Log.d(TAG, "Image URL: " + imageUrl);
            return imageUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static SearchResult getMoviesSearchResultFromJson(String jsonString) {
        SearchResult result = null;
        Gson gson = new Gson();

        result = gson.fromJson(jsonString, MOVIES_SEARCH_RESULT);

        return result;
    }
}
