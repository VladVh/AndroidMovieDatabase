package com.example.vlad.moviedatabase;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.vlad.moviedatabase.data.Movie;
import com.example.vlad.moviedatabase.data.MovieContract;

/**
 * Created by Vlad on 18.04.2016.
 */
public class DetailsActivity extends AppCompatActivity {
    public static final String CONTENT = "data";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            if (!Utility.getSortOrder(getBaseContext()).equals("favorites")) {
                arguments.putParcelable(DetailFragment.CONTENT, getIntent().getParcelableExtra(CONTENT));
            }
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());
            detailFragment = new DetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, detailFragment).commit();
        }
    }

    public void onWatchTrailerClick(View view) {
        String key = (String) view.findViewById(R.id.trailer_image_button).getTag();
        String YOUTUBE_BASE = "https://www.youtube.com/watch?v=";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE + key));
        startActivity(intent);
    }

    public void onButtonFavoriteClick(View view) {
        Movie movie = detailFragment.getmMovie();
        writeToDb(movie);
    }

    private void writeToDb(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPoster());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        Log.d(LOG_TAG, "movie " + movie.getTitle() + " inserted");
    }
}
