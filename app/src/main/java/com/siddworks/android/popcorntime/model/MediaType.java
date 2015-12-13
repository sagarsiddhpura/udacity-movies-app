package com.siddworks.android.popcorntime.model;

import android.text.TextUtils;

/**
 * Created by SIDD on 27-Nov-15.
 */
public enum MediaType {

    /**
     * Movie media type
     */
    MOVIE,
    /**
     * TV Show media type
     */
    TV,
    /**
     * TV Episode media type
     */
    EPISODE;

    /**
     * Convert a string into an Enum type
     *
     * @param mediaType
     * @return
     * @throws IllegalArgumentException If type is not recognised
     *
     */
    public static MediaType fromString(String mediaType) {
        if (!TextUtils.isEmpty(mediaType)) {
            try {
                return MediaType.valueOf(mediaType.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("MediaType " + mediaType + " does not exist.", ex);
            }
        }
        throw new IllegalArgumentException("MediaType must not be null");
    }
}