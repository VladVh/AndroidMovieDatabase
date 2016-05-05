package com.example.vlad.moviedatabase.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class implements Parcelable interface, because its objects are transferred between fragments.
 * It provides all needed information about a movie.
 */
public class Movie implements Parcelable {
    private String title;
    private String poster;
    private String overview;
    private String release_date;
    private Double rating;
    private Double popularity;
    private int id;

    private final int TITLE_ID = 0;
    private final int POSTER_ID = 1;
    private final int OVERVIEW_ID = 2;
    private final int RELEASE_DATE_ID = 3;

    public Movie() {

    }

    public Movie(String poster) {
        this.poster = poster;
    }

    private Movie(Parcel in) {
        String[] strings = new String[4];
        double[] values = new double[2];
        in.readDoubleArray(values);
        rating = values[0];
        popularity = values[1];
        in.readStringArray(strings);

        title = strings[TITLE_ID];
        poster = strings[POSTER_ID];
        overview = strings[OVERVIEW_ID];
        release_date = strings[RELEASE_DATE_ID];

        id = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(new double[] {rating, popularity});
        dest.writeStringArray(new String[]{title, poster, overview, release_date});
        dest.writeInt(id);
    }


}
