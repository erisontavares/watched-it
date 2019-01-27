package be.pxl.erisontavares.watchedit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import be.pxl.erisontavares.watchedit.data.WatchedItContract.MoviesEntry;
import be.pxl.erisontavares.watchedit.data.WatchedItQueryHandler;
import be.pxl.erisontavares.watchedit.model.Movie;
import be.pxl.erisontavares.watchedit.utilities.Helpers;
import be.pxl.erisontavares.watchedit.utilities.NetworkUtils;

public class MoviesSearchAdapter extends RecyclerView.Adapter<MoviesSearchAdapter.MoviesSearchViewHolder> {
    private static final String TAG = MoviesSearchAdapter.class.getSimpleName();

    List<Movie> movies;

//    public MoviesSearchAdapter(List<Movie> movies) {
//        this.movies = movies;
//    }

    public MoviesSearchAdapter() {
        this.movies = new ArrayList<Movie>();
    }

    @NonNull
    @Override
    public MoviesSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.movie_search_list_item, viewGroup, false);

        return new MoviesSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesSearchViewHolder moviesSearchViewHolder, final int i) {
        final Movie movie = movies.get(i);

        final ContentResolver contentResolver = moviesSearchViewHolder.mAddButton.getContext().getContentResolver();
        final boolean movieAlreadyExists = recordExists(movie.getId(), contentResolver);

        moviesSearchViewHolder.bind(movie);

        final WatchedItQueryHandler handler = new WatchedItQueryHandler(contentResolver);

        if (movieAlreadyExists) {
            moviesSearchViewHolder.mAddButton.setText(R.string.movie_already_added);
            moviesSearchViewHolder.mAddButton.setEnabled(false);
            moviesSearchViewHolder.mAddButton.setBackgroundTintList(
                    moviesSearchViewHolder.mAddButton.getContext().getResources().getColorStateList(R.color.colorDivider)
            );
        } else {
            moviesSearchViewHolder.mAddButton.setText(R.string.add);
            moviesSearchViewHolder.mAddButton.setEnabled(true);
            moviesSearchViewHolder.mAddButton.setBackgroundTintList(
                    moviesSearchViewHolder.mAddButton.getContext().getResources().getColorStateList(R.color.colorAccent)
            );
            moviesSearchViewHolder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues values = new ContentValues();
                    values.put(MoviesEntry._ID, movie.getId());
                    values.put(MoviesEntry.COLUMN_TITLE, movie.getTitle());
                    values.put(MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
                    values.put(MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    values.put(MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    values.put(MoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                    values.put(MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

                    handler.startInsert(1, null, MoviesEntry.CONTENT_URI, values);

                    // Creates toast and changes colors
                    Toast toast = Toast.makeText(moviesSearchViewHolder.mAddButton.getContext(), "Movie added to your library!", Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    TextView toastText = toastView.findViewById(android.R.id.message);
                    toastText.setTextColor(Color.WHITE);
                    toast.show();

                    notifyItemChanged(i);
                }
            });
        }
    }

    private boolean recordExists(int id, ContentResolver contentResolver) {
        String[] projection = {MoviesEntry._ID,
                MoviesEntry.COLUMN_TITLE,
                MoviesEntry.COLUMN_OVERVIEW,
                MoviesEntry.COLUMN_VOTE_AVERAGE,
                MoviesEntry.COLUMN_RELEASE_DATE,
                MoviesEntry.COLUMN_POSTER_PATH,
                MoviesEntry.COLUMN_ADDED_DATE
        };
        String selection = MoviesEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = contentResolver.query(MoviesEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MoviesSearchViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mOverview;
        TextView mVoteAverage;
        TextView mReleaseDate;
        ImageView mMoviePoster;

        Button mAddButton;

        public MoviesSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.search_movie_title);
            mOverview = itemView.findViewById(R.id.search_movie_overview);
            mVoteAverage = itemView.findViewById(R.id.search_movie_vote_average);
            mReleaseDate = itemView.findViewById(R.id.search_movie_release_date);
            mMoviePoster = itemView.findViewById(R.id.search_movie_poster);

            mAddButton = itemView.findViewById(R.id.add_movie_button);
        }

        public void bind(Movie movie) {
            mTitle.setText(movie.getTitle());
            mOverview.setText(movie.getOverview());
            mVoteAverage.setText(String.valueOf(movie.getVoteAverage()));

            // TODO: Fix parse error when release date is null
            String parsedReleaseDate = Helpers.formateDateFromstring(Movie.RELEASE_DATE_FORMAT, "dd MMM yyyy", movie.getReleaseDate());

            mReleaseDate.setText(itemView.getContext().getString(R.string.released_on, parsedReleaseDate));

            URL imageUrl = NetworkUtils.buildImageUrl(NetworkUtils.POSTER_SIZE_185, movie.getPosterPath());
            Picasso.get()
                    .load(imageUrl.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(mMoviePoster);
        }
    }
}
