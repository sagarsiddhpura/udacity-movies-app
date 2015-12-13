package com.siddworks.android.popcorntime.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;

/**
 * Created by SIDD on 27-Nov-15.
 */
public class AbstractJsonMapping implements Serializable {

    private static final long serialVersionUID = 100L;

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    protected void handleUnknown(String key, Object value) {
        StringBuilder unknown = new StringBuilder(this.getClass().getSimpleName());
        unknown.append(": Unknown property='").append(key);
        unknown.append("' value='").append(value).append("'");

//        LOG.trace(unknown.toString());
    }

    @Override
    public String toString() {
        return "AbstractJsonMapping";
    }
}
