package com.siddworks.android.popcorntime.util;

import com.siddworks.android.popcorntime.data.Api;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class PopcornTimeUtil {
    public static Api getApi() {

        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        return api;
    }

    public void getPopularMovies(Api api) {
//        call.enqueue(new Callback<MovieWrapper>() {
//
//            @Override
//            public void onResponse(Response<MovieWrapper> response, Retrofit retrofit) {
//                Log.d(isLoggingEnabled, TAG, "onResponse  response:" + response);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d(isLoggingEnabled, TAG, "onFailure  t:" + t);
//            }
//        });


    }
}
