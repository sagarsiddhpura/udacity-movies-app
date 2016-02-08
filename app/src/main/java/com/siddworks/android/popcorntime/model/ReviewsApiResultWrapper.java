package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class ReviewsApiResultWrapper {

    @JsonProperty("id")
    private int id;

    @JsonProperty("page")
    private int page;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    @JsonProperty("results")
    private ArrayList<ReviewsApiResult> results;

    public ArrayList<ReviewsApiResult> getReviews() {
        ArrayList<ReviewsApiResult> filteredReviews = new ArrayList<>();
        if(results != null) {
            for (ReviewsApiResult result : results) {
                if(result.getContent() != null && result.getAuthor() != null) {
                    filteredReviews.add(result);
                }
            }
        }
        return filteredReviews;
    }
}
