package com.example.vlad.moviedatabase;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.vlad.moviedatabase.data.Review;
import com.example.vlad.moviedatabase.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A Class to fetch the movie's reviews from THEMOVIEDB
 */
public class FetchReviewTask extends AsyncTask<Integer, Void, List<Review>> {
    private DetailFragment mFragment;

    public FetchReviewTask(DetailFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    protected List<Review> doInBackground(Integer... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader;
        String reviewsJsonString;

        List<Review> reviews = null;
        try {
            final String BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY = "api_key";
            final String REVIEWS = "reviews";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0].toString())
                    .appendEncodedPath(REVIEWS)
                    .appendQueryParameter(API_KEY, BuildConfig.THEMOVIEDB_API_KEY)
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

            reviewsJsonString = buffer.toString();
            reviews = getTrailersFromJson(reviewsJsonString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    private List<Review> getTrailersFromJson(String string) {
        final String TMD_RESULTS = "results";
        final String TMD_AUTHOR = "author";
        final String TMD_CONTENT = "content";

        List<Review> reviewsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray trailers = jsonObject.getJSONArray(TMD_RESULTS);

            for (int i = 0; i < trailers.length(); i++) {
                String author;
                String content;

                JSONObject object = trailers.getJSONObject(i);
                author = object.getString(TMD_AUTHOR);
                content = object.getString(TMD_CONTENT);

                reviewsList.add(new Review(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewsList;
    }
    @Override
    protected void onPostExecute(List<Review> results) {
        mFragment.updateReviews(results);
    }
}
