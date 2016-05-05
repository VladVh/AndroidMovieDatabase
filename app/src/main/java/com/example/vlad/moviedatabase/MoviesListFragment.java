package com.example.vlad.moviedatabase;


import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vlad.moviedatabase.data.Movie;
import com.example.vlad.moviedatabase.data.MovieContract;
import com.example.vlad.moviedatabase.recyclerview.EndlessRecyclerViewScrollListener;
import com.example.vlad.moviedatabase.recyclerview.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all the movies that are fetched either from THEMOVIEDB or from the database.
 */
public class MoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MoviesListFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;

    private RecyclerView mRecyclerView;
    private MoviesRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;


    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_USER_RATING,
            MovieContract.MovieEntry.COLUMN_POPULARITY
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_TITLE = 1;
    static final int COL_MOVIE_POSTER = 2;
    static final int COL_MOVIE_OVERVIEW = 3;
    static final int COL_MOVIE_RELEASE_DATE = 4;
    static final int COL_MOVIE_RATING = 5;
    static final int COL_MOVIE_POPULARITY = 6;

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
        mAdapter = new MoviesRecyclerAdapter(new ArrayList<Movie>(), getContext());

        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);

        FetchMoviesTask task = new FetchMoviesTask(this);
        task.execute(1);

        final MoviesListFragment instance = this;

        if (!Utility.getSortOrder(getContext()).equals("favorites")) {
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    FetchMoviesTask task = new FetchMoviesTask(instance);
                    task.execute(page);
                    Log.d(LOG_TAG, "Images were downloaded " + mAdapter.getItemCount());
                }
            });
        }

        return rootview;
    }

    public void updateContent(List<Movie> movies) {
        mAdapter.addContent(movies);
        mAdapter.notifyDataSetChanged();
    }

    public void onSortOrderChange() {
        mAdapter = new MoviesRecyclerAdapter(new ArrayList<Movie>(), getContext());
        mRecyclerView.setAdapter(mAdapter);
        if (!Utility.getSortOrder(getContext()).equals("favorites")) {
            FetchMoviesTask task = new FetchMoviesTask(this);
            task.execute(1);
        } else {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri moviesUri = MovieContract.MovieEntry.buildMovieUri();
        return new CursorLoader(getActivity(),
                moviesUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
