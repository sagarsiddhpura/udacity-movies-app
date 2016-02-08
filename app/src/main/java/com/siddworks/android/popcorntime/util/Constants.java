package com.siddworks.android.popcorntime.util;

import com.siddworks.android.popcorntime.data.MovieContract;

/**
 * Created by SIDD on 25-Nov-15.
 */
public class Constants {
    public static final String SORT_BY_POPULAR = "popularity.desc";
    public static final String SORT_BY_RATED = "vote_average.desc";
    public static final String SORT_BY_FAVOURITES = "favourites";
    public static final String SORT_CHOICE = "popularity.asc";
    public static final String POSTER_API_URL_ROOT = "http://image.tmdb.org/t/p/w185/";
    public static final String BACKDROP_API_URL_ROOT = "http://image.tmdb.org/t/p/w500/";
    public static final String MOVIE_JSON = "movie_json";
    public static final String MIN_VOTE_COUNT = "300";
    public static final String YOUTUBE = "YouTube";
    public static final String[] MOVIE_COLUMNS = {

            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.TABLE_NAME + "." +MovieContract.Movies.PAGE,
            MovieContract.Movies.TABLE_NAME + "." +MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ADULT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POPULARITY,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.TABLE_NAME + "." +MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.TABLE_NAME + "." +MovieContract.Movies.FAVOURED
    };
    public static final String[] TRAILER_COLUMNS = {

            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers._ID,
            MovieContract.Trailers.TABLE_NAME + "." +MovieContract.Trailers.NAME,
            MovieContract.Trailers.TABLE_NAME + "." +MovieContract.Trailers.SIZE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SOURCE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.TYPE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.MOVIE_ID
    };
    public static final String[] REVIEW_COLUMNS = {

            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews._ID,
            MovieContract.Reviews.TABLE_NAME + "." +MovieContract.Reviews.PAGE,
            MovieContract.Reviews.TABLE_NAME + "." +MovieContract.Reviews.TOTAL_PAGE,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.TOTAL_RESULTS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.ID_REVIEWS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.AUTHOR,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.CONTENT,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.URL,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.MOVIE_ID

    };
    public static final String[] GENRE_COLUMNS = {

            MovieContract.Genres.TABLE_NAME + "." + MovieContract.Genres._ID,
            MovieContract.Genres.TABLE_NAME + "." +MovieContract.Genres.NAME,
            MovieContract.Genres.TABLE_NAME + "." +MovieContract.Genres.ID_GENRES,
            MovieContract.Genres.TABLE_NAME + "." + MovieContract.Genres.MOVIE_ID
    };
    public static final String[] FAVOURITE_MOVIE_COLUMNS = {

            MovieContract.Favourites.TABLE_NAME + "." + MovieContract.Favourites._ID,
            MovieContract.Favourites.PAGE,
            MovieContract.Favourites.POSTER_PATH,
            MovieContract.Favourites.ADULT,
            MovieContract.Favourites.OVERVIEW,
            MovieContract.Favourites.RELEASE_DATE,
            MovieContract.Favourites.MOVIE_ID,
            MovieContract.Favourites.ORIGINAL_TITLE,
            MovieContract.Favourites.ORIGINAL_LANGUAGE,
            MovieContract.Favourites.TITLE,
            MovieContract.Favourites.BACKDROP_PATH,
            MovieContract.Favourites.POPULARITY,
            MovieContract.Favourites.VOTE_COUNT,
            MovieContract.Favourites.VOTE_AVERAGE,
            MovieContract.Favourites.FAVOURED,
            MovieContract.Favourites.SHOWED,
            MovieContract.Favourites.DOWNLOADED,
            MovieContract.Favourites.SORT_BY
    };
    public static final int DETAIL_LOADER = 0;
    public static final int TRAILER_LOADER = 1;
    public static final int REVIEW_LOADER = 2;
    public static final int GENRE_LOADER = 3;
    public static final int FAVOURITE_LOADER = 4;

    public static int COL_ID = 0;
    public static int COL_PAGE = 1;
    public static int COL_POSTER_PATH = 2;
    public static int COL_ADULT = 3;
    public static int COL_OVERVIEW = 4;
    public static int COL_RELEASE_DATE = 5;
    public static int COL_MOVIE_ID = 6;
    public static int COL_ORIGINAL_TITLE = 7;
    public static int COL_ORIGINAL_LANG = 8;
    public static int COL_TITLE = 9;
    public static int COL_BACKDROP_PATH = 10;
    public static int COL_POPULARITY = 11;
    public static int COL_VOTE_COUNT = 12;
    public static int COL_VOTE_AVERAGE = 13;
    public static int COL_FAVOURED = 14;
    public static int COL_SHOWED = 15;
    public static int COL_DOWNLOADED = 16;
    public static int COL_SORT_BY = 17;}
