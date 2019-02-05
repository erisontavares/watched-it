package be.pxl.erisontavares.watchedit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoSearchResult {
    @SerializedName("id")
    private int movieId;
    private List<Video> results;

    public VideoSearchResult() {
    }

    public VideoSearchResult(int movieId, List<Video> results) {
        this.movieId = movieId;
        this.results = results;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
