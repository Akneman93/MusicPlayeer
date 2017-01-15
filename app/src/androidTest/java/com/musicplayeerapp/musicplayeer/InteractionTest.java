package com.musicplayeerapp.musicplayeer;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)


// tests interaction
public class InteractionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    //main acitivity
    MainActivity mainActivity;

    //contains list of items to play
    RecyclerView recView;

    //playing item description(title+subtitle)
    TextView playingItemDescView;


    // play-pause image used as button
    ImageView playImage;


    // perform click on item on positon pos in recyclerview on UI thread
    private void performItemClickOnUIThread(final int pos)
    {

        Runnable runnable =  new Runnable() {
            public void run() {
                recView.findViewHolderForAdapterPosition(pos).itemView.performClick();
            }};
        mActivityRule.getActivity().runOnUiThread(runnable);
    }

    private void performPlayImageClickOnUIThread()
    {

        Runnable runnable =  new Runnable() {
            public void run() {
                playImage.performClick();
            }};
        mActivityRule.getActivity().runOnUiThread(runnable);
    }


    /** tests interaction with list of items and play button*/
    @Test
    public void InteractionTest() {


        mainActivity = (MainActivity)mActivityRule.getActivity();
        recView = (RecyclerView)mainActivity.findViewById(R.id.recyclerView);

        assertNotNull(mainActivity);
        assertNotNull(recView);



        final int Position = 0;
        final int WaitingTime = 2000;

        MyAudioRecyclerViewAdapter adapter = (MyAudioRecyclerViewAdapter)recView.getAdapter();

        //check recyclerView is't empty
        assertFalse(adapter.getItemList().size() == 0);
        assertFalse(adapter.getItemCount() == 0);

        // get  media item in recyclerView at Position
        MediaBrowserCompat.MediaItem Item = adapter.getItemList().get(Position);


        // perform click on UI thread
        performItemClickOnUIThread(Position);

        //wait for changes to happen
        try {
            Thread.sleep(WaitingTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        playingItemDescView = (TextView)mainActivity.findViewById(R.id.Textview_description);
        playImage = (ImageView)mainActivity.findViewById(R.id.play);

        assertNotNull(playingItemDescView);
        assertNotNull(playImage);


        //check if playing item's description correspond to the one of clicked item
        assertEquals(playingItemDescView.getText().toString(), Item.getDescription().getTitle() + " " + Item.getDescription().getSubtitle());

        //check if AudiPlayFragment is visible after click
        assertNotNull(mainActivity.playFrag);
        assertTrue(mainActivity.playFrag.isVisible());

        //player should be playing already
        assertEquals(mainActivity.playFrag.getSessionState().getState(), PlaybackStateCompat.STATE_PLAYING);

        //current image should be pause
        assertTrue(mainActivity.playFrag.getCurrentPlayImageId() == mainActivity.playFrag.pause_image_id);


        // pause player
        performPlayImageClickOnUIThread();

        try {
            Thread.sleep(WaitingTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        //current image should be play
        assertTrue(mainActivity.playFrag.getCurrentPlayImageId() == mainActivity.playFrag.play_image_id);

        //player should be paused
        assertEquals(mainActivity.playFrag.getSessionState().getState(), PlaybackStateCompat.STATE_PAUSED);

        // resume player
        performPlayImageClickOnUIThread();

        try {
            Thread.sleep(WaitingTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        //current image should be pause
        assertTrue(mainActivity.playFrag.getCurrentPlayImageId() == mainActivity.playFrag.pause_image_id);

        //player should be playing
        assertEquals(mainActivity.playFrag.getSessionState().getState(), PlaybackStateCompat.STATE_PLAYING);

    }











}
