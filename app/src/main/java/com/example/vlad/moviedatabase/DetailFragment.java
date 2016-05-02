package com.example.vlad.moviedatabase;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vlad.moviedatabase.data.Movie;
import com.example.vlad.moviedatabase.data.MovieContract;
import com.example.vlad.moviedatabase.data.Review;
import com.example.vlad.moviedatabase.data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String CONTENT = "data";
    public static final String DETAIL_URI = "URI";
    public static final int DETAIL_LOADER_ID = 0;

    private Movie mMovie = null;
    private String mSortOrder;
    private Uri mUri;

    private TextView mMovieTitle;
    private TextView mMovieRating;
    private TextView mMovieOverview;
    private TextView mFavoriteMarked;
    private Button mFavoriteButton;
    private ImageView mThumbnail;
    private ListView mListView;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_USER_RATING,
            MovieContract.MovieEntry.COLUMN_POPULARITY
    };

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortOrder = Utility.getSortOrder(getContext());

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (!mSortOrder.equals("favorites")) {
                mMovie = arguments.getParcelable(CONTENT);
            }
            mUri = arguments.getParcelable(DETAIL_URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.movie_details, container, false);

        mMovieTitle = (TextView) rootview.findViewById(R.id.detail_movie_title);
        mMovieRating = (TextView) rootview.findViewById(R.id.detail_movie_rating);
        mMovieOverview = (TextView) rootview.findViewById(R.id.detail_movie_overview);
        mFavoriteMarked = (TextView) rootview.findViewById(R.id.detail_favorite_added);
        mFavoriteButton = (Button) rootview.findViewById(R.id.detail_favorite_button);
        mThumbnail = (ImageView) rootview.findViewById(R.id.detail_movie_thumbnail);

        if (mMovie != null) {
            mMovieTitle.setText(mMovie.getTitle());

            mMovieRating.setText(mMovie.getRating() + "/10.0");

            mMovieOverview.setText(mMovie.getOverview());

            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + mMovie.getPoster())
                    .into(mThumbnail);

            FetchTrailersTask task = new FetchTrailersTask(this);
            task.execute(mMovie.getId());

            FetchReviewTask task2 = new FetchReviewTask(this);
            task2.execute(mMovie.getId());
        }

        mListView = (ListView) rootview.findViewById(R.id.detail_movie_reviews);

        return rootview;
    }

    public void updateTrailers(List<Trailer> trailers) {
        TrailersListAdapter adapter = new TrailersListAdapter(getContext(), R.layout.movie_trailer_item, trailers);
        //ListView listView = (ListView) getView().findViewById(R.id.detail_trailers_listview);
        //listView.setAdapter(adapter);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.detail_linear_layout);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = adapter.getView(i, null, null);
            layout.addView(item);
        }
    }

    public void updateReviews(List<Review> reviewsList) {
        ReviewsListAdapter adapter = new ReviewsListAdapter(getContext(), R.layout.movie_review_item, reviewsList);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.detail_linear_layout);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = adapter.getView(i, null, null);
            layout.addView(item);
        }
    }

    public Movie getmMovie() {
        return mMovie;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(getContext(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
                    );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }
        if (mMovie == null) {
            FetchTrailersTask task = new FetchTrailersTask(this);
            task.execute(data.getInt(MoviesListFragment.COL_MOVIE_ID));

            FetchReviewTask task2 = new FetchReviewTask(this);
            task2.execute(data.getInt(MoviesListFragment.COL_MOVIE_ID));
        }

        mMovieTitle.setText(data.getString(MoviesListFragment.COL_MOVIE_TITLE));

        mMovieOverview.setText(data.getString(MoviesListFragment.COL_MOVIE_OVERVIEW));

        mMovieRating.setText(data.getString(MoviesListFragment.COL_MOVIE_RATING) + "/10.0");

        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/"
                        + data.getString(MoviesListFragment.COL_MOVIE_POSTER))
                .into(mThumbnail);

        mFavoriteMarked.setVisibility(View.VISIBLE);
        mFavoriteButton.setClickable(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
