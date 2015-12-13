package com.siddworks.android.popcorntime.data;

import com.siddworks.android.popcorntime.model.MovieWrapper;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by SIDD on 26-Nov-15.
 */
public interface Api {

    public static final String ENDPOINT = "http://api.themoviedb.org/3/discover/";

    // Get Popular Movies API
    @GET("movie")
    public Observable<MovieWrapper> getPopularMovies(@Query("api_key") String apiKey ,
                                                     @Query("vote_count.gte") String minVoteCount ,
                                                     @Query("sort_by") String sortBy);

    // Get Rated Movies API
    @GET("movie")
    public Observable<MovieWrapper> getHighestRatedMovies(@Query("api_key") String apiKey,
                                                          @Query("vote_count.gte") String minVoteCount ,
                                                          @Query("sort_by") String sortBy);
}
