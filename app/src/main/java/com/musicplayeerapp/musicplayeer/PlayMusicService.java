package com.musicplayeerapp.musicplayeer;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.List;


    public class PlayMusicService extends MediaBrowserServiceCompat implements AudioPlayer.Callback{

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private AudioPlayer mPlayer;
    private int mPlayerState;
    private int lastPos;
    public static final String TAG = "PlayMusicService";
    public static final String MEDIA_ID_ROOT = "Media_root";

        @Override
        public void onCompletion() {
            updateSessionState();
            mPlayer.releasePlayer();
        }

        @Override
        public void onStateChanged(int state) {

            updateSessionState();
        }

        @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG,"Service oncreate started");

        mPlayer = new AudioPlayer(this, this);


        mMediaSession = new MediaSessionCompat(this, "PlayMusicService");


        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);


        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE|
                        PlaybackStateCompat.ACTION_PLAY_FROM_URI|
                        PlaybackStateCompat.ACTION_SEEK_TO|
                        PlaybackStateCompat.ACTION_STOP
                );
        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(mCallback);

        Context context = getApplicationContext();
        Intent intent = new Intent(context, AudioPlayFragment.class);
        PendingIntent pi = PendingIntent.getActivity(context, 99 /*request code*/,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mMediaSession.setSessionActivity(pi);



        Log.e(TAG,"Service oncreate ended");



        // Set the session's token so that client activities can communicate with it.
        setSessionToken(mMediaSession.getSessionToken());
    }

        @Override
        public boolean onUnbind(Intent intent) {

            Log.e(TAG, "unbind from service");
            return super.onUnbind(intent);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.e(TAG, "service ondestroy");
            mPlayer.releasePlayer();
            mPlayer = null;
            mMediaSession.release();
            mMediaSession = null;
        }

        @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid,
                                 Bundle rootHints) {

        return new BrowserRoot(MEDIA_ID_ROOT, null);

    }

    @Override
    public void onLoadChildren(final String parentMediaId,
                               final Result<List<MediaBrowserCompat.MediaItem>> result) {

        result.sendResult(null);
        return;
    }

    MediaSessionCompat.Callback mCallback = new MediaSessionCompat.Callback(){
        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);

            Log.i(TAG,"session received play request");

            mPlayer.play(uri.toString());
        }

        @Override
        public void onPause() {
            super.onPause();
            mPlayer.pause();
        }

        @Override
        public void onStop() {
            super.onStop();
            mPlayer.stop();
            mPlayer.releasePlayer();
        }

        @Override
        //where is onResume
        public void onPlay() {
            super.onPlay();
            mPlayer.resume();
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
        }
    };

        private void updateSessionState()
        {
            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE|
                                    PlaybackStateCompat.ACTION_PLAY_FROM_URI|
                                    PlaybackStateCompat.ACTION_SEEK_TO|
                                    PlaybackStateCompat.ACTION_STOP
                    );

            long position =  mPlayer.getPosition();

            int state = mPlayer.getPlayerState();

            //noinspection WrongConstant
            mStateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());


            mMediaSession.setPlaybackState(mStateBuilder.build());

        }







}
