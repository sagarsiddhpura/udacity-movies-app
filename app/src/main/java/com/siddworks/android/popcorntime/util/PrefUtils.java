package com.siddworks.android.popcorntime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by SIDD on 28-Nov-15.
 */
public class PrefUtils {
    public static void savePopularMovies(Context mContext, String movies) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putString(Constants.SORT_BY_POPULAR, movies).commit();
    }

    public static String getPopularMovies(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(Constants.SORT_BY_POPULAR, null);
    }

    public static void saveHighestRatedMovies(Context mContext, String movies) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putString(Constants.SORT_BY_RATED, movies).commit();
    }

    public static String getHighestRatedMovies(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(Constants.SORT_BY_RATED, null);
    }

    public static String getMoviesSortType(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(Constants.SORT_CHOICE, Constants.SORT_BY_POPULAR);
    }

    public static void setMoviesSortType(Context mContext, String moviesType) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putString(Constants.SORT_CHOICE, moviesType).commit();
    }
}
