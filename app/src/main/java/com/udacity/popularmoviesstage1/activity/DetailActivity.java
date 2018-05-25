package com.udacity.popularmoviesstage1.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviesstage1.R;
import com.udacity.popularmoviesstage1.model.Movie;

import java.text.NumberFormat;

public class DetailActivity extends AppCompatActivity {

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
    }

    private TextView findViewByIdTextView(int id) {
        return (TextView) findViewById(id);
    }

    private void setTextViewText(TextView textView,
                                 String text) {
        textView.setText(text);
    }
}
