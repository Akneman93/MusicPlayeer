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
        public final TextView mDescription;

        public MediaBrowserCompat.MediaItem mInfo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDescription = (TextView) view.findViewById(R.id.itemdescription);

        }

        public void setAudioInfo(MediaBrowserCompat.MediaItem audioinfo)
        {
            mInfo = audioinfo;
            String descr = mInfo.getDescription().getTitle().toString() + " ";
            descr += mInfo.getDescription().getSubtitle();
            mDescription.setText(descr);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescription.getText();
        }
    }
}
