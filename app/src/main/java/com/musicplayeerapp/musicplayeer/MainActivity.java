package com.musicplayeerapp.musicplayeer;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AudioListFragment.OnListFragmentInteractionListener,
        AudioPlayFragment.OnFragmentInteractionListener
{

    private static final String TAG = "MainActivity";


    @Override
    public void onListLoaded(List<MediaBrowserCompat.MediaItem> newItems) {
        Log.i(TAG,"onListLoaded called");
        AudioListFragment frag = (AudioListFragment)getSupportFragmentManager().findFragmentById(R.id.audiolist);
        frag.setList(newItems);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(MediaBrowserCompat.MediaItem audioInfo)
    {
        if (audioInfo == null) return;
        AudioPlayFragment frag = (AudioPlayFragment)getSupportFragmentManager().findFragmentById(R.id.audioplay);
        if (audioInfo.isPlayable())
            frag.setAudio(audioInfo);
        else
            if (audioInfo.isBrowsable())
            {
                frag.setAlbum(audioInfo);
            }
        else
                Log.e(TAG,"error (onClick)");


    }






}
