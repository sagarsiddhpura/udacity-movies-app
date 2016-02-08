package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class VideosApiResultWrapper {

    @JsonProperty("id")
    private int id;

    @JsonProperty("results")
    private ArrayList<VideosApiResult> results;

    public ArrayList<VideosApiResult> getVideos() {
        ArrayList<VideosApiResult> filteredVideos = new ArrayList<>();
        if(results != null) {
            for (VideosApiResult result : results) {
                if(result.getKey() != null && result.getSite() != null) {
                    filteredVideos.add(result);
                }
            }
        }
        return filteredVideos;
    }
}
