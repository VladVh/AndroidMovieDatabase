package com.example.vlad.moviedatabase;

import android.net.Uri;
import android.os.AsyncTask;

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

/**
 * A Class to fetch the movie's trailers from THEMOVIEDB
 */
public class FetchTrailersTask extends AsyncTask<Integer, Void, List<Trailer>> {
    private DetailFragment mFragment;

    public FetchTrailersTask(DetailFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    protected List<Trailer> doInBackground(Integer... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader;
        String trailersJsonString;

        List<Trailer> trailers = null;
        try {
            final String BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY = "api_key";
            final String VIDEOS = "videos";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0].toString())
                    .appendEncodedPath(VIDEOS)
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

            trailersJsonString = buffer.toString();
            trailers = getTrailersFromJson(trailersJsonString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    private List<Trailer> getTrailersFromJson(String string) {
        final String TMD_RESULTS = "results";
        final String TMD_KEY = "key";
        final String TMD_NAME = "name";
        final String TMD_TYPE = "type";

        List<Trailer> trailersList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray trailers = jsonObject.getJSONArray(TMD_RESULTS);

            for (int i = 0; i < trailers.length(); i++) {
                String key;
                String name;
                String type;

                JSONObject object = trailers.getJSONObject(i);
                key = object.getString(TMD_KEY);
                name = object.getString(TMD_NAME);
                type = object.getString(TMD_TYPE);
                Trailer current = new Trailer();
                current.setKey(key);
                current.setName(name);
                current.setType(type);

                trailersList.add(current);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailersList;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        mFragment.updateTrailers(trailers);
    }
}
