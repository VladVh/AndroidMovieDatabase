package com.example.vlad.moviedatabase;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vlad.moviedatabase.data.Movie;
import com.example.vlad.moviedatabase.recyclerview.EndlessRecyclerViewScrollListener;
import com.example.vlad.moviedatabase.recyclerview.GridSpacingItemDecoration;
import com.example.vlad.moviedatabase.recyclerview.MoviesRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MoviesListFragment extends Fragment {
    private static final String LOG_TAG = MoviesListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MoviesRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public MoviesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_movies_list, container, false);

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);

        int spanCount;
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 3;
        } else {
            spanCount = 2;
        }

        int spacing = 0;
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getContext(), spanCount);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MoviesRecyclerAdapter(new ArrayList<Movie>());

        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);

        FetchMoviesTask task = new FetchMoviesTask(this);
        task.execute(1);

        final MoviesListFragment instance = this;
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                FetchMoviesTask task = new FetchMoviesTask(instance);
                task.execute(page);
                Log.d(LOG_TAG, "Images were downloaded " + mAdapter.getItemCount());
            }
        });

        return rootview;
    }

    public void updateContent(List<Movie> movies) {
        mAdapter.addContent(movies);
        mAdapter.notifyDataSetChanged();
    }

    public void onSortOrderChange() {
        mAdapter = new MoviesRecyclerAdapter(new ArrayList<Movie>());
        mRecyclerView.setAdapter(mAdapter);
        FetchMoviesTask task = new FetchMoviesTask(this);
        task.execute(1);
    }

}
