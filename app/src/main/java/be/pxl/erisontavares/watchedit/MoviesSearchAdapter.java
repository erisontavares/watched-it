package be.pxl.erisontavares.watchedit;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import be.pxl.erisontavares.watchedit.model.Movie;
import be.pxl.erisontavares.watchedit.utilities.Helpers;
import be.pxl.erisontavares.watchedit.utilities.NetworkUtils;

public class MoviesSearchAdapter extends RecyclerView.Adapter<MoviesSearchAdapter.MovieViewHolder> {

    List<Movie> movies;

    public MoviesSearchAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public MoviesSearchAdapter() {
        this.movies = new ArrayList<Movie>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.movie_list_item, viewGroup, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = movies.get(i);
        movieViewHolder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mOverview;
        TextView mVoteAverage;
        TextView mReleaseDate;
        ImageView mMoviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.search_movie_title);
            mOverview = itemView.findViewById(R.id.search_movie_overview);
            mVoteAverage = itemView.findViewById(R.id.search_movie_vote_average);
            mReleaseDate = itemView.findViewById(R.id.search_movie_release_date);
            mMoviePoster = itemView.findViewById(R.id.search_movie_poster);
        }

        public void bind(Movie movie) {
            mTitle.setText(movie.getTitle());
            mOverview.setText(movie.getOverview());
            mVoteAverage.setText(String.valueOf(movie.getVoteAverage()));

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
