package com.example.vlad.moviedatabase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Vlad on 18.04.2016.
 */
public class DetailsActivity extends AppCompatActivity {
    public static final String CONTENT = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.CONTENT, getIntent().getParcelableExtra(CONTENT));

            DetailFragment df = new DetailFragment();
            df.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, df).commit();
        }
    }

    public void onWatchTrailerClick(View view) {
        String key = (String) view.findViewById(R.id.trailer_image_button).getTag();
//        String YOUTUBE_BASE = "https://www.youtube.com/watch?v=";
//        String link = YOUTUBE_BASE + key;
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//        intent.setType("video/mp4");
//        startActivity(intent);
        Intent intent = new Intent(this, YoutubeActivity.class);
        intent.putExtra("id", key);
        startActivity(intent);
    }
}
