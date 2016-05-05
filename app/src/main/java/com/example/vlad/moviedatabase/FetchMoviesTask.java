package com.example.vlad.moviedatabase;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vlad.moviedatabase.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A Class to fetch the movies information from THEMOVIEDB
 */
public class FetchMoviesTask extends AsyncTask<Integer, Void, List<Movie>> {
    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private MoviesListFragment mFragment;

    public FetchMoviesTask(MoviesListFragment fragment) {
        mFragment = fragment;
    }

    private List<Movie> getMoviesListFromJson(String movieJsonStr) {
        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        final String TMD_TITLE = "title";
        final String TMD_POSTER = "poster_path";
        final String TMD_OVERVIEW = "overview";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_RATING = "vote_average";
        final String TMD_RESULTS = "results";
        final String TMD_POPULARITY = "popularity";
        final String TMD_ID = "id";

        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = moviesJson.getJSONArray(TMD_RESULTS);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
            for (int i = 0; i < movieArray.length(); i++) {
                String title;
                String poster_path;
                String overview;
                String dateTime;
                double vote_average;
                double popularity;
                int id;

                JSONObject movie = movieArray.getJSONObject(i);
                title = movie.getString(TMD_TITLE);
                poster_path = movie.getString(TMD_POSTER);
                overview = movie.getString(TMD_OVERVIEW);
                vote_average = movie.getDouble(TMD_RATING);
                dateTime = movie.getString(TMD_RELEASE_DATE);
                popularity = movie.getDouble(TMD_POPULARITY);
                id = movie.getInt(TMD_ID);

                Movie current = new Movie();
                current.setPoster(poster_path);
                current.setTitle(title);
                current.setOverview(overview);
                current.setRating(vote_average);
                current.setRelease_date(dateTime);
                current.setPopularity(popularity);
                current.setId(id);
                movies.add(current);
//                ContentValues contentValues = new ContentValues();
//
//                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
//                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, poster_path);
//                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
//                contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, vote_average);
//                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, dateTime);
//
//                cVVector.add(contentValues);
//            }
//
//            int inserted = 0;
//            if (cVVector.size() > 0) {
//                mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
//
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    protected List<Movie> doInBackground(Integer... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr;

        List<Movie> movies = null;
        try {
            final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String API_KEY = "api_key";
            final String SORT_PARAM = "sort_by";
            final String SORT_VALUE = Utility.getSortOrder(mFragment.getContext());
            final String PAGE = "page";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.THEMOVIEDB_API_KEY)
                    .appendQueryParameter(SORT_PARAM, SORT_VALUE)
                    .appendQueryParameter(PAGE, String.valueOf(params[0]))
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            moviesJsonStr = buffer.toString();
            movies = getMoviesListFromJson(moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        mFragment.updateContent(movies);
    }
}
