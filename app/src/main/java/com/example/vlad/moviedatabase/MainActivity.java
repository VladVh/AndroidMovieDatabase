package com.example.vlad.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSortOrder = Utility.getSortOrder(this);

        if (savedInstanceState == null) {
            MoviesListFragment mListFragment = new MoviesListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_list_container, mListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortOrder = Utility.getSortOrder(this);
        if (!sortOrder.equals(mSortOrder)) {
            MoviesListFragment moviesListFragment =
                    (MoviesListFragment) getSupportFragmentManager().findFragmentById(R.id.movies_list_container);
            moviesListFragment.onSortOrderChange();
            mSortOrder = sortOrder;
        }

    }
}
