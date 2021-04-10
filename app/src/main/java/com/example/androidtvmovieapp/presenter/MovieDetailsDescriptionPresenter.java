package com.example.androidtvmovieapp.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import com.example.androidtvmovieapp.R;
import com.example.androidtvmovieapp.model.MovieDetail;

public class MovieDetailsDescriptionPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_details, parent, false);
        return new MovieDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        MovieDetail movie = (MovieDetail) item;
        MovieDetailsViewHolder holder = (MovieDetailsViewHolder) viewHolder;
        holder.bind(movie);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
