package com.udacity.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.model.Movie;

import java.util.List;

/**
 * Created by hansend on 5/18/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private List<Movie> movieList;
    private final Context context;

    final private MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(@NonNull Context context,
                        MovieAdapterOnClickHandler movieAdapterOnClickHandler,
                        List<Movie> movieList) {
        this.context = context;
        this.movieAdapterOnClickHandler = movieAdapterOnClickHandler;
        this.movieList = movieList;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                     int viewType) {
        View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.movie_list_item, viewGroup, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder movieAdapterViewHolder,
                                 int position) {
        Movie movie = movieList.get(position);

        Picasso
            .with(context)
            .load(movie.buildPosterPath())
            .into(movieAdapterViewHolder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == movieList) return 0;
        return movieList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;

        public MovieAdapterViewHolder(View view) {
            super(view);

            moviePoster = (ImageView) view.findViewById(R.id.imageview_movie_poster);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movieList.get(adapterPosition);
            movieAdapterOnClickHandler.onClick(movie);
        }
    }
}
