package com.example.androidtvmovieapp.presenter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.Presenter;

import com.example.androidtvmovieapp.model.MovieDetail;

public abstract class AbstractMoviePresenter<T extends BaseCardView> extends Presenter {

    private static final String TAG = "AbstractMoviePresenter";

    private final Context mContext;

    protected AbstractMoviePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent) {
        T cardView = onCreateView();
        return new ViewHolder(cardView);
    }

    @Override
    public final void onBindViewHolder(ViewHolder viewHolder, Object item) {
        MovieDetail card = (MovieDetail) item;
        onBindViewHolder(card, (T) viewHolder.view);
    }

    @Override
    public final void onUnbindViewHolder(ViewHolder viewHolder) {
        onUnbindViewHolder((T) viewHolder.view);
    }

    public void onUnbindViewHolder(T cardView) {
        // Nothing to clean up. Override if necessary.
    }

    protected abstract T onCreateView();

    public abstract void onBindViewHolder(MovieDetail card, T cardView);


}

