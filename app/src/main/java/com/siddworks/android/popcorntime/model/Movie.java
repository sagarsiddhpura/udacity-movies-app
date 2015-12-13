package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class Movie extends MediaBasic implements Serializable {
    private static final long serialVersionUID = 100L;

    @JsonProperty("_id")
    private String mediaId;

    @Override
    public String toString() {
        return "--------------Movie{" +
//                "mediaId='" + mediaId + '\'' +
//                "id=" + id +
//                ", mediaType=" + mediaType +
//                ", backdropPath='" + backdropPath + '\'' +
//                ", posterPath='" + posterPath + '\'' +
                ", popularity=" + popularity +
                ", voteAverage=" + voteAverage +
//                ", voteCount=" + voteCount +
//                ", adult=" + adult +
//                ", originalTitle='" + originalTitle + '\'' +
//                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
//                ", video=" + video +
//                ", genreIds=" + genreIds +
//                ", originalLanguage='" + originalLanguage + '\'' +
//                ", overview='" + overview + '\'' +
//                ", revenue=" + revenue +
                "}--------------";
    }

    // popularity, voteCount, userRating, genreIds, originalLanguage, revenue

    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("video")
    private Boolean video = null;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("revenue")
    private long revenue = 0L;

    public Movie() {
        super.setMediaType(MediaType.MOVIE);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
