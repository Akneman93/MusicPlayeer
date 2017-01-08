package com.musicplayeerapp.musicplayeer;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
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
import android.widget.TextView;



public class AudioPlayFragment extends Fragment {

    private static final String TAG = "AudioPlayFragment";

    MediaBrowserCompat mMediaBrowser;
    MediaControllerCompat mMediaController;
    View mView;

   // private OnFragmentInteractionListener mListener;

    public AudioPlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.audio_play, container, false);

        mMediaBrowser = new MediaBrowserCompat(getActivity(),
                new ComponentName(getActivity(), PlayMusicService.class),
                mConnectionCallbacks,
                null);

        mMediaBrowser.connect();
        buildTransportControls();
        return mView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mListener = null;
        mMediaBrowser.disconnect();
        mMediaController.unregisterCallback(controllerCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"AudioPlayFragment destroy");
    }



    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void setAudio(MediaBrowserCompat.MediaItem audioInfo)
    {
        TextView artist =  (TextView)mView.findViewById(R.id.Textview_artist);
        artist.setText(audioInfo.getDescription().getTitle());
        TextView composition =  (TextView)mView.findViewById(R.id.Textview_composition);
        composition.setText(audioInfo.getDescription().getSubtitle());
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
                Log.e(TAG,"Connection failed");
                throw new RuntimeException(ex);
            }

            mMediaController.registerCallback(controllerCallback);


            Log.e(TAG,"Connection succeded");

        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.e(TAG,"Connection failed (onConnectionFailed)");
        }
    };

    public void buildTransportControls()
    {
        final Button button = (Button)mView.findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri uri = Uri.parse("http://srv58.listentoyoutube.com/download/4piUbWlgm66npa9tnt+UbHFjoGRrY3Fu4q/Mq62V1oajjKit2tjFnQ==/Lou%20Reed%20-%20Satellite%20of%20Love.mp3");

                int state = mMediaController.getPlaybackState().getState();
                if (state == PlaybackStateCompat.STATE_PLAYING) {
                    mMediaController.getTransportControls().pause();
                    Log.e(TAG,"pause call");
                    button.setText("Play");
                }
                else
                   if (state == PlaybackStateCompat.STATE_PAUSED)
                       //where is resume
                   {
                       mMediaController.getTransportControls().play();
                       Log.e(TAG,"play(resume) call");
                       button.setText("Pause");
                   }
                else
                   {
                       mMediaController.getTransportControls().playFromUri(uri,null);
                       Log.e(TAG,"play from uri call");
                       button.setText("Pause");
                   }



                Log.e(TAG,"Button pressed");

            }
        });

    }


    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {

                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state)
                {


                }


            };



}
