package com.udacity.popularmoviesstage2.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hansend on 6/8/2018.
 */

public class Video {
    private String key;
    private String name;
    private String type;
    private String site;

    public Video(JSONObject jsonObjectMovie) throws JSONException {
        key = getJSONObjectValueString(jsonObjectMovie,
                                      "key");
        name = getJSONObjectValueString(jsonObjectMovie,
                                       "name");
        type = getJSONObjectValueString(jsonObjectMovie,
                                       "type");
        site = getJSONObjectValueString(jsonObjectMovie,
                                       "site");
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

    private static String getJSONObjectValueString(JSONObject jsonObject,
                                                   String name) throws JSONException {
        return jsonObject.getString(name);
    }

    public String buildVideoPath() {
        final String _URL_TEMPLATE = "%s";
        final String _YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=%s";
        return String.format(_URL_TEMPLATE,
                             String.format(_YOUTUBE_BASE_URL,
                                           key));
    }

    public boolean isValidType() {
        return "YOUTUBE".equals(site.toUpperCase());
    }

}
