package be.pxl.erisontavares.watchedit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> implements Parcelable {
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    private List<T> results;

    public SearchResult() {
    }

    public SearchResult(int page, int totalResults, int totalPages, List<T> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    protected SearchResult(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();

        int size = in.readInt();
        if (size == 0) {
            results = null;
        } else {
            Class<?> type = (Class<?>) in.readSerializable();
            results = new ArrayList<>(size);
            in.readList(results, type.getClassLoader());
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);

        if (results == null || results.size() == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(results.size());
            final Class<?> objectsType = results.get(0).getClass();
            dest.writeSerializable(objectsType);
            dest.writeList(results);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
