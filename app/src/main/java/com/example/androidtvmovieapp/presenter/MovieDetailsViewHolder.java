package com.example.androidtvmovieapp.presenter;

import android.view.View;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.example.androidtvmovieapp.model.MovieDetail;

public class MovieDetailsViewHolder extends Presenter.ViewHolder {

    private TextView detail_movie_name;
    private TextView movie_synopsis;
    private View itemView;

    public MovieDetailsViewHolder(View view) {
        super(view);
        itemView = view;
    }

    public void bind(MovieDetail movie) {
        if (movie != null && movie.getTitle() != null) {
            detail_movie_name.setText(movie.getTitle());
            movie_synopsis.setText(movie.getOverview());
        }
    }
}
