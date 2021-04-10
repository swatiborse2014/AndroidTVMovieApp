package com.example.androidtvmovieapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.media2.exoplayer.external.util.Util;

import com.example.androidtvmovieapp.R;
import com.example.androidtvmovieapp.model.MovieDetail;
import com.example.androidtvmovieapp.network.APIInterface;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;

public class PlayerActivity extends Activity implements MediaSourceFactory {

    private static final String TAG = "PlayerActivity";

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private DataSource.Factory dataSourceFactory;
    private ImageButton img_next;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mPlayerView = findViewById(R.id.player_view);
        img_next=findViewById(R.id.img_next);
        TextView nowInfo = findViewById(R.id.nowInfo);

        final MovieDetail movie = getIntent().getParcelableExtra(DetailsActivity.MOVIE);
        nowInfo.setText(movie.getTitle());

        String userAgent = Util.getUserAgent(this, getString(R.string.app_name));

        dataSourceFactory =
                new com.google.android.exoplayer2.upstream.DefaultDataSourceFactory(
                        this, com.google.android.exoplayer2.util.Util.getUserAgent(this,userAgent));
        initializePlayer();
    }

    private void initializePlayer() {

        LoadControl loadControl = new DefaultLoadControl();

        mPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), loadControl, null);

        mPlayerView.setPlayer(mPlayer);
        String URL_PATH = APIInterface.VIDEO_URL;
        String URL = URL_PATH + "BigBuckBunny.mp4";

        String urlExtension = URL.substring(URL.lastIndexOf("."));

        if (urlExtension.equalsIgnoreCase(".mp4")) {
            mPlayer.prepare(buildMediaSource(Uri.parse(URL)));

        } else if (urlExtension.equalsIgnoreCase(".m3u8")) {
            mPlayer.prepare(buildMediaSource(Uri.parse(URL)));

        } else if (urlExtension.equalsIgnoreCase(".mpd")) {
            mPlayer.prepare(buildMediaSource(Uri.parse(URL)));

        } else {
            Toast.makeText(this, "This streaming can't support.", Toast.LENGTH_SHORT).show();
        }

        setPlayPause(true);
        executeListener();
    }

    private MediaSource buildMediaSource(Uri uri) {
        @SuppressLint("RestrictedApi") @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                Toast.makeText(this, "DASH", Toast.LENGTH_SHORT).show();
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                Toast.makeText(this, "SS", Toast.LENGTH_SHORT).show();
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                Toast.makeText(this, "HLS", Toast.LENGTH_SHORT).show();
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                Toast.makeText(this, "OTHER", Toast.LENGTH_SHORT).show();
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }


    private void executeListener() {
        mPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray
                    trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void setPlayPause(boolean play) {
        mPlayer.setPlayWhenReady(play);
        mPlayer.getPlaybackState();
    }

    @Override
    protected void onPause() {
        setPlayPause(false);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onError(Exception e) {
        Log.e(TAG, "Playback failed", e);
        Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public MediaSourceFactory setDrmSessionManager(DrmSessionManager<?> drmSessionManager) {
        return null;
    }

    @Override
    public MediaSource createMediaSource(Uri uri) {
        return null;
    }

    @Override
    public int[] getSupportedTypes() {
        return new int[0];
    }
}

