package com.udacity.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hansend on 6/15/2018.
 */

public class MovieContract {
    // The To Do app (Lesson 09) from the first phase of the Google Scholarship exercises was used
    // to assist in the making of this class
    public static final String AUTHORITY = "com.udacity.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
    }
}
