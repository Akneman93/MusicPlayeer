package com.musicplayeerapp.musicplayeer;

import android.content.ComponentName;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AudioPlayFragment extends Fragment {

    private static final String TAG = "AudioPlayFragment";

    MediaBrowserCompat mMediaBrowser;
    MediaControllerCompat mMediaController;
    View mView;
    MediaBrowserCompat.MediaItem mItem;
    OnFragmentInteractionListener mListener;
    List<MediaBrowserCompat.MediaItem> mItems;



    public AudioPlayFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

            Log.e(TAG," must implement OnFragmentInteractionListener");
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");

        }
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

       public void onListLoaded(List<MediaBrowserCompat.MediaItem> newItems);
    }

    public void setAudio(MediaBrowserCompat.MediaItem item)
    {
        TextView artist =  (TextView)mView.findViewById(R.id.Textview_description);
        artist.setText(item.getDescription().getTitle() + " " + item.getDescription().getSubtitle());
        mItem = item;

        if (mMediaController != null)
            mMediaController.getTransportControls().playFromUri(mItem.getDescription().getMediaUri(),null);



    }


    public void setAlbum(MediaBrowserCompat.MediaItem item)
    {
        mMediaBrowser.subscribe(item.getMediaId(),subscriptionCallback);

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

            mMediaBrowser.subscribe(MediaItemLoader.ALBUMS_REQEST, subscriptionCallback);


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

                //Uri uri = Uri.parse("http://srv58.listentoyoutube.com/download/4piUbWlgm66npa9tnt+UbHFjoGRrY3Fu4q/Mq62V1oajjKit2tjFnQ==/Lou%20Reed%20-%20Satellite%20of%20Love.mp3");

                Log.e(TAG,"Button pressed");



                int state = mMediaController.getPlaybackState().getState();
                if (state == PlaybackStateCompat.STATE_PLAYING) {
                    mMediaController.getTransportControls().pause();
                    Log.e(TAG,"pause call");

                }
                else
                   if (state == PlaybackStateCompat.STATE_PAUSED)
                       //where is resume
                   {
                       mMediaController.getTransportControls().play();
                       Log.e(TAG,"play(resume) call");

                   }
                else
                   {

                       if (mItem != null)
                           mMediaController.getTransportControls().playFromUri(mItem.getDescription().getMediaUri(),null);
                       Log.e(TAG,"play from uri call");

                   }
            }
        });
    }


    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    Log.e(TAG,"playback state changed (onMetaDataChanged)");
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state)
                {
                    final Button button = (Button)mView.findViewById(R.id.start_button);
                    if (state.getState() == PlaybackStateCompat.STATE_PAUSED)
                        button.setText("Play");
                    else
                        if (state.getState() == PlaybackStateCompat.STATE_PLAYING)
                            button.setText("Pause");
                    else
                        if (state.getState() == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT)
                        {
                            Log.i(TAG, "skip to next (onPlaybackStateChanged)");
                            int id = Integer.parseInt(mItem.getMediaId());
                            Log.i(TAG, "id = " + mItem.getMediaId());
                            id++;
                            MediaBrowserCompat.MediaItem newItem = mItems.get(id % mItems.size());
                            setAudio(newItem);

                        }
                    else
                        button.setText("Play");

                }
            };

    MediaBrowserCompat.SubscriptionCallback subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback()
    {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            Log.e(TAG,"onChildrenLoaded (subscriptioncallback) called");

            mItems = new ArrayList<>(children);
            if (mListener != null)
                mListener.onListLoaded(mItems);
            else
                Log.e(TAG,"onChildrenLoaded (subscriptioncallback) listener == null");



        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
            Log.e(TAG,"subscriptionCallback error");
        }


    };









}
