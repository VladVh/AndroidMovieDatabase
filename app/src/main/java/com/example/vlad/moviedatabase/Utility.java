package com.example.vlad.moviedatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Vlad on 20.04.2016.
 */
public class Utility {

    public static String getSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sortOrder = prefs.getString(context.getString(R.string.setting_sort_key),
                context.getString(R.string.setting_sort_default));
        switch (sortOrder) {
            case "1": {
                return "vote_average.desc";
            }
            case "2": {
                return "popularity.desc";
            }
            case "3": {
                return "favorites";
            }
        }
        return null;
    }
}
