package com.example.androidtvmovieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.leanback.app.DetailsFragment;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewLogoPresenter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.androidtvmovieapp.activity.DetailsActivity;
import com.example.androidtvmovieapp.R;
import com.example.androidtvmovieapp.activity.PlayerActivity;
import com.example.androidtvmovieapp.model.MovieDetail;
import com.example.androidtvmovieapp.network.APIInterface;
import com.example.androidtvmovieapp.presenter.CustomMovieDetailsPresenter;
import com.example.androidtvmovieapp.presenter.MovieDetailsDescriptionPresenter;

@SuppressLint("ValidFragment")
public class DetailFragment extends DetailsFragment {
    public static String TRANSITION_NAME = "poster_transition";

    private MovieDetail movie;
    String mYoutubeID;
    private ArrayObjectAdapter mAdapter;
    private CustomMovieDetailsPresenter mFullWidthMovieDetailsPresenter;
    private DetailsOverviewRow mDetailsOverviewRow;
    private static final int ACTION_PLAY = 0;
    private static final int ACTION_TRAILER = 1;
    private static final int ACTION_RENT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = (MovieDetail) getActivity().getIntent().getParcelableExtra(DetailsActivity.MOVIE);

        setUpAdapter();
        setUpDetailsOverviewRow();
    }

    private void setUpAdapter() {
        mFullWidthMovieDetailsPresenter = new CustomMovieDetailsPresenter(new MovieDetailsDescriptionPresenter(),
                new DetailsOverviewLogoPresenter());

        FullWidthDetailsOverviewSharedElementHelper helper = new FullWidthDetailsOverviewSharedElementHelper();
        helper.setSharedElementEnterTransition(getActivity(), TRANSITION_NAME); // the transition name is important
        mFullWidthMovieDetailsPresenter.setListener(helper); // Attach the listener
        mFullWidthMovieDetailsPresenter.setParticipatingEntranceTransition(false);

        mFullWidthMovieDetailsPresenter.setOnActionClickedListener(action -> {
            int actionId = (int) action.getId();
            switch (actionId) {
                case 0:
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtra(DetailsActivity.MOVIE, movie);
                    startActivity(intent);
                    break;
                case 1:
                    if (mYoutubeID != null) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + mYoutubeID)));
                    }
                    break;
            }
        });


        ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
        classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mFullWidthMovieDetailsPresenter);
        classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mAdapter = new ArrayObjectAdapter(classPresenterSelector);
        setAdapter(mAdapter);
    }

    private void setUpDetailsOverviewRow() {
        mDetailsOverviewRow = new DetailsOverviewRow(new MovieDetail());
        mAdapter.add(mDetailsOverviewRow);
        loadImage(APIInterface.POSTER_URL + movie.getPosterPath());
        Glide.with(getActivity()).load(APIInterface.POSTER_URL + movie.getBackdropPath())
                .placeholder(R.drawable.app_icon_your_company).dontAnimate()
                .centerCrop()
                .error(R.drawable.default_background);

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        actionAdapter.add(
                new Action(
                        ACTION_PLAY,
                        getResources().getString(R.string.play),
                        getResources().getString(R.string.free)));
        actionAdapter.add(
                new Action(
                        ACTION_TRAILER,
                        getResources().getString(R.string.watch_trailer_1),
                        getResources().getString(R.string.free)));
        actionAdapter.add(
                new Action(
                        ACTION_RENT,
                        getResources().getString(R.string.buy_1),
                        getResources().getString(R.string.buy_2)));

        mDetailsOverviewRow.setActionsAdapter(actionAdapter);

        mAdapter.add(mDetailsOverviewRow);
    }

    private SimpleTarget<GlideDrawable> mGlideDrawableSimpleTarget = new SimpleTarget<GlideDrawable>() {
        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            mDetailsOverviewRow.setImageDrawable(resource);
        }
    };

    private void loadImage(String url) {
        Glide.with(getActivity())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        changePalette(((GlideBitmapDrawable) resource).getBitmap());
                        return false;
                    }
                })
                .into(mGlideDrawableSimpleTarget);
    }

    private void changePalette(Bitmap bitmap) {
        Palette.from(bitmap).generate();
    }

}
