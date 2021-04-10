package com.example.androidtvmovieapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtvmovieapp.common.Config;
import com.example.androidtvmovieapp.activity.DetailsActivity;
import com.example.androidtvmovieapp.common.GlideBackgroundManager;
import com.example.androidtvmovieapp.common.MovieRow;
import com.example.androidtvmovieapp.R;
import com.example.androidtvmovieapp.model.MovieDetail;
import com.example.androidtvmovieapp.model.MovieResponse;
import com.example.androidtvmovieapp.network.APIClient;
import com.example.androidtvmovieapp.network.APIInterface;
import com.example.androidtvmovieapp.presenter.MoviePresenter;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.StringReader;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BrowseFragment implements OnItemViewSelectedListener {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;

    private Timer mBackgroundTimer;
    private GlideBackgroundManager mBackgroundManager;
    private static final int NOW_PLAYING = 0;

    SparseArray<MovieRow> mRows;
    APIInterface apiInterface;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mBackgroundManager = new GlideBackgroundManager(getActivity());


        setupUIElements();
        createDataRows();
        createRows();
        prepareEntranceTransition();
        fetchNowPlayingMovies();
        setupEventListeners();
    }

    private void createRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for (int i = 0; i < mRows.size(); i++) {
            MovieRow row = mRows.get(i);
            HeaderItem headerItem = new HeaderItem(row.getId(), row.getTitle());
            ListRow listRow = new ListRow(headerItem, row.getAdapter());
            rowsAdapter.add(listRow);
        }
        setAdapter(rowsAdapter);
        setOnItemViewSelectedListener(this);
    }

    private void createDataRows() {
        mRows = new SparseArray<>();
        MoviePresenter moviePresenter = new MoviePresenter(getActivity());
        mRows.put(NOW_PLAYING, new MovieRow()
                .setId(NOW_PLAYING)
                .setAdapter(new ArrayObjectAdapter(moviePresenter))
                .setTitle("Now Playing")
                .setPage(1)
        );

    }

    private void fetchNowPlayingMovies() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.getMovieData(Config.API_KEY_URL, String.valueOf(mRows.get(NOW_PLAYING).getPage()));
        Log.d("URL", call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            Gson gson = new Gson();
                            Reader reader = new StringReader(res);
                            MovieResponse movieResponse = gson.fromJson(reader, MovieResponse.class);

                            MovieRow row = mRows.get(NOW_PLAYING);
                            row.setPage(row.getPage() + 1);

                            for (MovieDetail m : movieResponse.getResults()) {
                                if (m.getPosterPath() != null) {
                                    row.getAdapter().add(m);
                                    startEntranceTransition();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void setupUIElements() {
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }


    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof MovieDetail) {
            MovieDetail movie = (MovieDetail) item;
            if (movie.getBackdropPath() != null) {
                mBackgroundManager.loadImage(APIInterface.BACKDROP_URL + movie.getBackdropPath());
            } else {
                // If there is no backdrop for the movie we just use a default one
                mBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
            }
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof MovieDetail) {
                MovieDetail movieDetail = (MovieDetail) item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movieDetail);
                getActivity().startActivity(intent);

            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof MovieDetail) {
//                mBackgroundUri = ((MovieDetail) item).getBackgroundImageUrl();
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    updateBackground(mBackgroundUri);
//                }
//            });
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}