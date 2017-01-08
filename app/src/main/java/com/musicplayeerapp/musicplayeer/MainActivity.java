package com.musicplayeerapp.musicplayeer;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AudioListFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    List<MediaBrowserCompat.MediaItem> audioList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG,"mainactivity oncreate");
        setContentView(R.layout.activity_main);





        loadDummyAudioList();
        AudioListFragment frag = (AudioListFragment)getSupportFragmentManager().findFragmentById(R.id.audiolist);
        frag.setList(audioList);
        Log.e(TAG,"mainactivity oncreate finished");
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    protected void loadDummyAudioList()
    {
        audioList = new ArrayList<>();
        //for (int i = 0; i < 100; i++)
        //    audioList.add(AudioInfo.generateDummy(i));



    }



    public void onClick(MediaBrowserCompat.MediaItem audioInfo)
    {
        AudioPlayFragment frag = (AudioPlayFragment)getSupportFragmentManager().findFragmentById(R.id.audioplay);
        frag.setAudio(audioInfo);
    }






}
