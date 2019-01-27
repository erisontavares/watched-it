package be.pxl.erisontavares.watchedit;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;

import be.pxl.erisontavares.watchedit.data.WatchedItQueryHandler;
import be.pxl.erisontavares.watchedit.model.Movie;
import be.pxl.erisontavares.watchedit.utilities.Helpers;
import be.pxl.erisontavares.watchedit.utilities.NetworkUtils;

import be.pxl.erisontavares.watchedit.data.WatchedItContract.MoviesEntry;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    public MoviesAdapter(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, viewGroup, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapterViewHolder moviesAdapterViewHolder, int i) {
        mCursor.moveToPosition(i);
        moviesAdapterViewHolder.bindCursor(mCursor);
        final String movieId = mCursor.getString(
                mCursor.getColumnIndexOrThrow(MoviesEntry._ID)
        );
        final WatchedItQueryHandler handler = new WatchedItQueryHandler(moviesAdapterViewHolder.mRemoveButton.getContext().getContentResolver());

        moviesAdapterViewHolder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("REMOVE MOVIE", "MOVIE ID: " + movieId);
                String[] args = { movieId };

                handler.startDelete(1, null, MoviesEntry.CONTENT_URI, MoviesEntry._ID + "=?", args);

                // Creates toast and changes colors
                Toast toast = Toast.makeText(moviesAdapterViewHolder.mRemoveButton.getContext(), "Movie removed from your library!", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                TextView toastText = toastView.findViewById(android.R.id.message);
                toastText.setTextColor(Color.WHITE);
                toast.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        // After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mOverview;
        TextView mVoteAverage;
        TextView mReleaseDate;
        ImageView mMoviePoster;
        Button mRemoveButton;

        public MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.movie_title);
            mOverview = itemView.findViewById(R.id.movie_overview);
            mVoteAverage = itemView.findViewById(R.id.movie_vote_average);
            mReleaseDate = itemView.findViewById(R.id.movie_release_date);
            mMoviePoster = itemView.findViewById(R.id.movie_poster);
            mRemoveButton = itemView.findViewById(R.id.remove_movie_button);
        }

        public void bindCursor(Cursor cursor) {
            mTitle.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_TITLE)
            ));

            mOverview.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_OVERVIEW)
            ));
            mVoteAverage.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_VOTE_AVERAGE)
            ));

            // TODO: Fix parse error when release date is null
            String parsedReleaseDate = Helpers.formateDateFromstring(Movie.RELEASE_DATE_FORMAT, "dd MMM yyyy", cursor.getString(
                    cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_RELEASE_DATE)
            ));

            mReleaseDate.setText(itemView.getContext().getString(R.string.released_on, parsedReleaseDate));

            URL imageUrl = NetworkUtils.buildImageUrl(NetworkUtils.POSTER_SIZE_185, cursor.getString(
                    cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_POSTER_PATH)
            ));

            Picasso.get()
                    .load(imageUrl.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(mMoviePoster);
        }
    }
}
