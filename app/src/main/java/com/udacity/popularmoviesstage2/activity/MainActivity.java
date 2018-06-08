package com.udacity.popularmoviesstage2.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.adapter.MovieAdapter;
import com.udacity.popularmoviesstage2.model.Movie;
import com.udacity.popularmoviesstage2.utility.NetworkConnectionUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 * All ActionBar logic comes from the Android developer training site:
 * https://developer.android.com/training/appbar/
 */
public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView recyclerViewMovie;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        recyclerViewMovie = findViewById(R.id.recyclerview_movie);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerViewMovie.setLayoutManager(linearLayoutManager);

        recyclerViewMovie.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this,
                                        this,
                                         movieList);

        recyclerViewMovie.setAdapter(movieAdapter);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movie_search));

        loadMovies(Movies.TOP_RATED.id);
    }

    private void loadMovies(int moviesId) {
        ConnectivityManager connectivityManager =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(NetworkConnectionUtility.haveActiveNetworkConnection(connectivityManager)) {
            new MoviesRetrieval().execute(moviesId, 1);
        }
        else {
            NetworkConnectionUtility.displayNoNetworkConnection(this);
        }
    }

    private void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        movieAdapter = new MovieAdapter(this,
                                        this,
                this.movieList);
        recyclerViewMovie.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        clearMovieList();
        loadMovies(menuItem.getItemId());
        return true;
    }

    private void clearMovieList() {
        setMovieList(new ArrayList<Movie>());
    }

    @Override
    public void onClick(Movie movie) {
        // How to pass an object in an intent was used from example at:
        // http://www.developerphil.com/parcelable-vs-serializable/
        Intent movieDetailIntent = new Intent(MainActivity.this,
                                              DetailActivity.class);
        movieDetailIntent.putExtra("movie_data", movie);
        startActivity(movieDetailIntent);
    }

    /*
     * Parts of the following code are from:
     * https://www.androidauthority.com/use-remote-web-api-within-android-app-617869/.
     *
     * Additional info gathered from:
     * https://developer.android.com/reference/java/net/HttpURLConnection
     *
     *
     * This is the first result when doing a Google search of "android connect to web API".
     *
     * It is from 2015 which could be outdated by now, may be a need to look for more recent
     * example.
     *
     * The TMDB "code generation" uses something called "Unirest" or "OkHttpClient" which do not
     * appear to be recoginzed by the IDE when looking at code completion so they may be third
     * party libraries.
     */
    private class MoviesRetrieval extends AsyncTask {
        private final String _API_KEY = Movie._API_KEY;
        private final String _TOP_RATED = "top_rated";
        private final String _POPULAR = "popular";
        private final String _URL_TEMPLATE = "https://api.themoviedb.org/3/movie/%s?page=%s&language=en-US&api_key=%s";

        public List<Movie> doInBackground(Object ... params) {
            try {
                int moviesId = (int) params[0];
                int page = (int) params[1];


                String moviesUrl = getMoviesUrl(moviesId);
                String urlString = buildUrlString(moviesUrl,
                                                  page);
                URL url = buildUrl(urlString);
                return getMovies(url);
            }
            catch (Exception e) {
                // Better error handling here?
                System.out.println(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            setMovieList((ArrayList<Movie>) result);
        }

        private String getMoviesUrl(int moviesId) {
            String moviesUrl = _TOP_RATED;
            if (moviesId == Movies.POPULAR.id) {
                moviesUrl = _POPULAR;
            }
            return moviesUrl;
        }

        private String buildUrlString(String movesUrl,
                                      int page) {
            return String.format(_URL_TEMPLATE,
                                 movesUrl,
                                 page,
                                 _API_KEY);
        }

        private URL buildUrl(String urlString) throws MalformedURLException {
            return new URL(urlString);
        }

        private List<Movie> getMovies(URL url) throws IOException, JSONException {
            HttpURLConnection httpURLConnection = getHttpUrlConnection(url);
            try {
                httpURLConnection.connect();
                String json = getResponseJson(httpURLConnection.getInputStream());
                return createMovieList(json);
            }
            finally {
                httpURLConnection.disconnect();
            }
        }

        private HttpURLConnection getHttpUrlConnection(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }

        private String getResponseJson(InputStream inputStream) throws IOException {
            // Taken from
            // https://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
            // Refactored
            InputStreamReader inputStreamReader = createInputStreamReader(inputStream);
            BufferedReader bufferedReader = createBufferedReader(inputStreamReader);
            return createResponseJson(bufferedReader);
        }

        private InputStreamReader createInputStreamReader(InputStream inputStream) {
            return new InputStreamReader((inputStream));
        }

        private BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
            return new BufferedReader(inputStreamReader);
        }

        private String createResponseJson(BufferedReader bufferedReader) throws IOException {
            // Streams is minimum JDK of 24, we have listed 16 for project
            // So streams should be avoided
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }

        private List<Movie> createMovieList(String json) throws JSONException {
            List<Movie> movieList = new ArrayList<Movie>();
            JSONObject resultsJSONObject = new JSONObject(json);
            JSONArray resultsArray = resultsJSONObject.getJSONArray("results");
            // Does not appear as if there are any for loop or iterators criteria on
            // JSON Array, use older for loop logic
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonObjectMovie = resultsArray.getJSONObject(i);
                movieList.add(new Movie(jsonObjectMovie));
            }
            return movieList;
        }
    }

    private enum Movies {
        TOP_RATED(R.id.menu_item_top_rated),
        POPULAR(R.id.menu_item_popular);

        private int id;
        Movies(int id) {
            this.id = id;
        }
    }
}
