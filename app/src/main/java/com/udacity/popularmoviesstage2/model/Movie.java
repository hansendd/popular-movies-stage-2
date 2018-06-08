package com.udacity.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by hansend on 5/18/2018.
 *
 *  https://developers.themoviedb.org/3/movies/get-popular-movies
  * https://developers.themoviedb.org/3/movies/get-top-rated-movies
 */

public class Movie implements Parcelable {
    final public static String _API_KEY = "<<API_KEY>>";
    private String posterPath;
    private String title;
    private String releaseDate;
    private BigDecimal voteAverage;
    private String overview;
    private BigDecimal page;
    private String id;

    public Movie(JSONObject jsonObjectMovie) throws JSONException {
//        Log.d("Movie", jsonObjectMovie.toString());
        posterPath = getJSONObjectValueString(jsonObjectMovie,
                                              "poster_path");
        title = getJSONObjectValueString(jsonObjectMovie,
                                         "title");
        releaseDate = getJSONObjectValueString(jsonObjectMovie,
                                               "release_date");
        voteAverage = getJSONObjectValueBigDecimal(jsonObjectMovie,
                                                   "vote_average");
        overview = getJSONObjectValueString(jsonObjectMovie,
                                            "overview");
        id = getJSONObjectValueString(jsonObjectMovie,
                                      "id");
//        page = getJSONObjectValueBigDecimal(jsonObjectMovie,
//                                            "page");
    }

    private static String getJSONObjectValueString(JSONObject jsonObject,
                                                    String name) throws JSONException {
        return jsonObject.getString(name);
    }

    private static BigDecimal getJSONObjectValueBigDecimal(JSONObject jsonObject,
                                                           String name) throws JSONException {
        return new BigDecimal(jsonObject.getString(name));
    }


    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public BigDecimal getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public BigDecimal getPage() {
        return page;
    }

    public String getId() { return id; }

    public String buildPosterPath() {
        final String _URL_TEMPLATE = "%s/%s/%s";
        final String _BASE_URL = "http://image.tmdb.org/t/p";
        final String _SIZE = "w185";
        return String.format(_URL_TEMPLATE,
                             _BASE_URL,
                             _SIZE,
                             posterPath);
    }

    // Taken from http://www.developerphil.com/parcelable-vs-serializable/
    public Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = new BigDecimal(in.readDouble());
        overview = in.readString();
        id = in.readString();
//        page = new BigDecimal(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel dest,
                              int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage.doubleValue());
        dest.writeString(overview);
        dest.writeString(id);
//        dest.writeInt(page.toBigInteger().intValue());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
