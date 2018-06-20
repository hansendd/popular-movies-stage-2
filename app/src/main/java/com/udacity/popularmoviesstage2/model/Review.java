package com.udacity.popularmoviesstage2.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

/**
 * Created by hansend on 6/15/2018.
 */

public class Review {
    private String author;
    private String content;

    public Review(JSONObject jsonObjectMovie) throws JSONException {
        author = getJSONObjectValueString(jsonObjectMovie,
                "author");
        content = getJSONObjectValueString(jsonObjectMovie,
                "content");
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    private static String getJSONObjectValueString(JSONObject jsonObject,
                                                   String name) throws JSONException {
        return jsonObject.getString(name);
    }
}
