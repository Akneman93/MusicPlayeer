package com.musicplayeerapp.musicplayeer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 08.01.2017.
 */

public class MediaItemLoader {
    public static final String TAG = "MediaItemLoader";

    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String SUBTITLE_KEY = "SUBTITLE_KEY";
    public static final String URI_KEY = "URI_KEY";
    public static final String MEDIA_ID_KEY = "MEDIA_ID_KEY";



    Context mContext;
    ContentResolver mContentResolver;

    public MediaItemLoader(Context context)
    {
        mContext = context;
        mContentResolver = context.getContentResolver();    }




    /** Get all songs sorted by title */

    public List<MediaBrowserCompat.MediaItem> getSongs()
    {

        MediaDescriptionCompat.Builder  builder = new  MediaDescriptionCompat.Builder();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, null, sortOrder);


        List<MediaBrowserCompat.MediaItem> mItems = new ArrayList<>();

        int i = 0;

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
              //  Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                String Title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
              //  Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                String Data =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
               // Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));



                builder.setTitle(Artist.equals("<unknown>") ? "" : Artist);
                builder.setSubtitle(Title.equals("<unknown>") ? "" : Title);
                builder.setMediaUri(Uri.parse(Data));
                builder.setMediaId(new Integer(i).toString());
                mItems.add(new MediaBrowserCompat.MediaItem(builder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
                i++;
            }
        }
        cursor.close();

        Log.i(TAG, "Loaded: " + String.valueOf(mItems.size()));

        return mItems;
    }


    // TODO: 12.01.2017
    /** main function to get media items based on onLoadChildren's parentID */
    public List<MediaBrowserCompat.MediaItem> getItems(final String parentMediaID)    {

        return getSongs();
    }


    public static Bundle putItemInBundle(final Bundle bundle, final MediaBrowserCompat.MediaItem item)
    {
        bundle.putString(TITLE_KEY, item.getDescription().getTitle().toString());
        bundle.putString(SUBTITLE_KEY, item.getDescription().getSubtitle().toString());
        bundle.putString(URI_KEY, item.getDescription().getMediaUri().toString());
        bundle.putString(MEDIA_ID_KEY, item.getMediaId());

        return bundle;
    }

    public static MediaBrowserCompat.MediaItem getItemFromBundle(final Bundle bundle)
    {

        MediaDescriptionCompat.Builder  builder = new  MediaDescriptionCompat.Builder();

        builder.setTitle(bundle.getString(TITLE_KEY));
        builder.setSubtitle(bundle.getString(SUBTITLE_KEY));
        builder.setMediaId(bundle.getString(MEDIA_ID_KEY));
        builder.setMediaUri(Uri.parse(bundle.getString(URI_KEY)));

        return new MediaBrowserCompat.MediaItem(builder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
    }



}
