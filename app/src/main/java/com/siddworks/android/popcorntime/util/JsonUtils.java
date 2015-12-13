package com.siddworks.android.popcorntime.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
 * Created by SIDD on 28-Nov-15.
 */
public class JsonUtils {

    public static String makeJson(Object obj) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object makeObject(String json, Class objClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(json, objClass);
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
