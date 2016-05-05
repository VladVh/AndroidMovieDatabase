package com.example.vlad.moviedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vlad.moviedatabase.data.Review;

import java.util.List;

/**
 * Adapter to display all the movie's reviews
 */
public class ReviewsListAdapter extends ArrayAdapter<Review> {
    boolean isExpanded = false;
    public ReviewsListAdapter(Context context, int resource, List<Review> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.movie_review_item, null);
        }

        final Review review = getItem(position);
        if (review != null) {
            TextView author = (TextView) view.findViewById(R.id.review_author);
            author.append(review.getAuthor());

            final TextView content = (TextView) view.findViewById(R.id.review_expandable_content);
            content.setText(review.getContent().substring(0, 30) + "...");

            final ImageButton button = (ImageButton) view.findViewById(R.id.review_expand_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int contentSize = content.getText().length();
                    int reviewSize = review.getContent().length();
                    if (contentSize != reviewSize) {
                        content.setText(review.getContent());
                        button.setImageResource(R.drawable.minus_icon);
                    } else {
                        content.setText(review.getContent().substring(0, 30) + "...");
                        button.setImageResource(R.drawable.plus_icon);
                    }
                }
            });
        }


        return view;
    }
}
