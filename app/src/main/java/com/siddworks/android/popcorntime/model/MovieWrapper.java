package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class MovieWrapper {

    @JsonProperty("page")
    private int page;

    @Override
    public String toString() {
        return "MovieWrapper{" +
                "page=" + page +
                ", totalPages='" + totalPages + '\'' +
                ", totalResults='" + totalResults + '\'' +
                ", movies=" + movies +
                '}';
    }

    @JsonProperty("total_pages")
    private String totalPages;
    @JsonProperty("total_results")
    private String totalResults;
    @JsonProperty("results")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
