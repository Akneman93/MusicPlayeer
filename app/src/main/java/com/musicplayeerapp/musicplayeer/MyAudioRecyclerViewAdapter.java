package com.musicplayeerapp.musicplayeer;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicplayeerapp.musicplayeer.AudioListFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MyAudioRecyclerViewAdapter extends RecyclerView.Adapter<MyAudioRecyclerViewAdapter.ViewHolder> {

    //private final List<DummyItem> mValues;
    private final List<MediaBrowserCompat.MediaItem> mAudioList;
    private final OnListFragmentInteractionListener mListener;


    public MyAudioRecyclerViewAdapter(List<MediaBrowserCompat.MediaItem> audiolist, OnListFragmentInteractionListener listener) {
        //mValues = items;
        mListener = listener;
        mAudioList = audiolist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MediaBrowserCompat.MediaItem audioInfo = mAudioList.get(position);
        holder.setAudioInfo(audioInfo);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.onClick(holder.mInfo);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return mAudioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mArtistView;
        public final TextView mCompositionView;

        public MediaBrowserCompat.MediaItem mInfo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mArtistView = (TextView) view.findViewById(R.id.artist);
            mCompositionView = (TextView) view.findViewById(R.id.composition);
        }

        public void setAudioInfo(MediaBrowserCompat.MediaItem audioinfo)
        {
            mInfo = audioinfo;
            mArtistView.setText(mInfo.getDescription().getTitle());
            mCompositionView.setText(mInfo.getDescription().getSubtitle());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mArtistView.getText() + "'"+ mCompositionView.getText();
        }
    }
}
