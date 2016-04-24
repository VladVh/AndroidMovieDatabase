package com.example.vlad.moviedatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vlad.moviedatabase.data.Movie;
import com.example.vlad.moviedatabase.data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailFragment extends Fragment {
    public static final String CONTENT = "data";
    private Movie mMovie = null;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.movie_details, container, false);

        TextView textView = (TextView)rootview.findViewById(R.id.detail_movie_title);
        textView.setText(mMovie.getTitle());

        textView = (TextView) rootview.findViewById(R.id.detail_movie_rating);
        textView.setText(mMovie.getRating() + "/10.0");

        textView = (TextView) rootview.findViewById(R.id.detail_movie_overview);
        textView.setText(mMovie.getOverview());

        ImageView thumbnail = (ImageView) rootview.findViewById(R.id.detail_movie_thumbnail);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + mMovie.getPoster())
                .into(thumbnail);

        FetchTrailersTask task = new FetchTrailersTask(this);
        task.execute(mMovie.getId());

        return rootview;
    }

    public void updateTrailers(List<Trailer> trailers) {
        TrailersListAdapter adapter = new TrailersListAdapter(getContext(), R.layout.trailer_item, trailers);
        //ListView listView = (ListView) getView().findViewById(R.id.detail_trailers_listview);
        //listView.setAdapter(adapter);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.detail_linear_layout);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = adapter.getView(i, null, null);
            layout.addView(item);
        }
    }
}
