package com.musicplayeerapp.musicplayeer;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class AudioListFragment extends Fragment {


    private static final String TAG = "AudioListFragment";

    MediaBrowserCompat mMediaBrowser;


    private OnListFragmentInteractionListener mListener;
    private List<MediaBrowserCompat.MediaItem> mAudioList;
    private MyAudioRecyclerViewAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;


    public AudioListFragment() {
    }


    @SuppressWarnings("unused")
    public static AudioListFragment newInstance(int columnCount) {
        AudioListFragment fragment = new AudioListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");


        mMediaBrowser = new MediaBrowserCompat(getActivity(),
                new ComponentName(getActivity(), PlayMusicService.class),
                mConnectionCallbacks,
                null);
        mMediaBrowser.connect();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
        mMediaBrowser.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.audio_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        //if we returned from backstack, then we have initialized mAudioList
        if (mAudioList != null)
            //reset mRecyclerView
             mRecyclerView.setAdapter(new MyAudioRecyclerViewAdapter(mAudioList, onClickListener));

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            Log.i(TAG," must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,"onDetach");
        mListener = null;
        mAudioList = null;
        mRecyclerView = null;
        mRecyclerAdapter = null;
    }


    public interface OnListFragmentInteractionListener {

        void onClick(MediaBrowserCompat.MediaItem audioInfo);

    }




    OnListFragmentInteractionListener onClickListener = new OnListFragmentInteractionListener()
    {
        @Override
        public void onClick(MediaBrowserCompat.MediaItem audioInfo) {

            if (audioInfo.isPlayable()) {
                Log.i(TAG, "onClick:onClick");
                mListener.onClick(audioInfo);
            }

            else
            if  (mMediaBrowser != null) {
                Log.i(TAG, "onClick:subscribe");
                mMediaBrowser.subscribe("notImportant", subscriptionCallback);
            }
            else
                Log.i(TAG, "mediaBrowser null");
        }
    };



    MediaBrowserCompat.SubscriptionCallback subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback()
    {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            Log.i(TAG,"onChildrenLoaded (subscriptioncallback) called");
            mAudioList = new ArrayList<>(children);
            Log.i(TAG, String.valueOf(children.size()) + " childrens loaded");
            mRecyclerView.setAdapter(new MyAudioRecyclerViewAdapter(mAudioList, onClickListener));
        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
            Log.i(TAG,"subscriptionCallback error");
        }
    };

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallbacks =  new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            Log.i(TAG,"Connection succeded");
            mMediaBrowser.subscribe("notImportant", subscriptionCallback);

        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.i(TAG,"Connection failed (onConnectionFailed)");
        }
    };



}
