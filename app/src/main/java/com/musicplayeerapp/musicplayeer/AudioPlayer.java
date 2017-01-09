package com.musicplayeerapp.musicplayeer;



import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;

import java.io.IOException;


public class AudioPlayer {



    interface Callback{

        void onCompletion();
        void onStateChanged(int state);
    }



    Context mContext;

    Callback mCallback;

    private MediaPlayer mPlayer;

    private int lastPos;

    private int mState = PlaybackState.STATE_NONE;

    private WifiManager.WifiLock mWifiLock;


    private void stateChanged(int state)
    {
        mState = state;
        if (mCallback != null)
            mCallback.onStateChanged(mState);

    }




    public boolean isAlive()
    {
        return mPlayer == null ? false : true;
    }




    public AudioPlayer(Context context, AudioPlayer.Callback callback)
    {
        mContext = context;
        mCallback = callback;
        //createPlayer();

    }

    public int getPlayerState()
    {
        return mState;
    }

    public int getPosition()
    {
        return mPlayer != null ?
                mPlayer.getCurrentPosition() : lastPos;
    }

    public int getDuration()
    {
        return mPlayer != null ?
                mPlayer.getCurrentPosition() : 0;
    }


    public void play(String audioSource)
    {
        if (mPlayer == null) createPlayer();

        if (mPlayer.isPlaying()) mPlayer.stop();

        mPlayer.reset();

        try {
            mPlayer.setDataSource(audioSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
    }




    public void stop()
    {
        if (mPlayer != null)
        {
            mPlayer.stop();
            stateChanged(PlaybackStateCompat.STATE_STOPPED);
        }
    }

    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying())
        {
            mPlayer.pause();
            lastPos = mPlayer.getCurrentPosition();
            stateChanged(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    public void resume() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.seekTo(lastPos);
            mPlayer.start();
            stateChanged(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    public void seekTo(int position)
    {
        if (mPlayer != null && (mState == PlaybackState.STATE_PLAYING || mState == PlaybackState.STATE_PAUSED))
        {
            mPlayer.seekTo(position);
        }
    }




    MediaPlayer.OnPreparedListener prepListener = new MediaPlayer.OnPreparedListener()
    {
        public void onPrepared(MediaPlayer player)
        {
            if (player != null && !player.isPlaying()) {
                player.start();
                stateChanged(PlaybackStateCompat.STATE_PLAYING);
            }
        }
    };



    MediaPlayer.OnCompletionListener completListener = new MediaPlayer.OnCompletionListener()
    {
        public void onCompletion(MediaPlayer player)
        {
            stateChanged(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT);
            if (mCallback != null)
                mCallback.onCompletion();
        }
    };




    private void createPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(prepListener);
        mPlayer.setOnCompletionListener(completListener);

        mPlayer.setWakeMode(mContext.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


    }

    public void releasePlayer()
    {
        if (mPlayer == null) return;
        if (mPlayer.isPlaying()) mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        mState = PlaybackStateCompat.STATE_NONE;
        stateChanged(mState);
    }






}
