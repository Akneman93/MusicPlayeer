package com.musicplayeerapp.musicplayeer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class AudioListFragment extends Fragment {


    private OnListFragmentInteractionListener mListener;
    private List<MediaBrowserCompat.MediaItem> mAudioList = new ArrayList<>();
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
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.audio_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mAudioList = null;
        mRecyclerView = null;
        mRecyclerAdapter = null;
    }


    public interface OnListFragmentInteractionListener {

        void onClick(MediaBrowserCompat.MediaItem audioInfo);

    }


    public void setList(List<MediaBrowserCompat.MediaItem> audiolist)
    {
        mRecyclerView.setAdapter(new MyAudioRecyclerViewAdapter(audiolist, mListener));
    }



}
