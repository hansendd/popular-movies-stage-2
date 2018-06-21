package com.udacity.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.udacity.popularmoviesstage2.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by hansend on 6/15/2018.
 */

public class MovieContentProvider extends ContentProvider {
    // The To Do app (Lesson 09) from the first phase of the Google Scholarship exercises was used
    // to assist in the making of this class

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
        return uriMatcher;
    }

    private MovieDbHelper movieDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri,
                      ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = sqLiteDatabase.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the tasks directory
            case MOVIES:
                retCursor =  sqLiteDatabase.query(TABLE_NAME,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                                  null,
                                                  null,
                                                  sortOrder);
            case MOVIES_WITH_ID:
                retCursor =  sqLiteDatabase.query(TABLE_NAME,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                         null,
                                          null,
                                                  sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public int delete(@NonNull Uri uri,
                      String selection,
                      String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = sqLiteDatabase.delete(TABLE_NAME, "movie_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return tasksDeleted;
    }


    @Override
    public int update(@NonNull Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
