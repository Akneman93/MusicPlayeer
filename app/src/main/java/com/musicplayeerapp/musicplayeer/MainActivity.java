package com.musicplayeerapp.musicplayeer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AudioListFragment.OnListFragmentInteractionListener
{

    private static final String TAG = "MainActivity";
    private boolean mDualPane;
    private AudioPlayFragment playFrag;
    AudioListFragment listFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View playLayout = findViewById(R.id.playlayout);

        mDualPane = playLayout != null && playLayout.getVisibility() == View.VISIBLE;

        if (!mDualPane) {
            listFragment = new AudioListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listlayout, (Fragment)listFragment)
                    .commit();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(MediaBrowserCompat.MediaItem audioInfo)
    {

        if (audioInfo == null) return;

        if (mDualPane) {
            Log.i(TAG,"dualpane");

            if (playFrag == null) {
                Log.i(TAG,"first use");
                playFrag = new AudioPlayFragment();
                playFrag.setArguments(MediaItemLoader.putItemInBundle(new Bundle(),audioInfo));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.playlayout, (Fragment)playFrag).commit();
            }
            else {
                Log.i(TAG,"setAudio call");
                playFrag.setAudio(audioInfo);
            }
        }
        else
        {
            Log.i(TAG,"not dualpane");
            playFrag = new AudioPlayFragment();
            playFrag.setArguments(MediaItemLoader.putItemInBundle(new Bundle(),audioInfo));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listlayout, (Fragment)playFrag)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }



    }






}
