package com.example.androidtvmovieapp.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.BaseCardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.androidtvmovieapp.R;
import com.example.androidtvmovieapp.model.MovieDetail;
import com.example.androidtvmovieapp.network.APIInterface;

public class MoviePresenter extends AbstractMoviePresenter<BaseCardView> {

    private final Context mContext;
    private int mSelectedBackgroundColor = -1;
    private int mDefaultBackgroundColor = -1;

    public MoviePresenter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected BaseCardView onCreateView() {
        mDefaultBackgroundColor =
                ContextCompat.getColor(getContext(), R.color.colorFocus);
        mSelectedBackgroundColor =
                ContextCompat.getColor(getContext(), R.color.colorAccent);

        BaseCardView cardView = new BaseCardView(getContext(), null, R.style.SideInfoCardStyle) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.addView(LayoutInflater.from(getContext()).inflate(R.layout.info_card_movie, null)); //layout of item attached
        updateCardBackgroundColor(cardView, false);
        return cardView;
    }

    private void updateCardBackgroundColor(BaseCardView view, boolean selected) {
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
        int textColor = selected ? Color.WHITE : Color.BLACK;
        view.setBackgroundColor(color);
        ((TextView) view.findViewById(R.id.movie_title)).setTextColor(textColor);
    }

    @Override
    public void onBindViewHolder(MovieDetail card, BaseCardView cardView) {
        ImageView imageView = cardView.findViewById(R.id.movie_image);
        TextView movieTitle = cardView.findViewById(R.id.movie_title);

        movieTitle.setText(card.getTitle());

        if (card.getPosterPath() != null) {
            Glide.with(mContext).load(APIInterface.POSTER_URL + card.getPosterPath())
                    .placeholder(R.drawable.app_icon_your_company).dontAnimate().into(imageView);
        }
    }

    @Override
    public void onUnbindViewHolder(BaseCardView cardView) {
        super.onUnbindViewHolder(cardView);
    }
}