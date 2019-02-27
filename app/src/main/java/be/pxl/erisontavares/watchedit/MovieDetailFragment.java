package be.pxl.erisontavares.watchedit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import be.pxl.erisontavares.watchedit.model.Movie;
import be.pxl.erisontavares.watchedit.model.Video;
import be.pxl.erisontavares.watchedit.model.VideoSearchResult;
import be.pxl.erisontavares.watchedit.utilities.Helpers;
import be.pxl.erisontavares.watchedit.utilities.NetworkUtils;

public class MovieDetailFragment extends Fragment {

    public static final String MOVIE_ITEM = "movie_item";
    public static final String IS_DARK_THEME = "is_dark_theme";

    private Movie mMovieItem;
    private Video mMovieTrailer;
    private boolean isDarkTheme;

    private Button mTrailerButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(MOVIE_ITEM)) {
            mMovieItem = getArguments().getParcelable(MOVIE_ITEM);
            isDarkTheme = getArguments().getBoolean(IS_DARK_THEME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mMovieItem.getTitle());
            }

            try {
                URL videoSearchUrl = NetworkUtils.buildMovieVideosUrl(String.valueOf(mMovieItem.getId()));
                new VideosQueryTask().execute(videoSearchUrl);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mMovieItem.getTitle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (mMovieItem != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail_overview)).setText(mMovieItem.getOverview());

            ImageView backdropImageView = rootView.findViewById(R.id.movie_backdrop);

            // TODO: Fix parse error when release date is null
            String parsedReleaseDate = Helpers.formateDateFromstring(Movie.RELEASE_DATE_FORMAT, "dd MMM yyyy", mMovieItem.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.movie_detail_release)).setText(getContext().getString(R.string.released_on, parsedReleaseDate));

            ((TextView) rootView.findViewById(R.id.movie_detail_vote_average)).setText(String.valueOf(mMovieItem.getVoteAverage()));

            URL imageUrl = NetworkUtils.buildImageUrl(NetworkUtils.BACKDROP_SIZE_780, mMovieItem.getBackdropPath());

            Picasso.get()
                    .load(imageUrl.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(backdropImageView);

            Resources resources = getContext().getResources();

            mTrailerButton = rootView.findViewById(R.id.trailer_button);
            mTrailerButton.setEnabled(false);
            mTrailerButton.setText(getString(R.string.no_trailer));
            mTrailerButton.setBackgroundTintList(
                    isDarkTheme ? resources.getColorStateList(R.color.darkColorDivider) : resources.getColorStateList(R.color.colorDivider)
            );
        }

        return rootView;
    }

    public class VideosQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            VideoSearchResult videoSearchResult = NetworkUtils.getVideoSearchResultFromJson(result);
            if (videoSearchResult != null && videoSearchResult.getResults().size() > 0) {
                mMovieTrailer = videoSearchResult.getResults().get(0);

                mTrailerButton.setEnabled(true);
                mTrailerButton.setText(getString(R.string.trailer_button));
                mTrailerButton.setBackgroundTintList(
                        isDarkTheme ?
                                getContext().getResources().getColorStateList(R.color.darkColorAccent)
                                : getContext().getResources().getColorStateList(R.color.colorAccent)
                );
                mTrailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        watchYoutubeVideo(v.getContext(), mMovieTrailer.getKey());
                    }
                });
            }
        }

        public void watchYoutubeVideo(Context context, String id) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));

            try {
                context.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(webIntent);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
