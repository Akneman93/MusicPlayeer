package com.musicplayeerapp.musicplayeer;

import android.content.ComponentName;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AudioPlayFragment extends Fragment {

    private static final String TAG = "AudioPlayFragment";
    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mMediaController;
    private View mView;
    private SeekBar seekBar;
    private MediaBrowserCompat.MediaItem mItem = null;
    private int mItemprogress = 0;
    public final String INIT_URI_PLAY = "INIT_URI_PLAY";
    public final String INTERRUPTED_URI_KEY = "INTERRUPTED_URI_KEY";
    private final String PROGRESS_KEY = "PROGRESS_KEY";







    public AudioPlayFragment() {



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = getArguments();

        if (bundle != null) {
                mItem = MediaItemLoader.getItemFromBundle(bundle);
        }
        else
        {
            Log.i(TAG,"null arguments");
        }

        if (savedInstanceState != null )
        {
            mItem = MediaItemLoader.getItemFromBundle(savedInstanceState);
            mItemprogress = savedInstanceState.getInt(PROGRESS_KEY);
            Log.i(TAG, "SavedInstance: progress " + " " + mItemprogress);
        }



        mView = inflater.inflate(R.layout.audio_play, container, false);

        seekBar = (SeekBar)mView.findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);


        mMediaBrowser = new MediaBrowserCompat(getActivity(),
                new ComponentName(getActivity(), PlayMusicService.class),
                mConnectionCallbacks,
                null);

        mMediaBrowser.connect();
        buildTransportControls();


        return mView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        MediaItemLoader.putItemInBundle(outState, mItem);
        outState.putInt(PROGRESS_KEY, mItemprogress);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener  = new SeekBar.OnSeekBarChangeListener() {


        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            if ( mMediaController!= null )
            {
                int state = mMediaController.getPlaybackState().getState();
                mMediaController.getTransportControls().seekTo(seekBar.getProgress());
                trackProgress();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            stopTracking();
            Log.i(TAG,"start tracking touch");

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser)
            {
                seekBar.setProgress(progress);
            }
        }
    };



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTracking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"destroy");
        mMediaBrowser.disconnect();
        mMediaController.unregisterCallback(controllerCallback);


    }





    public void setAudio(MediaBrowserCompat.MediaItem item)
    {
        TextView artist =  (TextView)mView.findViewById(R.id.Textview_description);
        artist.setText(item.getDescription().getTitle() + " " + item.getDescription().getSubtitle());
        mItem = item;

        if (mMediaController != null) {
            mMediaController.getTransportControls().playFromUri(mItem.getDescription().getMediaUri(), null);
            trackProgress();
        }

    }


    public void setSeekBar(int pos, int max)
    {
        seekBar.setMax(max);
        seekBar.setProgress(pos);
    }




    private final MediaBrowserCompat.ConnectionCallback mConnectionCallbacks =  new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {


            MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();

            try {
                mMediaController =
                        new MediaControllerCompat(getActivity(), token);
            }
            catch (RemoteException ex)
            {
                Log.i(TAG,"Connection failed");
                throw new RuntimeException(ex);
            }

            mMediaController.registerCallback(controllerCallback);


            if (mItem != null)
            {
                setAudio(mItem);
            }



            Log.i(TAG,"Connection succeded");

        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.i(TAG,"Connection failed (onConnectionFailed)");
        }
    };

    public void buildTransportControls()
    {
        final Button button = (Button)mView.findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.i(TAG,"Button pressed");

                int state = mMediaController.getPlaybackState().getState();

                switch (state) {

                    case PlaybackStateCompat.STATE_PLAYING:
                        stopTracking();
                        mMediaController.getTransportControls().pause();
                        Log.i(TAG, "pause call");
                        break;
                    case PlaybackStateCompat.STATE_PAUSED:
                        trackProgress();
                        mMediaController.getTransportControls().play();
                        Log.i(TAG, "play(resume) call");
                        break;
                    default:
                        if (mItem != null) {
                            mMediaController.getTransportControls().playFromUri(mItem.getDescription().getMediaUri(), null);
                            trackProgress();
                        }
                        Log.i(TAG, "play from uri call");
                }


            }
        });
    }


    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    Log.i(TAG,"playback state changed (onMetaDataChanged)");
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat playbackstate)
                {
                    final Button button = (Button)mView.findViewById(R.id.start_button);

                    int state = playbackstate.getState();


                    switch (state) {

                        case PlaybackStateCompat.STATE_PAUSED:
                            button.setText("Play");
                            break;

                        case PlaybackStateCompat.STATE_PLAYING:
                            button.setText("Pause");
                            break;

                        case PlaybackStateCompat.STATE_SKIPPING_TO_NEXT:

                            Log.i(TAG, "skip to next (onPlaybackStateChanged)");

                            /* MediaId is expected to be the index of item in list
                            int id = Integer.parseInt(mItem.getMediaId());
                            Log.i(TAG, "id = " + mItem.getMediaId());
                            id++;//index of next mediaItem
                            MediaBrowserCompat.MediaItem newItem = mItems.get(id % mItems.size());
                            setAudio(newItem);*/
                            break;

                        default:
                            button.setText("Play");
                    }

                }
            };




    //if we should update seekBar progress
    private boolean isTracked = false;


    private Handler mHandler = new Handler();

    /** task for updating seekBar progress */
    private Runnable informAboutState = new Runnable() {

        @Override
        public void run() {
            if (mMediaController != null)
            {
                mMediaController.getTransportControls().sendCustomAction("update",null);
                PlaybackStateCompat state = mMediaController.getPlaybackState();
                if (isTracked && (state.getState() == PlaybackStateCompat.STATE_PLAYING
                        || state.getState() == PlaybackStateCompat.STATE_PAUSED))
                {
                    int pos = (int) state.getPosition();
                    int duration = state.getExtras().getInt("duration");

                    //Log.i(TAG, "Duration " + duration + " Position " + pos);

                    setSeekBar(pos, duration);
                }
            }
            mHandler.postDelayed(this, 200);
        }
    };




    /**starts checking progress of player*/
    private void trackProgress()
    {
        if (isTracked) return;
        getActivity().runOnUiThread(informAboutState);
        isTracked = true;
    }

    /**stops checking progress of player*/
    private void stopTracking()
    {
        if (isTracked) {
            mHandler.removeCallbacks(informAboutState);
            isTracked = false;
        }
    }



}
