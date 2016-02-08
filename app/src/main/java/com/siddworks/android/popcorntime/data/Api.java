package com.siddworks.android.popcorntime.data;

import com.siddworks.android.popcorntime.model.MovieWrapper;
import com.siddworks.android.popcorntime.model.ReviewsApiResultWrapper;
import com.siddworks.android.popcorntime.model.VideosApiResultWrapper;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by SIDD on 26-Nov-15.
 */
public interface Api {

    public static final String ENDPOINT = "http://api.themoviedb.org/3/";

    // Get Popular Movies API
    @GET("discover/movie")
    public Observable<MovieWrapper> getPopularMovies(@Query("api_key") String apiKey ,
                                                     @Query("vote_count.gte") String minVoteCount ,
                                                     @Query("sort_by") String sortBy);

    // Get Popular rated Movies API
    @GET("discover/movie")
    public Observable<MovieWrapper> getHighestRatedMovies(@Query("api_key") String apiKey,
                                                          @Query("vote_count.gte") String minVoteCount ,
                                                          @Query("sort_by") String sortBy);

    // Get trailers API
    @GET("movie/{id}/videos")
    public Observable<VideosApiResultWrapper> getVideosForMovie(@Path("id") String id,
                                                                @Query("api_key") String apiKey);

    // Get reviews API
    @GET("movie/{id}/reviews")
    public Observable<ReviewsApiResultWrapper> getReviewsForMovie(@Path("id") String id,
                                                                @Query("api_key") String apiKey);
}
