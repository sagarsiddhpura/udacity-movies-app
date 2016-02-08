package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.siddworks.android.popcorntime.util.Constants;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class VideosApiResult {

    @JsonProperty("id")
    private String id;

    @JsonProperty("iso_639_1")
    private String iso_639_1;

    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    public int getSize() {
        return size;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSite() {
        return site;
    }

    @JsonProperty("site")
    private String site;

    @JsonProperty("size")
    private int size;

    @JsonProperty("type")
    private String type;

    public String getThumbnailUrl() {
        if(site.equals(Constants.YOUTUBE)) {
            String BASE_URL="http://img.youtube.com/vi/";
            String url=BASE_URL + key + "/0.jpg";
            return url;
        }
        return null;
    }

    public String getVideoUrl() {
        if(site.equals(Constants.YOUTUBE)) {
            String url= "https://www.youtube.com/watch?v=" + key ;
            return url;
        }
        return null;
    }
}
