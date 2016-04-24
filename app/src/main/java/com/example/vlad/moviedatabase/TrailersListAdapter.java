package com.example.vlad.moviedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.moviedatabase.data.Trailer;

import java.util.List;

/**
 * Created by Vlad on 21.04.2016.
 */
public class TrailersListAdapter extends ArrayAdapter<Trailer> {

    public TrailersListAdapter(Context context, int resource, List<Trailer> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, null);
        }

        Trailer trailer = getItem(position);
        if (trailer != null) {
            TextView textView = (TextView) v.findViewById(R.id.trailer_name);
            textView.setText(trailer.getName());

            ImageView imageView = (ImageView) v.findViewById(R.id.trailer_image_button);
            imageView.setTag(trailer.getKey());
        }

        return v;
    }
}
