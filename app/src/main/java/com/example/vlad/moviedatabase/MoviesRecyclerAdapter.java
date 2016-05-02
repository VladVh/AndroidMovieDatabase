package com.example.vlad.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vlad.moviedatabase.data.Movie;
import com.example.vlad.moviedatabase.data.MovieContract;
import com.example.vlad.moviedatabase.recyclerview.IMyViewHolderClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 16.04.2016.
 */
public class MoviesRecyclerAdapter extends
        RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Movie> mMovies = new ArrayList<>();
    private Context mContext;
    private Cursor mCursor;
    private String mSortOrder;
    private IMyViewHolderClickListener mListener;

    // Pass in the contact array into the constructor
    public MoviesRecyclerAdapter(List<Movie> movies, Context context) {
        mMovies = movies;
        mSortOrder = Utility.getSortOrder(context);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Movie current;
        private ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            current = null;

            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }

        public void setCurrent(Movie movie) {
            current = movie;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mSortOrder = Utility.getSortOrder(mContext);

        mListener = new IMyViewHolderClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                int id;
                if (mSortOrder.equals("favorites")) {
                    mCursor.moveToPosition(position);
                    id = mCursor.getInt(MoviesListFragment.COL_MOVIE_ID);
                } else {
                    id = mMovies.get(position).getId();
                    intent.putExtra(DetailsActivity.CONTENT, mMovies.get(position));
                }
                intent.setData(MovieContract.MovieEntry.buildMovieUriWithId(id));
                mContext.startActivity(intent);
            }
        };
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, null);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String poster;
        if (mSortOrder.equals("favorites")) {
            mCursor.moveToPosition(position);
            poster = mCursor.getString(MoviesListFragment.COL_MOVIE_POSTER);
        } else {
            Movie movie = mMovies.get(position);
            viewHolder.setCurrent(movie);
            poster = movie.getPoster();
        }

        String url = "http://image.tmdb.org/t/p/w185/" + poster;
        Picasso.with(mContext).load(url)
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mSortOrder.equals("favorites")) {
            if (null == mCursor) return 0;
            return mCursor.getCount();
        } else {
            return mMovies.size();
        }
    }

    public void addContent(List<Movie> movies) {
        mMovies.addAll(movies);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
