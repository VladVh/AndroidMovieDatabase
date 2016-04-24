package com.example.vlad.moviedatabase;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import android.os.Bundle;
import android.widget.Toast;

public class YoutubeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    private static final String API_KEY = BuildConfig.YOUTUBE_API_KEY;
    private static String video_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        video_id = getIntent().getStringExtra("id");

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(getApplicationContext(),
                "onInitializationFailure()",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(video_id);
        }
    }

}
