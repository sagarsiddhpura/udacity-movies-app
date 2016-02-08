package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SIDD on 07-Feb-16.
 */
public class ReviewsApiResult {

    @JsonProperty("id")
    private String id;

    @JsonProperty("author")
    private String author;

    @JsonProperty("content")
    private String content;

    @JsonProperty("url")
    private String url;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getReview() {
        return author + ": " + content;
    }
}
