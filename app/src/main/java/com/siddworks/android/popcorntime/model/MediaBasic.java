package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.siddworks.android.popcorntime.util.Constants;

import java.io.Serializable;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class MediaBasic  extends AbstractJsonMapping implements Serializable, Identification {

    private static final long serialVersionUID = 100L;

    @JsonProperty("id")
    int id;
    MediaType mediaType;
    @JsonProperty("backdrop_path")
    String backdropPath;
    @JsonProperty("poster_path")
    String posterPath;
    @JsonProperty("popularity")
    float popularity;
    @JsonProperty("vote_average")
    float voteAverage;
    @JsonProperty("vote_count")
    int voteCount;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    @JsonSetter("media_type")
    public void setMediaType(String mediaType) {
        this.mediaType = MediaType.fromString(mediaType);
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getFullBackdropPath() {
        return Constants.BACKDROP_API_URL_ROOT + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getFullPosterPath() {
        return Constants.POSTER_API_URL_ROOT + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "\n\nMediaBasic{" +
                "id=" + id +
                ", mediaType=" + mediaType +
                ", backdropPath='" + backdropPath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", popularity=" + popularity +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
}
