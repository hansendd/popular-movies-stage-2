package com.udacity.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hansend on 6/15/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    // The To Do app (Lesson 09) from the first phase of the Google Scholarship exercises was used
    // to assist in the making of this class
    private static final String DATABASE_NAME = "favoriteMoviesDb.db";
    public static final int VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID    + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE       + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
