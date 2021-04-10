package com.example.androidtvmovieapp.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.androidtvmovieapp.common.GlideBackgroundManager;
import com.example.androidtvmovieapp.R;
import com.example.androidtvmovieapp.fragment.DetailFragment;
import com.example.androidtvmovieapp.model.MovieDetail;
import com.example.androidtvmovieapp.network.APIInterface;

public class DetailsActivity extends MainActivity {
    public static final String MOVIE = "Movie";
    private GlideBackgroundManager mBackgroundManager;
    private MovieDetail movieDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        movieDetail = (MovieDetail) getIntent().getParcelableExtra(DetailsActivity.MOVIE);

        if (savedInstanceState == null) {
            DetailFragment fragment =
                    new DetailFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, fragment)
                    .commit();
        }

        mBackgroundManager = new GlideBackgroundManager(this);
        if (movieDetail != null && movieDetail.getBackdropPath() != null) {
            mBackgroundManager.loadImage(APIInterface.BACKDROP_URL + movieDetail.getBackdropPath());
        } else {
            mBackgroundManager.setBackground(ContextCompat.getDrawable(this, R.drawable.app_icon_your_company));
        }
    }
}