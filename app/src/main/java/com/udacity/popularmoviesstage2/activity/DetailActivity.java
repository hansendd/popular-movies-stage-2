package com.udacity.popularmoviesstage2.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.adapter.MovieAdapter;
import com.udacity.popularmoviesstage2.adapter.VideoAdapter;
import com.udacity.popularmoviesstage2.model.Movie;
import com.udacity.popularmoviesstage2.model.Video;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        VideoAdapter.VideoAdapterOnClickHandler {

    // Video Addition
    private RecyclerView recyclerViewVideo;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<Video>();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie movie = (Movie) getIntent().getParcelableExtra("movie_data");

        TextView title = findViewById(R.id.textview_title);
        TextView releaseDate = findViewByIdTextView(R.id.textview_release_date);
        TextView voteAverage = findViewByIdTextView(R.id.textview_vote_average);
        TextView plot = findViewByIdTextView(R.id.textview_plot);
        final ImageView moviePoster = (ImageView) findViewById(R.id.imageview_movie_poster_detail);

        setTextViewText(title,
                        movie.getTitle());
        setTextViewText(releaseDate,
                        movie.getReleaseDate());
        setTextViewText(voteAverage,
                        (NumberFormat.getNumberInstance().format(movie.getVoteAverage())));
        setTextViewText(plot,
                        movie.getOverview());

        Picasso
            .with(this)
            .load(movie.buildPosterPath())
            .into(moviePoster);


        // Video Addition
        recyclerViewVideo = findViewById(R.id.recyclerview_movie_detail_video);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerViewVideo.setLayoutManager(linearLayoutManager);

        recyclerViewVideo.setHasFixedSize(true);

        videoAdapter = new VideoAdapter(this,
                                        this,
                                         videoList);

        recyclerViewVideo.setAdapter(videoAdapter);
        loadVideos(movie.getId());
    }

    private void loadVideos(String movieId) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(NetworkConnectionUtility.haveActiveNetworkConnection(connectivityManager)) {
            new VideosRetrieval().execute(movieId);
        }
        else {
            NetworkConnectionUtility.displayNoNetworkConnection(this);
        }
    }

    private void setVideoList(List<Video> movieList) {
        this.videoList.clear();
        this.videoList.addAll(movieList);
        videoAdapter = new VideoAdapter(this,
                                        this,
                                         this.videoList);
        recyclerViewVideo.setAdapter(videoAdapter);
    }

    private TextView findViewByIdTextView(int id) {
        return (TextView) findViewById(id);
    }

    private void setTextViewText(TextView textView,
                                 String text) {
        textView.setText(text);
    }

    @Override
    public void onClick(Video video) {
        if (video.isValidType()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video.buildVideoPath())));
        }
        else {
            Toast.makeText(this, R.string.unrecognized_video_format, Toast.LENGTH_SHORT).show();
        }
    }

    private class VideosRetrieval extends AsyncTask {
        private final String _API_KEY = Movie._API_KEY;
        private final String _URL_TEMPLATE = "https://api.themoviedb.org/3/movie/%s/videos?language=en-US&api_key=%s";

        public List<Video> doInBackground(Object ... params) {
            try {
                String movieId = (String) params[0];

                String urlString = buildUrlString(movieId);
                URL url = buildUrl(urlString);
                return getVideos(url);
            }
            catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            setVideoList((ArrayList<Video>) result);
        }

        private String buildUrlString(String movieId) {
            return String.format(_URL_TEMPLATE,
                                 movieId,
                                 _API_KEY);
        }

        private URL buildUrl(String urlString) throws MalformedURLException {
            return new URL(urlString);
        }

        private List<Video> getVideos(URL url) throws IOException, JSONException {
            HttpURLConnection httpURLConnection = getHttpUrlConnection(url);
            try {
                httpURLConnection.connect();
                String json = getResponseJson(httpURLConnection.getInputStream());
                return createVideoList(json);
            }
            finally {
                httpURLConnection.disconnect();
            }
        }

        private HttpURLConnection getHttpUrlConnection(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }

        private String getResponseJson(InputStream inputStream) throws IOException {
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
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }

        private List<Video> createVideoList(String json) throws JSONException {
            List<Video> videoList = new ArrayList<Video>();
            JSONObject resultsJSONObject = new JSONObject(json);
            JSONArray resultsArray = resultsJSONObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonObjectVideo = resultsArray.getJSONObject(i);
                videoList.add(new Video(jsonObjectVideo));
            }
            return videoList;
        }
    }
}
