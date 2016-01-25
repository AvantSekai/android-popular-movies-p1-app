package app.com.example.android.popular_movies_p1;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shavant on 1/23/16.
 */

// Create a Parcelabble Class for passing data along to other Activities
public class MovieParcel implements Parcelable {
    // Data for details on each Movie Parcelable Object
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;

    public MovieParcel() {

    }

    public int describeContents() {
        return 0;
    }

    public void setStringTitle(String title) {
        this.title = title;
    }

    public String getStringTitle(){
        return title;
    }

    public void setPosterPath(String path) {
        this.posterPath = path;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public void writeToParcel(Parcel dest, int flags) {

    }

    private MovieParcel(Parcel in) {

    }

    public static final Parcelable.Creator<MovieParcel> CREATOR
            = new Parcelable.Creator<MovieParcel>() {

        @Override
        public MovieParcel createFromParcel(Parcel in) {
            return new MovieParcel(in);
        }

        @Override
        public MovieParcel[] newArray(int size) {
            return new MovieParcel[size];
        }

    };

}
